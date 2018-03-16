package com.sakis.web;

import  org.apache.commons.io.FilenameUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Paths;


public class HomePage extends BasePage implements IAjaxIndicatorAware {

    private FileUploadField fileUpload;

    private String UPLOAD_FOLDER = "PomUploads\\";


    public HomePage(final PageParameters parameters) {


        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

        add(feedbackPanel);

        feedbackPanel.setOutputMarkupId(true);

        Form<?> form = new Form<Void>("form");


        AjaxButton ab = new AjaxButton("submit") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                target.add(feedbackPanel);
                if (target != null) {
                    final FileUpload uploadedFile = fileUpload.getFileUpload();
                    if (uploadedFile != null) {

                        String extension = FilenameUtils.getExtension(uploadedFile.getClientFileName());
                        String name = uploadedFile.getClientFileName();

                        if (name.equals("pom.xml") && (extension.equals("xml"))) {

                            int usercount =0;

                           usercount = ChangeUserCount(UPLOAD_FOLDER,usercount);

                            System.out.println(usercount);

                            File dir = new File(UPLOAD_FOLDER + usercount);
                            dir.mkdir();

                            File newFile = new File(UPLOAD_FOLDER + usercount +"\\"
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

                            pageParameters.add("filename", UPLOAD_FOLDER + usercount +"\\"
                                    + uploadedFile.getClientFileName());

                            pageParameters.add("uploadfolder", UPLOAD_FOLDER + usercount +"\\");


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
        fileUpload = new FileUploadField("fileUpload");

        fileUpload.setRequired(true);


        form.add(fileUpload);


        add(form);


    }


    @Override
    public String getAjaxIndicatorMarkupId() {
        return "loadingIndicator_id";
    }


    public int ChangeUserCount(String folder, int  usercount) {


        try {
            BufferedReader file = new BufferedReader(new FileReader(folder+"users.txt"));
            String number;
            StringBuffer inputBuffer = new StringBuffer();

            if ((number = file.readLine()) != null) {
                inputBuffer.append(number);
                usercount = Integer.parseInt(number);
            }

            file.close();


            String inputStr = inputBuffer.toString();



             int newcount = usercount + 1;
             inputStr =  String.valueOf(newcount);

            FileOutputStream fileOut = new FileOutputStream(folder+"users.txt");
            fileOut.write(inputStr.getBytes());
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

           return  usercount;


    }

}