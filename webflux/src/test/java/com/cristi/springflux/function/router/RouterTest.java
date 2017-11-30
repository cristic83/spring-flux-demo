package com.cristi.springflux.function.router;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static reactor.core.publisher.Mono.empty;

public class RouterTest {

    public Router router = new Router();

    @Test
    public void givenHttpRequest_whenGetPersonJsonRequest_thenReturnHandlerFunction() throws URISyntaxException {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .uri(new URI("http://localhost/person"))
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
        assertNotNull(router.route(request).block());
    }

    @Test
    public void givenHttpRequest_whenGetPersonByIdJsonRequest_thenReturnHandlerFunction() throws URISyntaxException {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .uri(new URI("http://localhost/person"))
                .pathVariable("id", "1")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
        assertNotNull(router.route(request).block());
    }


    @Test
    public void givenHttpRequest_whenPostPersonJsonRequest_thenReturnHandlerFunction() throws URISyntaxException {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(new URI("http://localhost/person"))
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
        assertNotNull(router.route(request).block());
    }
}
