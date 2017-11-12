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

import com.cristi.springflux.domain.Person;
import com.cristi.springflux.domain.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class PersonHandler {

	private final PersonRepository repository;

	public PersonHandler(PersonRepository repository) {
		this.repository = repository;
	}

	public Mono<ServerResponse> getPerson(ServerRequest request) {

		int personId = Integer.valueOf(request.pathVariable("id"));

		Mono<Person> personMono = this.repository.getPerson(personId);

		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return personMono
				.flatMap(person -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(person)))
				.switchIfEmpty(notFound);
	}


	public Mono<ServerResponse> createPerson(ServerRequest request) {

		Mono<Person> person = request.bodyToMono(Person.class);

		Mono<Void> voidPublisher = this.repository.savePerson(person);

		return ServerResponse.ok().build(voidPublisher);
	}

	public Mono<ServerResponse> listPeople(ServerRequest request) {

		Flux<Person> people = this.repository.allPeople();

		return ServerResponse.ok().contentType(APPLICATION_JSON).body(people, Person.class);
	}

}
