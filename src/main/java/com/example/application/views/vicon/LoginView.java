package com.example.application.views.vicon;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.communication.PushMode;
import org.springframework.security.core.context.SecurityContextHolder;

@PageTitle("ViCon")
@Route(value = "ViCon", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class LoginView extends VerticalLayout {

    public LoginView() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Image image = new Image("images/ViCon-Logo-Transparent.png", "ViConLogo2");
        image.setWidth("50%");
        image.setHeight("50%");
        // Use TextField for standard text input

        Icon icon = new Icon();



        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        //addClassName("centered-content");
        VerticalLayout layout = new VerticalLayout();
        layout.add(image);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        //add(image);
        //add(textField, button);
    }

}
