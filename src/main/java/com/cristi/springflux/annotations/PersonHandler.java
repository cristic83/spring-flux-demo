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

package com.cristi.springflux.annotations;

import com.cristi.springflux.domain.Person;
import com.cristi.springflux.domain.PersonRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person")
public class PersonHandler {

	private final PersonRepository repository;

	public PersonHandler(PersonRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
	public Mono<Person> getPerson(@PathVariable("id") int personId) {
		return this.repository.getPerson(personId);
	}

	@PostMapping
	public Mono<Void> createPerson(@RequestBody Mono<Person> person) {
		return this.repository.savePerson(person);
	}

	@GetMapping(produces = {"application/json"})
	public Flux<Person> listPeople() {
		return this.repository.allPeople();
	}

}
