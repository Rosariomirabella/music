package com.example.music.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Creo una classe che rappresenta la tabella 
 * Album. La creo sfruttando la convenzione detta
 * POJO, cioè la convenzione java beans. Una classe
 * è
 */

public class Album {
	private Integer albumId;
	private String titolo;
	private LocalDate dataUscita;
	private String genere;
	private LocalDateTime dataInserimento;
	private LocalDateTime dataAggiornamento;
	
	private Artista artista; //ogni album ha il suo artista di riferimento, manteniamo un reference
	
	public Album() {
		
	}

	public Album(Integer album, String titolo, LocalDate dataUscita, String genere, LocalDateTime dataInserimento, LocalDateTime dataAggiornamento, Artista artista) {
		super();
		this.albumId = album;
		this.titolo = titolo;
		this.dataUscita = dataUscita;
		this.genere = genere;
		this.dataInserimento = dataInserimento;
		this.dataAggiornamento = dataAggiornamento;
		this.artista = artista;
	}

	public Integer getAlbum() {
		return albumId;
	}

	public void setAlbum(Integer albumId) {
		this.albumId = albumId;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public LocalDate getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(LocalDate dataUscita) {
		this.dataUscita = dataUscita;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public LocalDateTime getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDateTime dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public LocalDateTime getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	@Override
	public int hashCode() {
		return Objects.hash(albumId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		return Objects.equals(albumId, other.albumId);
	}

	/*
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nAlbum {\n");
		builder.append("  ID:                ").append(albumId).append("\n");
		builder.append("  Titolo:            ").append(titolo).append("\n");
		builder.append("  Data Uscita:       ").append(dataUscita).append("\n");
		builder.append("  Genere:            ").append(genere).append("\n");
		builder.append("  Data Inserimento:  ").append(dataInserimento).append("\n");
		builder.append("  Data Aggiornamento: ").append(dataAggiornamento != null ? dataAggiornamento : "N/D").append("\n");
		builder.append("  Artista:           ").append(artista != null ? artista.toString() : "N/D").append("\n");
		builder.append("}");
		return builder.toString();
	}
	*/

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nAlbum {\n");
		builder.append("  ID:                ").append(albumId).append("\n");
		builder.append("  Titolo:            ").append(titolo).append("\n");
		builder.append("  Data Uscita:       ").append(dataUscita).append("\n");
		builder.append("  Genere:            ").append(genere).append("\n");
		builder.append("  Data Inserimento:  ").append(dataInserimento).append("\n");
		builder.append("  Data Aggiornamento: ").append(dataAggiornamento != null ? dataAggiornamento : "N/D").append("\n");
		builder.append("  Artista:           ")
			.append(artista != null ? artista.getNome() + " (ID: " + artista.getArtistaId() + ")" : "N/D")
			.append("\n");
		builder.append("}");
		return builder.toString();
	}

}