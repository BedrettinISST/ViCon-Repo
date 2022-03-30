package com.example.application.views;


import com.example.application.views.about.AboutView;
import com.example.application.views.vicon.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.communication.PushMode;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames("menu-item-link");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames("menu-item-text");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                addClassNames("menu-item-icon");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        createHeaderContent();
        addToDrawer(createDrawerContent());
    }

    private Component header() {
        H1 logo = new H1("Vaadin CRM");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        return header;

    }

    private void createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        HorizontalLayout horizontal = new HorizontalLayout(toggle, viewTitle);
        horizontal.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        horizontal.addClassNames("py-0", "px-m");


        VerticalLayout rightItem  = new VerticalLayout(getLoginLogoutButton());
        rightItem.setHorizontalComponentAlignment(FlexComponent.Alignment.END);

        Div b = new Div();
        b.getStyle().set("margin-left", "auto");
        b.add(getLoginLogoutButton());
        addToNavbar(horizontal);
        addToNavbar(b);

    }

    private Component createDrawerContent() {
        H2 appName = new H2("My App");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private Component getLoginLogoutButton() {
        Button loginLogoutButton;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
            String username = keycloakPrincipal.getKeycloakSecurityContext().getIdToken().getPreferredUsername();

            loginLogoutButton = new Button("Ausloggen (" + username + ")");
            loginLogoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            loginLogoutButton.addClickListener(event -> {
                UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
                UI.getCurrent().getPage().setLocation("http://localhost:8091/sso/logout");
            });
        } else {
            loginLogoutButton = new Button("Einloggen");
            loginLogoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            loginLogoutButton.addClickListener(event -> {
                UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
                UI.getCurrent().getPage().setLocation("http://localhost:8091/sso/login");
            });
        }
        return loginLogoutButton;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("menu-item-container");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("navigation-list");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("ViCon", "la la-cloud", LoginView.class), //

                new MenuItemInfo("About", "la la-file", AboutView.class), //

        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
