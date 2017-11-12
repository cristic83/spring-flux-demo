package com.cristi.springflux.annotations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

import java.io.IOException;

public class Server {

    public static final String HOST = "localhost";

    public static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException, IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.registerBean(WebFluxConfiguration.class);
        applicationContext.refresh();

        Server server = new Server();
        server.start(applicationContext);

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public void start(ApplicationContext applicationContext) throws InterruptedException {

        HttpServer server = HttpServer.create(HOST, PORT);

        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();

        server.newHandler(getReactorAdapter(httpHandler)).block();
    }

    private ReactorHttpHandlerAdapter getReactorAdapter(HttpHandler httpHandler) {
        return new ReactorHttpHandlerAdapter(httpHandler);
    }
}
