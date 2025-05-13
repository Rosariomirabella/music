package com.example.music.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.example.music.model.Artista;
import com.example.music.config.MySQLConnection;

/**
 * Implementazione dell'interfaccia ArtistaDao per l'accesso ai dati degli artisti.
 * 
 * Fornisco metodi per eseguire operazioni CRUD (Create, Read, Update, Delete)
 * sugli oggetti Artista nel database MySQL.
 *
 * Utilizzo la classe MySQLConnection per ottenere connessioni al database
 * e gestisco le eccezioni SQLException in caso di errori durante l'accesso ai dati.
 *
 * @see ArtistaDao
 * @see MySQLConnection
 */

public class ArtistaDaoImpl implements ArtistaDao {
    private static final Logger log = Logger.getLogger(ArtistaDaoImpl.class.getName());

    /**
     * Salvo un nuovo artista nel database.
     *
     * @param artista L'oggetto Artista da salvare.
     * @return L'oggetto Artista salvato, con l'ID generato dal database.
     * @throws SQLException Se si verifica un errore durante l'operazione di salvataggio.
     */

    @Override
    public Artista save(Artista artista) throws SQLException {
        Artista inserito = null;

        String query = "INSERT INTO artisti (nome, nazione, anno_inizio, data_inserimento, data_aggiornamento) VALUES (?, ?, ?, ?, ?)";

        // try with resource
        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, artista.getNome());
            stmt.setString(2, artista.getNazione());
            stmt.setInt(3, artista.getAnnoInizio());
            stmt.setTimestamp(4, Timestamp.valueOf(artista.getDataInserimento()));
            stmt.setTimestamp(5, null);

            int righeCoinvolte = stmt.executeUpdate();

            Integer id_artista = -1;
            if (righeCoinvolte > 0) {
                try (ResultSet chiavi = stmt.getGeneratedKeys();) {
                    if (chiavi.next()) {
                        id_artista = chiavi.getInt(1);

                        inserito = new Artista();
                        inserito.setArtistaId(id_artista);
                        inserito.setNome(artista.getNome());
                        inserito.setNazione(artista.getNazione());
                        inserito.setAnnoInizio(artista.getAnnoInizio());
                        inserito.setDataInserimento(artista.getDataInserimento());

                        log.info("Creato l'artista con il seguente ID: " + id_artista);
                    } else {
                        log.warning("Non è stato possibile recuperare la chiave primaria");
                    }
                }
            } else {
                log.severe("Non è stato possibile inserire l'artista");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inserito;
    }

