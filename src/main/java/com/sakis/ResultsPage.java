package com.sakis;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by sakis on 3/22/2017.
 */
public class ResultsPage extends BasePage {

    private String filename;

    private String UPLOAD_FOLDER = "C:\\Users\\sakis\\Desktop\\THESIS\\";

    public ResultsPage(final PageParameters parameters) {

        Document doc;

        String output = "empty";

        Form<?> form = new Form<Void>("form");


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
             doc = db.parse(new FileInputStream(new File(UPLOAD_FOLDER +"test.xml")));
            try {
                 output = prettyPrint(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //output = output.replaceAll("\n", "<br />");


        MultiLineLabel resultlabel = new MultiLineLabel("resultlabel",output);


        System.out.println(output);

        form.add(resultlabel);

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