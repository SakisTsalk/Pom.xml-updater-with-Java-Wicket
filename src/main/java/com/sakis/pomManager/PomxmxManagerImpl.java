package com.sakis.pomManager;

import com.sakis.pomManager.pomdepenencies.Dependencies;
import com.sakis.pomManager.pomdepenencies.Properties;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sakis on 4/21/2017.
 */
public class PomxmxManagerImpl implements PomxmlManager {

    @Override
    public void GetPomResults(File fXmlFile, ArrayList<Dependencies> dependencylist, File responsefile) {
        ArrayList<Properties> propertieslist = new ArrayList<Properties>();

        Document doc;

        String groupidString;
        String latestVersion = new String();
        String artifactidString;
        String versionString = new String();
        String propName;
        String propVersion;
        String newVersion;

        try{


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



                    try {
                        FileUtils.copyURLToFile(url, responsefile);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONParser parser = new JSONParser();

                    try {

                        Object obj = parser.parse(new FileReader(responsefile));

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

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdatePomFile(File fXmlFile, ArrayList<Dependencies> dependencylist,String filename) {
        try {

            Document doc;


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("dependency");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Dependencies dep = dependencylist.get(temp);


                    eElement.getElementsByTagName("version").item(0).setTextContent(dep.getNewversion());

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(filename));
                    transformer.transform(source, result);



                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
