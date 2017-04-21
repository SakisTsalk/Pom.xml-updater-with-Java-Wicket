package com.sakis;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;


public class HomePage extends BasePage {

    private FileUploadField fileUpload;
    private String UPLOAD_FOLDER = "C:\\Users\\sakis\\Desktop\\THESIS\\";
    private boolean flag;


	public HomePage(final PageParameters parameters) {

        fileUpload = new FileUploadField("fileUpload");

        add(new FeedbackPanel("feedback"));
         Form<?> form = new Form<Void>("form") {
             @Override
             protected void onSubmit() {

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

                             info("saved file: " + uploadedFile.getClientFileName());
                         } catch (Exception e) {
                             throw new IllegalStateException("Error");
                         }

                         PageParameters pageParameters = new PageParameters();

                         pageParameters.add("filename", UPLOAD_FOLDER
                                 + uploadedFile.getClientFileName());

                         setResponsePage(ResultsPage.class, pageParameters);
                     } else {
                        flag = false;
                         info("Only pom.xml files are accepted");

                     }

                 }

             };


         };

        form.setMultiPart(true);

        //form.setMaxSize(Bytes.kilobytes(30));

        if (flag == false){



        }


        fileUpload.setRequired(true);

        form.add(fileUpload);



        add(form);



    }


    }
