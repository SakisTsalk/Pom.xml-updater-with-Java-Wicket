package com.sakis.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by sakis on 5/9/2017.
 */
public class AboutPage extends BasePage {

    public AboutPage(final PageParameters parameters) {

        Form<?> form = new Form<Void>("form");

        Link homePageLink = new Link("homePage") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        form.add(homePageLink);

        Link homePageLink2 = new Link("homePage2") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        form.add(homePageLink2);
        add(form);
    }
}
