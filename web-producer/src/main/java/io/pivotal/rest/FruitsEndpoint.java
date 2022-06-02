package io.pivotal.rest;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Endpoint(id = "fruits")
public class FruitsEndpoint {

    private final List<Fruit> availableFruits = new ArrayList<>();

    @ReadOperation
    public List<Fruit> fruits() {
        return availableFruits;
    }

    @WriteOperation
    public void addFruits(@Selector String name, boolean enabled) {
        availableFruits.add(Fruit.builder().name(name).enabled(enabled).build());
    }
}
