package com.sakis.web;

import com.sakis.pomManager.PomxmlManager;
import com.sakis.pomManager.PomxmlManagerImpl;
import com.sakis.pomManager.pomdepenencies.Dependencies;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;

/**
 * Created by sakis on 3/22/2017.
 */
public class ResultsPage extends BasePage {


    public ResultsPage(final PageParameters parameters) {

        ArrayList<Dependencies> dependencylist = new ArrayList<Dependencies>();

        Form<?> form = new Form<Void>("form");

        StringValue filen = parameters.get("filename");

        StringValue uploadn = parameters.get("uploadfolder");

        String  filename = filen.toString();

        String  uploadfolder = uploadn.toString();

        File fXmlFile = new File(filename);
        File responsefile = new File(uploadfolder+"response.json");
        PomxmlManager pomxml = new PomxmlManagerImpl();
        pomxml.GetPomResults(fXmlFile,dependencylist,responsefile);


        ListView listView = new ListView("listView", dependencylist){
            protected void populateItem(ListItem item) {

                Dependencies dep = (Dependencies) item.getModelObject();
                Label versionlabel =  new Label("version", dep.getVersion());

                item.add(new Label("groupid", dep.getGroupid()));

                item.add(new Label("artifactid", dep.getArtifactid()));
                if (dep.getVersion().equals(dep.getNewversion())){
                    versionlabel.add(new AttributeAppender("style", "color:black;"));}
                    else{
                    versionlabel.add(new AttributeAppender("style", "color:red;"));}

                item.add(versionlabel);

                item.add(new Label("newversion", dep.getNewversion()));


            }
        };


        pomxml.UpdatePomFile(fXmlFile,dependencylist,filename);


        DownloadLink downloadlink = new DownloadLink("link1", fXmlFile, fXmlFile.getName());
        form.add(downloadlink);
        form.add(downloadlink);


        form.add(listView);

        add(form);

    }

}