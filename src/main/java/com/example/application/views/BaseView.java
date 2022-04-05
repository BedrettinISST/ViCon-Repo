package com.example.application.views;

import com.example.application.service.keycloak.AuthenticationService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Set;

public abstract class BaseView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private HorizontalLayout locationLayout;

    public BaseView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.setSpacing(false);
        this.setPadding(false);
        locationLayout = new HorizontalLayout();
        add(locationLayout);
    }

    public void showErrorNotification(String message) {
        if (getUI().isPresent()) {
            UI ui = getUI().get();
            ui.access(() -> {
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

                Div text = new Div(new Text(message));

                Button closeButton = new Button(new Icon("lumo", "cross"));
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeButton.addClickListener(event -> notification.close());

                HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                layout.setAlignItems(Alignment.CENTER);

                notification.add(layout);
                notification.open();
            });
        }
    }

    public void setLocation(String location) {
        remove(locationLayout);
        locationLayout = new HorizontalLayout();
        locationLayout.getStyle().set("background", "#FFFFFF");
        locationLayout.getStyle().set("height", "50px");
        locationLayout.getStyle().set("padding", "8px 24px");
        locationLayout.setAlignItems(Alignment.CENTER);

        Div locationTextDiv = new Div();
        locationTextDiv.getStyle().set("font-weight", "bold");
        locationTextDiv.add(location);
        locationLayout.setWidthFull();
        locationLayout.add(locationTextDiv);
        locationLayout.expand(locationTextDiv);
        addComponentAsFirst(locationLayout);
    }

    public void addMenu(MenuBar menuBar) {
        menuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);
        locationLayout.add(menuBar);
    }

}
