package com.cristi.springflux.function.reactive;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class ReactiveExample {

    @Test
    public void pullData() {
        List<Integer> customerIds = getCustomerIds();
        customerIds.stream().filter( id -> id > 10).forEach(id -> System.out.println("Pulled customer: " + id));
    }

    private List<Integer> getCustomerIds() {
        return Arrays.asList(10, 15, 7, 9, 20);
    }

    @Test
    public void pushedData() {
        Flux<Integer> customerIds = pushCustomerIds();
        customerIds.filter(id -> id > 10).subscribe(id -> System.out.println("Pushed customer: " + id));
    }

    private Flux<Integer> pushCustomerIds() {
        return Flux.just(10, 15, 7, 9, 20);
    }

}
