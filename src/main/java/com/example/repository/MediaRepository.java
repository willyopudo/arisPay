package com.example.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.model.Media;


public interface MediaRepository extends CrudRepository<Media, Long> {

	Optional<Media> findByName(final String name);

	boolean existsByNameIgnoreCase(String name);

}
