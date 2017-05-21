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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sakis on 4/21/2017.
 */
public class PomxmlManagerImpl implements PomxmlManager {

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

        long timestamp;
        Date newVersionDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String date = new String();

        Properties properties;
        URL url = null;

        try{


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();




            NodeList propertiesList = doc.getElementsByTagName("properties");

            if (propertiesList.getLength()!= 0) {

                Node propertiesNode = propertiesList.item(0);


                if (propertiesNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) propertiesNode;

                    NodeList n1 = eElement.getElementsByTagName("*");

                    Node eNode;

                    for (int i = 0; i < n1.getLength(); i++) {
                         eNode = n1.item(i);

                        propName = eNode.getNodeName();
                        propVersion = eNode.getTextContent();


                         properties = new Properties(propName, propVersion);

                        propertieslist.add(properties);

                    }
                }
            }

            NodeList dependencyList = doc.getElementsByTagName("dependency");

            NodeList pluginsList = doc.getElementsByTagName("plugin");

            List<Node> mergedList = ConcatLists(dependencyList,pluginsList);



            Node nNode;

            Element eElement;

            JSONParser parser;
            Object obj;
            JSONObject jsonObject;
            JSONObject resultObject;
            JSONObject docsObject;
            JSONArray docs;

            for (int temp = 0; temp < mergedList.size(); temp++) {

                 nNode = mergedList.get(temp);

                NodeList n1;

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {



                     eElement = (Element) nNode;


                    groupidString =   eElement.getElementsByTagName("groupId").item(0).getTextContent();
                    artifactidString = eElement.getElementsByTagName("artifactId").item(0).getTextContent();


                     n1 = eElement.getElementsByTagName("version");
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


                    try {
//                        url = new URL( "http://search.maven.org/solrsearch/select?q=g:%22"+groupidString+"%22+AND+a:%22"+artifactidString+"%22&core=gav&rows=20&wt=json");

                       url = new URL( "http://search.maven.org/solrsearch/select?q=g:%22"+groupidString+"%22%20AND%20a:%22"+artifactidString+"%22&rows=20&wt=json");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }



                    try {
                        FileUtils.copyURLToFile(url, responsefile);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                     parser = new JSONParser();

                    try {

                         obj = parser.parse(new FileReader(responsefile));

                         jsonObject = (JSONObject) obj;

                         resultObject = (JSONObject) jsonObject.get("response");

                         docs = (JSONArray) resultObject.get("docs");

                        if (docs.isEmpty()){
                            latestVersion = "NOT FOUND";
                            date = "-";
                        }else {
                             docsObject = (JSONObject) docs.get(0);

                            latestVersion = (String) docsObject.get("latestVersion").toString();

                            timestamp = (long) docsObject.get("timestamp");

                            newVersionDate = new Date(timestamp);

                             date = sdf.format(newVersionDate);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Dependencies dependency = new Dependencies(groupidString, artifactidString, versionString, latestVersion,date);

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

            NodeList dependencyList = doc.getElementsByTagName("dependency");

            NodeList pluginsList = doc.getElementsByTagName("plugin");

            List<Node> mergedList = ConcatLists(dependencyList,pluginsList);

           Dependencies dep;

            Node nNode;

            Element eElement;

            for (int temp = 0; temp < mergedList.size(); temp++) {

                 nNode = mergedList.get(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                     eElement = (Element) nNode;

                    for(int i=0; i<dependencylist.size(); i++) {
                          dep = dependencylist.get(i);
                        if (eElement.getElementsByTagName("groupId").item(0).getTextContent().equals(dep.getGroupid())&& eElement.getElementsByTagName("artifactId").item(0).getTextContent().equals(dep.getArtifactid())) {
                            eElement.getElementsByTagName("version").item(0).setTextContent(dep.getNewversion());

                        }

                    }

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

  public List<Node>  ConcatLists(NodeList lista, NodeList listb) {


      List<Node> mergedList = new ArrayList<Node>();

      int listaLength = lista.getLength();
      int listbLength = listb.getLength();

      if(listaLength>0)
      {
          for (int i = 0; i < listaLength; i++)
              mergedList.add(lista.item(i));
      }

      if(listbLength>0)
      {
          for (int i = 0; i < listbLength; i++)
              mergedList.add(listb.item(i));
      }
        return  mergedList;
  }
}
