package ru.sa2;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import ru.sa2.football_stats.Main;

/**
 * Created by AlVyaSmirnov on 15.02.2015.
 */
public class Launcher {
    public static void main(String[] args) throws Exception{
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);

        connector.setPort(8088);
        server.addConnector(connector);

        WebAppContext football_stats = new WebAppContext("web-apps/web", "/web/*");
        ServletContextHandler servlet = new ServletContextHandler();
        servlet.addServlet(Main.class, "/24score/*");

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[] { football_stats, servlet });

        server.setHandler(handlerCollection);
        server.start();
    }
}
