/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cristi.springflux.function;

import com.cristi.springflux.domain.DummyPersonRepository;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import com.cristi.springflux.domain.PersonRepository;
import reactor.ipc.netty.http.server.HttpServer;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

public class Server {

	public static final String HOST = "localhost";

	public static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.startReactorServer();
//		server.startTomcatServer();

		System.out.println("Press ENTER to exit.");
		System.in.read();
	}

	public RouterFunction<ServerResponse> routingFunction() {
		PersonRepository repository = new DummyPersonRepository();
		PersonHandler handler = new PersonHandler(repository);

		return nest(path("/person"),
				nest(accept(APPLICATION_JSON),
						route(GET("/{id}"), handler::getPerson)
						.andRoute(method(HttpMethod.GET), handler::listPeople)
						.andRoute(POST("/"), handler::createPerson)
				));
	}

	public void startReactorServer() throws InterruptedException {

		RouterFunction<ServerResponse> router = routingFunction();

		HttpHandler httpHandler = getHttpHandler(router);

		ReactorHttpHandlerAdapter adapter = getReactorAdapter(httpHandler);

		HttpServer server = getReactorServer();

		server.newHandler(adapter).block();
	}

	private HttpServer getReactorServer() {
		return HttpServer.create(HOST, PORT);
	}

	private ReactorHttpHandlerAdapter getReactorAdapter(HttpHandler httpHandler) {
		return new ReactorHttpHandlerAdapter(httpHandler);
	}

	public void startTomcatServer() throws LifecycleException {

		RouterFunction<ServerResponse> router = routingFunction();

		HttpHandler httpHandler = getHttpHandler(router);

		ServletHttpHandlerAdapter servlet = getTomcatAdapter(httpHandler);

		Tomcat tomcatServer = getTomcatServer(servlet);

		tomcatServer.start();
	}

	private Tomcat getTomcatServer(ServletHttpHandlerAdapter servlet) {
		Tomcat tomcatServer = new Tomcat();
		tomcatServer.setHostname(HOST);
		tomcatServer.setPort(PORT);
		Context rootContext = tomcatServer.addContext("", System.getProperty("java.io.tmpdir"));
		Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
		rootContext.addServletMapping("/", "httpHandlerServlet");
		return tomcatServer;
	}

	private ServletHttpHandlerAdapter getTomcatAdapter(HttpHandler httpHandler) {
		return new ServletHttpHandlerAdapter(httpHandler);
	}

	private HttpHandler getHttpHandler(RouterFunction<ServerResponse> router) {
		return toHttpHandler(router);
	}

}
