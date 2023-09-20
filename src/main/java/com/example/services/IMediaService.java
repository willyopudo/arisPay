package com.example.services;

import java.util.List;

import com.example.dto.MediaDTO;


public interface IMediaService {

	List<MediaDTO> findAll();

	MediaDTO get(Long id);

	MediaDTO findByName(String name);

	Long create(MediaDTO mediaDTO);

	void update(Long id, MediaDTO mdiaDTO);

	void delete(Long id);

	boolean nameExists(String name);

}