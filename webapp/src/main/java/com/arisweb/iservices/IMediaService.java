package com.arisweb.iservices;

import java.util.List;

import com.arisweb.dto.MediaDTO;


public interface IMediaService {

	List<MediaDTO> findAll();

	MediaDTO get(Long id);

	MediaDTO findByName(String name);

	Long create(MediaDTO mediaDTO);

	void update(Long id, MediaDTO mediaDTO);

	void delete(Long id);

	boolean nameExists(String name);

}