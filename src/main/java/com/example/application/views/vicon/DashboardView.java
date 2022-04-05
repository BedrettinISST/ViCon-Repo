package com.example.application.views.vicon;

import com.example.application.service.keycloak.AuthenticationService;
import com.example.application.views.BaseView;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Dashboard")
@Route(value = "Dashboard", layout = MainLayout.class)
public class DashboardView extends HorizontalLayout {


    private final AuthenticationService authenticationService;
    @Autowired
    public DashboardView(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;

        createMainDashboardComponents();
    }

    private void createMainDashboardComponents() {
        CreateEinwilligungsanfragenComponent();
        CreateEinwilligungenComponent();
        CreatePraeferenzenComponent();

    }


    private void CreateEinwilligungsanfragenComponent() {
        VerticalLayout layout = new VerticalLayout();
        var vl = new VerticalLayout();
        vl.add(new Text("Neue Einwilligungsanfragen"));
        vl.setAlignItems(Alignment.CENTER);
        layout.setAlignItems(Alignment.STRETCH);
        layout.setWidth("25%");
        layout.add(vl);
        layout.add(new Button("Behandlungskontext"));
        layout.add(new Button("Forschungskontext"));
        layout.getElement().getThemeList().add("backColorGrey");
        layout.setMargin(true);
        add(layout);
    }

    private void CreateEinwilligungenComponent() {
        VerticalLayout layout = new VerticalLayout();
        var vl = new VerticalLayout();
        vl.add(new Text("Getätigte Einwilligungen"));
        vl.setAlignItems(Alignment.CENTER);
        layout.setAlignItems(Alignment.STRETCH);
        layout.setWidth("25%");
        layout.add(vl);
        layout.add(new Button("Behandlungskontext"));
        layout.add(new Button("Forschungskontext"));
        layout.getElement().getThemeList().add("backColorGrey");
        layout.getElement().getStyle().set("background", "darkgray");
        layout.setMargin(true);
        add(layout);
    }

    private void CreatePraeferenzenComponent() {
        Image img = new Image("images/empty-plant.png", "IconPräferenzen");
        img.setWidth("100px");
        img.setHeight("100px");
        var but = new Button(img);
        but.setWidth("100px");
        but.setHeight("100px");
        add(but);
    }


}
