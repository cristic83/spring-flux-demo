package com.cristi.springflux.function.router;

import com.cristi.springflux.domain.DummyPersonRepository;
import com.cristi.springflux.domain.PersonRepository;
import com.cristi.springflux.function.PersonHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

public class Router {
    private PersonRepository repository = new DummyPersonRepository();
    private PersonHandler handler = new PersonHandler(repository);

    public Mono<HandlerFunction<ServerResponse>> route(ServerRequest request) {

        List<MediaType> mediaTypes = request.headers().accept();
        boolean jsonRequest = mediaTypes.size() == 1 && mediaTypes.get(0).equals(MediaType.APPLICATION_JSON);
        boolean personJsonRequest = request.uri().getPath().equals("/person") && jsonRequest;
        return personJsonRequest ?
                (request.method() == HttpMethod.GET ?
                        (request.pathVariables().isEmpty() ? just(handler::listPeople)
                                : request.pathVariables().containsKey("id")
                                ? just(handler::getPerson) : empty())
                        : request.method() == HttpMethod.POST ? just(handler::createPerson) : empty()
                )
                : empty();
    }
}
