import java.io.*;
import java.awt.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;


// Import-Klassen f�r das Fenster

import java.awt.event.*;
import javax.swing.*;


//JDOMtransform 2024 C.Knapp, String param[] for transform parameter

public class JDOMtransform 

{
static Document XMLdocument;
static Document XSLTdocument;


JDOMParseFenster ErrorFenster=new JDOMParseFenster(500,200,100,600,"JDOMParse Fehlermeldungen");

    public static void main(String param[])
    {

        if (param.length!=3)
        {
            System.out.println("No transfer parameters!");
            System.exit(1);
        }

    try
     { 
        String LookAndFeel=UIManager.getSystemLookAndFeelClassName();      
        UIManager.setLookAndFeel(LookAndFeel);
     }
    catch(Exception e)
     { 
         System.out.println("Fehler " + e);
     }
        JDOMtransform jdomtransform= new JDOMtransform(param);

   }

public JDOMtransform(String param[])
{

        transforming(param);


}

public void transforming(String param[])
{
 

        File XMLdatei=new File(param[0]);
        boolean XMLladen=XMLdatei.canRead();

        if (XMLladen)
         {
          try
           {
             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
             DocumentBuilder builder = factory.newDocumentBuilder();
             XMLdocument = builder.parse(XMLdatei);
             ErrorFenster.ausgabe("\nTransform starts");

             TransformerFactory tfactory = TransformerFactory.newInstance();
             StreamSource xmlsource = new StreamSource(XMLdatei);

             Source xsltsource = tfactory.getAssociatedStylesheet(xmlsource, null, "html-output", null);
             SaveFile(tfactory,xmlsource,xsltsource,param[1]);
 
             xsltsource = tfactory.getAssociatedStylesheet(xmlsource, null, "wml-output", null);
             SaveFile(tfactory,xmlsource,xsltsource,param[2]);

             ErrorFenster.ausgabe("\nEnd transform");


           }

          catch (SAXParseException error) 
           {
            ErrorFenster.ausgabe("\n+++Parse Error+++"+ "\nZeile: " + error.getLineNumber() + "\nDatei: " + error.getSystemId());
            ErrorFenster.ausgabe("\n" + error.getMessage() );


        }

          catch (ParserConfigurationException pce)
          {
            pce.printStackTrace();
          }
          catch (TransformerConfigurationException tce)
          {
            tce.printStackTrace();
          }
          catch (TransformerException te)
          {
            te.printStackTrace();
          }

          catch (IOException e)
           {
            // I/O error
          ErrorFenster.ausgabe("IO Error:\n"+e);
           }

          catch (Throwable t)
           {
            t.printStackTrace();
           }


        }
       else
       {
         ErrorFenster.ausgabe("No such file!");

       }

    }

 public void SaveFile(TransformerFactory tfactory, StreamSource xmlsource, Source xsltsource, String param) throws TransformerConfigurationException, IOException, TransformerException
{

  Transformer transformer = tfactory.newTransformer(xsltsource);
  FileWriter Ausgabestrom= new FileWriter(param);
  BufferedWriter output = new BufferedWriter(Ausgabestrom);
  StreamResult result = new StreamResult(output);
  transformer.transform(xmlsource, result);
  output.close();
  ErrorFenster.ausgabe("\nFile saved: "+param);



}

}



// Window

class JDOMParseFenster extends JFrame implements WindowListener
{

JTextArea textbereich;

 public JDOMParseFenster(int breite, int hoehe, int posx, int posy, String name)
  {

        setSize (breite,hoehe);
        setLocation (posx,posy);
        setTitle (name);
        textbereich=new JTextArea();
        Container content=getContentPane();
        content.add (textbereich);
        addWindowListener(this);
        setVisible(true);
 
 }

public void ausgabe(String s)
{

  textbereich.append(s);

}


  // Nun die Ereignisse des WindowListener
    public void windowClosing(WindowEvent evt) 
     {
     	  dispose();
     	  System.exit(0);
      }
    public void windowOpened(WindowEvent evt) {   }
    public void windowIconified(WindowEvent evt)  {  }
    public void windowDeiconified(WindowEvent evt)   { }
    public void windowClosed(WindowEvent evt)  { }
    public void windowActivated(WindowEvent evt)  {  }
    public void windowDeactivated(WindowEvent evt) { }
    

}
