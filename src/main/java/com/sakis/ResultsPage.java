package com.sakis;

import com.sakis.pomdepenencies.Dependencies;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.string.StringValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by sakis on 3/22/2017.
 */
public class ResultsPage extends BasePage {

    private String filename;

    private String UPLOAD_FOLDER = "C:\\Users\\sakis\\Desktop\\THESIS\\";

    private Dependencies dependency;

    public ResultsPage(final PageParameters parameters) {

        ArrayList<Dependencies> dependencylist = new ArrayList<Dependencies>();


        Document doc;

        String output = "empty";

        String groupidString;

        String artifactidString;

        String versionString;


        Form<?> form = new Form<Void>("form");


//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setValidating(false);
//        DocumentBuilder db = null;
//        try {
//            db = dbf.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        try {
//             doc = db.parse(new FileInputStream(new File(UPLOAD_FOLDER +"test.xml")));
//            try {
//                 output = prettyPrint(doc);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        StringValue filen = parameters.get("filename");

      String  filename = filen.toString();






        try {

            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("dependency");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {



                    Element eElement = (Element) nNode;

                    groupidString =   eElement.getElementsByTagName("groupId").item(0).getTextContent();
                    artifactidString = eElement.getElementsByTagName("artifactId").item(0).getTextContent();
                    versionString = eElement.getElementsByTagName("version").item(0).getTextContent();

                    Dependencies dependency = new Dependencies(groupidString,artifactidString,versionString);

                    dependencylist.add(dependency);

//                    System.out.println("Group ID : " + eElement.getElementsByTagName("groupId").item(0).getTextContent());
//                    System.out.println("Artifact ID : " + eElement.getElementsByTagName("artifactId").item(0).getTextContent());
//                    System.out.println("Version : " + eElement.getElementsByTagName("version").item(0).getTextContent());


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        ListView listView = new ListView("listView", dependencylist){
            protected void populateItem(ListItem item) {
                Dependencies dep = (Dependencies) item.getModelObject();

                item.add(new Label("groupid", dep.getGroupid()));

                item.add(new Label("artifactid", dep.getArtifactid()));

                item.add(new Label("version", dep.getVersion()));




            }
        };


       // MultiLineLabel resultlabel = new MultiLineLabel("resultlabel",output);


        System.out.println(output);

        form.add(listView);

        add(form);

    }

    public static  String prettyPrint(Document xml) throws Exception {

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));

        return out.toString();
    }


    }