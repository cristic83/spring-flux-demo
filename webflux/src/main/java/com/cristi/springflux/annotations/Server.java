package com.cristi.springflux.annotations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

import java.io.IOException;

public class Server {

    public static final String HOST = "0.0.0.0";

    public static final int PORT = 80;

    public static void main(String[] args) throws InterruptedException, IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.registerBean(WebFluxConfiguration.class);
        applicationContext.refresh();

        Server server = new Server();
        server.start(applicationContext);

        System.out.println("Using java version: " + System.getProperty("java.version"));
        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public void start(ApplicationContext applicationContext) throws InterruptedException {

        HttpHandler httpHandler = getHttpHandler(applicationContext);

        ReactorHttpHandlerAdapter adapter = getReactorAdapter(httpHandler);

        HttpServer server = getReactorServer();

        server.newHandler(adapter).block();
    }

    private HttpHandler getHttpHandler(ApplicationContext applicationContext) {
        return WebHttpHandlerBuilder.applicationContext(applicationContext).build();
    }

    private HttpServer getReactorServer() {
        return HttpServer.create(HOST, PORT);
    }

    private ReactorHttpHandlerAdapter getReactorAdapter(HttpHandler httpHandler) {
        return new ReactorHttpHandlerAdapter(httpHandler);
    }
}
