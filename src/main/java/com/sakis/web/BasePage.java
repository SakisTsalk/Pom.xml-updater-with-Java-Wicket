package com.sakis.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * Created by sakis on 3/22/2017.
 */
public class    BasePage extends WebPage  {



    public BasePage() {

        Link homePageLink = new Link("homePage") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        add(homePageLink);

        Link homePageLink2 = new Link("homePage2") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        add(homePageLink2);


    }
}
