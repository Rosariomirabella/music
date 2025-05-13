package com.example.music.dao;
import com.example.music.model.Artista;

import java.sql.SQLException;
import java.util.Set;

public interface ArtistaDao {
	Artista save(Artista artista) throws SQLException;
	
	Artista findById(Integer artistaId) throws SQLException;
	
	Artista update(Integer artistaId, Artista artista) throws SQLException;
	
	Artista deleteById(Integer artistaId) throws SQLException;
	
	Set<Artista> findAll() throws SQLException;

	void deleteAll() throws SQLException;
	
} 
