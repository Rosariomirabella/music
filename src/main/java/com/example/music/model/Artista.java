package com.example.music.model;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Creo una classe che rappresenta la tabella 
 * artisti. La creo sfruttando la convenzione detta
 * POJO, cioè la convenzione java beans. Una classe
 * è
 */

public class Artista {
	
	private Integer artistaId;
	private String nome;
	private String nazione;
	private Integer annoInizio;
	private LocalDateTime dataInserimento;
	private LocalDateTime dataAggiornamento;
	
	public Artista() {
		
	}
	
	public Artista(Integer artistaId, String nome, String nazione, Integer annoInizio, LocalDateTime dataInserimento, LocalDateTime dataAggiornamento) {
		this.artistaId=artistaId;
		this.nome=nome;
		this.nazione=nazione;
		this.annoInizio=annoInizio;
		this.dataInserimento=dataInserimento;
		this.dataAggiornamento=dataAggiornamento;
	}

	public Integer getArtistaId() {
		return artistaId;
	}

	public void setArtistaId(Integer artistaId) {
		this.artistaId = artistaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public Integer getAnnoInizio() {
		return annoInizio;
	}

	public void setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
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

	@Override
	public int hashCode() {
		return Objects.hash(artistaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(artistaId, other.artistaId);
	}

	
	@Override
    public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nArtista {\n");
		builder.append("  ID:               ").append(artistaId).append("\n");
		builder.append("  Nome:             ").append(nome).append("\n");
		builder.append("  Nazione:          ").append(nazione).append("\n");
		builder.append("  Anno Inizio:      ").append(annoInizio).append("\n");
		builder.append("  Data Inserimento: ").append(dataInserimento).append("\n");
		builder.append("  Data Aggiornamento: ").append(dataAggiornamento != null ? dataAggiornamento : "N/D").append("\n");
		builder.append("}");
		return builder.toString();
	}
}
	
	
	