    /**
     * Trovo un artista nel database tramite il suo ID.
     *
     * @param id_artista L'ID dell'artista da trovare.
     * @return L'oggetto Artista corrispondente all'ID specificato, o null se non trovato.
     * @throws SQLException Se si verifica un errore durante l'operazione di ricerca.
     */
    @Override
    public Artista findById(Integer id_artista) throws SQLException {
        Artista trovato = null;

        String query = "SELECT nome, nazione, anno_inizio, data_inserimento, data_aggiornamento FROM artisti WHERE artista_id = ?";
        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = stmt_findById(conn, query, id_artista);
             ResultSet result = stmt.executeQuery()) {
            if (result.next()) {
                trovato = new Artista();
                trovato.setArtistaId(id_artista);
                trovato.setNome(result.getString(1));
                trovato.setNazione(result.getString(2));
                trovato.setAnnoInizio(result.getInt(3));
                trovato.setDataInserimento(result.getTimestamp(4).toLocalDateTime());
                Timestamp timestamp = result.getTimestamp(5);
                if (timestamp != null)
                    trovato.setDataAggiornamento(timestamp.toLocalDateTime());

                log.info("Trovato l'artista con il seguente ID: " + id_artista);
            } else {
                log.warning("Non è stato possibile trovare l'artista");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trovato;
    }

    /**
     * Metodo di supporto per creare un PreparedStatement per la ricerca di un artista tramite ID.
     *
     * @param conn La connessione al database.
     * @param query La query SQL da eseguire.
     * @param id_artista L'ID dell'artista da cercare.
     * @return Il PreparedStatement configurato.
     * @throws SQLException Se si verifica un errore durante la creazione del PreparedStatement.
     */
    private PreparedStatement stmt_findById(Connection conn, String query, Integer id_artista) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id_artista);
        return stmt;
    }

    /**
     * Aggiorno un artista esistente nel database.
     *
     * @param id_artista L'ID dell'artista da aggiornare.
     * @param artista L'oggetto Artista con i nuovi dati.
     * @return L'oggetto Artista aggiornato.
     * @throws SQLException Se si verifica un errore durante l'operazione di aggiornamento.
     */
    @Override
    public Artista update(Integer id_artista, Artista artista) throws SQLException {
        Artista aggiornato = null;
        String query = "UPDATE artisti SET nome = ?, nazione = ?, anno_inizio = ?, data_inserimento = ?, data_aggiornamento = ? WHERE artista_id = ?";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, artista.getNome());
            stmt.setString(2, artista.getNazione());
            stmt.setInt(3, artista.getAnnoInizio());
            stmt.setTimestamp(4, Timestamp.valueOf(artista.getDataInserimento()));
            if (artista.getDataAggiornamento() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(artista.getDataAggiornamento()));
            } else {
                stmt.setTimestamp(5, null);
            }
            stmt.setInt(6, id_artista);

            int righeCoinvolte = stmt.executeUpdate();
            if (righeCoinvolte > 0) {
                aggiornato = findById(id_artista);
                log.info("Aggiornato l'artista con il seguente ID: " + id_artista);
            } else {
                log.warning("Non è stato possibile aggiornare l'artista");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aggiornato;
    }

    /**
     * Cancello un artista dal database tramite il suo ID.
     *
     * @param id_artista L'ID dell'artista da cancellare.
     * @return L'oggetto Artista cancellato.
     * @throws SQLException Se si verifica un errore durante l'operazione di cancellazione.
     */
    @Override
    public Artista deleteById(Integer id_artista) throws SQLException {
        Artista rimosso = null;
        String query1 = "SELECT nome, nazione, anno_inizio, data_inserimento, data_aggiornamento FROM artisti WHERE artista_id = ?";
        String query2 = "DELETE FROM artisti WHERE artista_id = ?";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt1 = conn.prepareStatement(query1);
             PreparedStatement stmt2 = conn.prepareStatement(query2)) {

            stmt1.setInt(1, id_artista);
            ResultSet result = stmt1.executeQuery();

            if (result.next()) {
                rimosso = new Artista();
                rimosso.setArtistaId(id_artista);
                rimosso.setNome(result.getString(1));
                rimosso.setNazione(result.getString(2));
                rimosso.setAnnoInizio(result.getInt(3));

                Timestamp timestampInserimento = result.getTimestamp(4);
                if (timestampInserimento != null) {
                    rimosso.setDataInserimento(timestampInserimento.toLocalDateTime());
                }

                Timestamp timestampAggiornamento = result.getTimestamp(5);
                if (timestampAggiornamento != null) {
                    rimosso.setDataAggiornamento(timestampAggiornamento.toLocalDateTime());
                }

                stmt2.setInt(1, id_artista);
                stmt2.executeUpdate();
                log.info("Rimosso l'artista con ID: " + id_artista);
            } else {
                log.warning("Non è stato possibile rimuovere l'artista con l'ID: " + id_artista);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rimosso;
    }

    /**
     * Trovo tutti gli artisti nel database.
     *
     * @return Un insieme (Set) di oggetti Artista presenti nel database.
     * @throws SQLException Se si verifica un errore durante l'operazione di ricerca.
     */
    @Override
    public Set<Artista> findAll() throws SQLException {
        Set<Artista> artisti = new LinkedHashSet<Artista>();
        String query = "SELECT * FROM artisti ORDER BY anno_inizio";
        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet result = stmt.executeQuery()) {
            while (result.next()) {
                Artista artista = new Artista();
                artista.setArtistaId(result.getInt(1));
                artista.setNome(result.getString(2));
                artista.setNazione(result.getString(3));
                artista.setAnnoInizio(result.getInt(4));
                artista.setDataInserimento(result.getTimestamp(5).toLocalDateTime());
                Timestamp timestamp = result.getTimestamp(6);
                if (timestamp != null)
                    artista.setDataAggiornamento(result.getTimestamp(6).toLocalDateTime());
                artisti.add(artista);
            }
            log.info("Sono stati trovati " + artisti.size() + " artisti");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artisti;
    }

    /**
     * Elimina tutti gli artisti dal database.
     *
     * @throws SQLException Se si verifica un errore durante l'operazione di eliminazione.
     */
    @Override
    public void deleteAll() throws SQLException {
        String query = "DELETE FROM artisti";
        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            int rowsAffected = stmt.executeUpdate();
            log.info("Sono stati eliminati " + rowsAffected + " artisti");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}