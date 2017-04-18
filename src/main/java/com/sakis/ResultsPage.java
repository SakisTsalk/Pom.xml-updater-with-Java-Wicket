package com.sakis;

import com.sakis.pomdepenencies.Dependencies;
import com.sakis.pomdepenencies.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.string.StringValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
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

        ArrayList<Properties> propertieslist = new ArrayList<Properties>();


        Document doc;

        String groupidString;

        String latestVersion = new String();

        String artifactidString;

        String versionString = new String();

        String propName;

        String propVersion;

        String newVersion;


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


            NodeList pList = doc.getElementsByTagName("properties");

           if (pList.getLength()!= 0) {


               Node pNode = pList.item(0);


               if (pNode.getNodeType() == Node.ELEMENT_NODE) {
                   Element eElement = (Element) pNode;

                   NodeList n1 = eElement.getElementsByTagName("*");


                   for (int i = 0; i < n1.getLength(); i++) {
                       Node eNode = n1.item(i);

                       Element elElement = (Element) eNode;

                       propName = eNode.getNodeName();
                       propVersion = eNode.getTextContent();


                       Properties properties = new Properties(propName, propVersion);

                       propertieslist.add(properties);

                   }
               }
           }

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {



                    Element eElement = (Element) nNode;




                    groupidString =   eElement.getElementsByTagName("groupId").item(0).getTextContent();
                    artifactidString = eElement.getElementsByTagName("artifactId").item(0).getTextContent();

                    NodeList n1 = eElement.getElementsByTagName("version");
                    if (n1.getLength() > 0) {
                        versionString = n1.item(0).getTextContent();
                    }

                    if (versionString.startsWith("${")) {
                        newVersion = versionString.replaceAll("[${}]", "");

                        for (int i = 0; i < propertieslist.size(); i++) {

                            if (newVersion.equals(propertieslist.get(i).getName())) {
                                versionString = propertieslist.get(i).getVersion();
                            }
                        }
                    }

                    URL url = null;
                    try {
                       url = new URL( "http://search.maven.org/solrsearch/select?q=g:%22"+groupidString+"%22%20AND%20a:%22"+artifactidString+"%22&rows=20&wt=json");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    File responsefile = new File(UPLOAD_FOLDER+"response.json");

                    try {
                        FileUtils.copyURLToFile(url, responsefile);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONParser parser = new JSONParser();

                    try {

                        Object obj = parser.parse(new FileReader(UPLOAD_FOLDER+"response.json"));

                        JSONObject jsonObject = (JSONObject) obj;

                        // JSONArray results = (JSONArray) jsonObject.get("response");

                        JSONObject resultObject = (JSONObject) jsonObject.get("response");

                        JSONArray docs = (JSONArray) resultObject.get("docs");

                        JSONObject docsObject = (JSONObject) docs.get(0);




                        latestVersion = (String) docsObject.get("latestVersion").toString();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                        Dependencies dependency = new Dependencies(groupidString, artifactidString, versionString, latestVersion);

                        dependencylist.add(dependency);



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

                item.add(new Label("newversion", dep.getNewversion()));


            }
        };


        form.add(listView);

        add(form);

    }

}