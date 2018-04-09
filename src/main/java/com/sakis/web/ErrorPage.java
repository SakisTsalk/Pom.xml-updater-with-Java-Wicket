package com.sakis.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ErrorPage extends BasePage {

    public ErrorPage(final PageParameters parameters) {

        Form<?> form = new Form<Void>("form");

        Link homePageLink = new Link("homePage") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        form.add(homePageLink);

        add(form);
    }
}
