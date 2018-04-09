package com.sakis.web;

import com.sakis.pomManager.PomxmlManager;
import com.sakis.pomManager.PomxmlManagerImpl;
import com.sakis.pomManager.pomdepenencies.Dependencies;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;

/**
 * Created by sakis on 3/22/2017.
 */
public class ResultsPage extends BasePage implements IAjaxIndicatorAware {




    ArrayList<Dependencies> selecteddependencieslist;


    public ResultsPage(final PageParameters parameters) {

        final ArrayList<Dependencies> dependencylist = new ArrayList<Dependencies>();

        Form<?> form = new Form<Void>("form");

        StringValue filen = parameters.get("filename");

        StringValue uploadn = parameters.get("uploadfolder");

        final String filename = filen.toString();

        String  uploadfolder = uploadn.toString();

        selecteddependencieslist = new ArrayList<Dependencies>();

        final File fXmlFile = new File(filename);
        File responsefile = new File(uploadfolder+"response.json");
        final PomxmlManager pomxml = new PomxmlManagerImpl();

            pomxml.GetPomResults(fXmlFile,dependencylist,responsefile);

            if (!responsefile.exists()) {
                setResponsePage(ErrorPage.class);
            }

        final CheckGroup<Dependencies> group = new CheckGroup<Dependencies>("group",selecteddependencieslist);

        form.add(group);

        final DownloadLink downloadlink = new DownloadLink("downloadlink", fXmlFile, fXmlFile.getName());



        AjaxButton ab = new AjaxButton("done") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                if (target!=null) {

                    target.add(downloadlink);

                    pomxml.UpdatePomFile(fXmlFile, selecteddependencieslist, filename);
                    downloadlink.setEnabled(true);

                }
            }
        };
        form.add(ab);
        group.add(new CheckGroupSelector("groupselector"));
        ListView<Dependencies> listView = new ListView<Dependencies>("listView", dependencylist){

            @Override
            protected void populateItem(ListItem<Dependencies> item) {

                Dependencies dep = (Dependencies) item.getModelObject();
                Label versionlabel =  new Label("version", dep.getVersion());

                item.add(new Label("groupid", dep.getGroupid()));

                item.add(new Label("artifactid", dep.getArtifactid()));
                if (dep.getVersion().equals(dep.getNewversion())||(dep.getNewversion().equals("NOT FOUND")) || (dep.getVersion().equals("Inherited")) ){
                    versionlabel.add(new AttributeAppender("style", "color:green;"));
                    item.add(new CheckBox("check", Model.of(Boolean.FALSE)).setEnabled(false));}
                    else{
                    versionlabel.add(new AttributeAppender("style", "color:red;"));
                    item.add(new Check<>("check", item.getModel()));
                }

                item.add(versionlabel);

                item.add(new Label("newversion", dep.getNewversion()));

                item.add(new Label("newversiondate", dep.getNewversiondate()));

            }
        };


       // pomxml.UpdatePomFile(fXmlFile, selecteddependencieslist, filename);


        Link homePageLink = new Link("homepagelink") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        form.add(homePageLink);


         downloadlink.setOutputMarkupId(true);


        downloadlink.setEnabled(false);

        form.add(downloadlink);




        listView.setReuseItems(true);

        group.add(listView);

        add(form);

    }


    @Override
    public String getAjaxIndicatorMarkupId() {
        return "loadingIndicator_id";
    }
}