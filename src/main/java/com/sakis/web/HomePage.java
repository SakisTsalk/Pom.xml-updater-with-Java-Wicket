package com.sakis.web;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;

import java.nio.file.Paths;


public class HomePage extends BasePage implements IAjaxIndicatorAware {

    private FileUploadField fileUpload;
    private String UPLOAD_FOLDER = Paths.get(".").toAbsolutePath().toString();;


	public HomePage(final PageParameters parameters) {

        fileUpload = new FileUploadField("fileUpload");

        add(new FeedbackPanel("feedback"));
         Form<?> form = new Form<Void>("form");


        AjaxButton ab = new AjaxButton("submit") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {


                if (target!=null)
            {

                final FileUpload uploadedFile = fileUpload.getFileUpload();
                 if (uploadedFile != null) {

                     String extension = FilenameUtils.getExtension(uploadedFile.getClientFileName());
                     String name = uploadedFile.getClientFileName();

                     if (name.equals("pom.xml") && (extension.equals("xml"))) {





                         File newFile = new File(UPLOAD_FOLDER
                                 + uploadedFile.getClientFileName());

                         if (newFile.exists()) {
                             newFile.delete();
                         }

                         try {
                             newFile.createNewFile();
                             uploadedFile.writeTo(newFile);


                         } catch (Exception e) {
                             throw new IllegalStateException("Error");
                         }

                         PageParameters pageParameters = new PageParameters();

                         pageParameters.add("filename", UPLOAD_FOLDER
                                 + uploadedFile.getClientFileName());

                         pageParameters.add("uploadfolder", UPLOAD_FOLDER);

                         setResponsePage(ResultsPage.class, pageParameters);
                     } else
                         info("Only pom.xml files are accepted");
                 }
             }
            }
        };



        form.add(ab);
        form.setMultiPart(true);

        //form.setMaxSize(Bytes.kilobytes(30));



        fileUpload.setRequired(true);

        form.add(fileUpload);



        add(form);



    }


    @Override
    public String getAjaxIndicatorMarkupId() {
        return "loadingIndicator_id";
    }
}
