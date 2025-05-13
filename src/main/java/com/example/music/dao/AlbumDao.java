package com.example.music.dao;
import com.example.music.model.Album;

import java.sql.SQLException;
import java.util.Set;

public interface AlbumDao {
	Album save(Album album) throws SQLException;
	
	Album findById(Integer albumId) throws SQLException;
	
	Album update(Integer albumId, Album album) throws SQLException;
	
	Album deleteById(Integer albumId) throws SQLException;
	
	Set<Album> findAll() throws SQLException;
} 
