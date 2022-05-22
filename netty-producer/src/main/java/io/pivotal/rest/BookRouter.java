package io.pivotal.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouter {
    @Bean
    RouterFunction<ServerResponse> bookFunctions(BookHandler bookHandler) {
        return route(GET("/books"), bookHandler::all)
                .andRoute(GET("/books/new"), bookHandler::getNew)
                .andRoute(GET("/books/{id}"), bookHandler::get)
                .andRoute(POST("/books"), bookHandler::create);
    }
}
