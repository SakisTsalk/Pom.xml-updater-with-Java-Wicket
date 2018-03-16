package com.sakis;

import com.sakis.web.AboutPage;
import com.sakis.web.HomePage;
import com.sakis.web.ResultsPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import java.io.File;
import java.io.IOException;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 

 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */





	@Override
	public void init()

	{

		try {



			File uploadfolder = new File("pomuploads");
			if (!uploadfolder.exists()) {
				if (uploadfolder.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}

			File userstxt = new File("pomuploads"+File.separator+"users.txt");

			if (userstxt.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}



		getDebugSettings().setAjaxDebugModeEnabled(false);
		super.init();

		mountPage("Results", ResultsPage.class);
		mountPage("About", AboutPage.class);

		mountPage("Home", HomePage.class);


		// add your configuration here
	}
}
