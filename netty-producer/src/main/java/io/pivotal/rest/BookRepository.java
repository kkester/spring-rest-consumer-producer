package io.pivotal.rest;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface BookRepository extends ReactiveCrudRepository<BookEntity, Long> {
}
