package com.example.music.dao;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.example.music.model.Album;
import com.example.music.model.Artista;
import com.example.music.config.MySQLConnection;

/**
 * Implementazione dell'interfaccia AlbumDao per l'accesso ai dati degli album.
 * 
 * Fornisco metodi per eseguire operazioni CRUD (Create, Read, Update, Delete)
 * sugli oggetti Album nel database MySQL.
 *
 * Utilizzo la classe MySQLConnection per ottenere connessioni al database
 * e gestisco le eccezioni SQLException in caso di errori durante l'accesso ai dati.
 *
 * @see AlbumDao
 * @see MySQLConnection
 */
public class AlbumDaoImpl implements AlbumDao{
    private static final Logger log = Logger.getLogger(ArtistaDaoImpl.class.getName());

    /**
     * Salvo un nuovo album nel database.
     *
     * @param album L'oggetto Album da salvare.
     * @return L'oggetto Album salvato, con l'ID generato dal database.
     * @throws SQLException Se si verifica un errore durante l'operazione di salvataggio.
     */
    @Override
    public Album save(Album album) throws SQLException {
        Album inserito = null;
        
        String query1 = "SELECT artista_id FROM artisti WHERE artista_id = ?";
        
        String query2 = "INSERT INTO album (titolo, data_uscita, genere, artista_id, data_inserimento, data_aggiornamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt1 = conn.prepareStatement(query1);
             PreparedStatement stmt2 = conn.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setInt(1, album.getArtista().getArtistaId());

            try (ResultSet rs = stmt1.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Artista con ID " + album.getArtista().getArtistaId() + " non trovato.");
                }
            } catch (SQLException e) {
                log.severe("Errore durante la verifica dell'artista per l'inserimento dell'album: " + e.getMessage());
                e.printStackTrace();
            }

            stmt2.setString(1, album.getTitolo());
            stmt2.setDate(2, java.sql.Date.valueOf(album.getDataUscita()));
            stmt2.setString(3, album.getGenere());
            stmt2.setInt(4, album.getArtista().getArtistaId());
            stmt2.setTimestamp(5, Timestamp.valueOf(album.getDataInserimento()));
            stmt2.setTimestamp(6, null);

            int righeCoinvolte = stmt2.executeUpdate();

            if (righeCoinvolte > 0) {
                try (ResultSet chiavi = stmt2.getGeneratedKeys()) {
                    if (chiavi.next()) {
                        int id_album = chiavi.getInt(1);
                        
                        inserito = new Album();
                        
                        inserito.setAlbum(id_album);
                        inserito.setTitolo(album.getTitolo());
                        inserito.setGenere(album.getGenere());
                        inserito.setDataUscita(album.getDataUscita());
                        inserito.setArtista(album.getArtista());
                        inserito.setDataInserimento(album.getDataInserimento());


                        log.info("Album creato con successo. ID: " + id_album);
                    }
                } catch (SQLException e) {
                    log.severe("Errore durante l'estrazione della chiave primaria per l'album: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                log.warning("Nessuna riga inserita durante il tentativo di creazione dell'album.");
            }
        }
        return inserito;
    }

    /**
     * Trovo un album nel database tramite il suo ID.
     *
     * @param album_id L'ID dell'album da trovare.
     * @return L'oggetto Album corrispondente all'ID specificato, o null se non trovato.
     * @throws SQLException Se si verifica un errore durante l'operazione di ricerca.
     */
    @Override
    public Album findById(Integer album_id) throws SQLException {
        Album trovato = null;
        Artista artista = null;

        String query = 
                "SELECT a.album_id, a.titolo, a.data_uscita, a.genere, a.data_inserimento, a.data_aggiornamento, " +
                "ar.artista_id, ar.nome, ar.nazione, ar.anno_inizio, ar.data_inserimento AS artista_data_inserimento " +
                "FROM album a " +
                "JOIN artisti ar ON a.artista_id = ar.artista_id " +
                "WHERE a.album_id = ?";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, album_id);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {

                    trovato = new Album();

                    trovato.setAlbum(result.getInt("album_id"));
                    trovato.setTitolo(result.getString("titolo"));
                    trovato.setDataUscita(result.getDate("data_uscita").toLocalDate());
                    trovato.setGenere(result.getString("genere"));
                    trovato.setDataInserimento(result.getTimestamp("data_inserimento").toLocalDateTime());

                    Timestamp timestamp = result.getTimestamp("data_aggiornamento");
                    if (timestamp != null) {
                        trovato.setDataAggiornamento(timestamp.toLocalDateTime());
                    }

                    artista = new Artista();

                    artista.setArtistaId(result.getInt("artista_id"));
                    artista.setNome(result.getString("nome"));
                    artista.setNazione(result.getString("nazione"));
                    artista.setAnnoInizio(result.getInt("anno_inizio"));
                    artista.setDataInserimento(result.getTimestamp("artista_data_inserimento").toLocalDateTime());

                    trovato.setArtista(artista);

                    log.info("Album trovato con successo. ID: " + album_id + " - " + trovato.getTitolo() + " - Artista: " + artista.getNome());

                } else {
                    log.warning("Nessun album trovato con ID: " + album_id);
                }

            } catch (SQLException e) {
                log.severe("Errore durante l'esecuzione della query per trovare l'album: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            log.severe("Errore durante la connessione o l'esecuzione della query per trovare l'album con ID: " + album_id + " - " + e.getMessage());
            e.printStackTrace();
        }

        return trovato;
    }

    /**
     * Aggiorno un album esistente nel database.
     *
     * @param album_id L'ID dell'album da aggiornare.
     * @param album L'oggetto Album con i nuovi dati.
     * @return L'oggetto Album aggiornato.
     * @throws SQLException Se si verifica un errore durante l'operazione di aggiornamento.
     */
    @Override
    public Album update(Integer album_id, Album album) throws SQLException {
        Album aggiornato = null;
        String query2 = "UPDATE album SET titolo = ?, data_uscita = ?, genere = ?, artista_id = ?, data_inserimento = ?, data_aggiornamento = ? WHERE album_id = ?";
        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt2 = conn.prepareStatement(query2)) {

            stmt2.setString(1, album.getTitolo());
            stmt2.setDate(2, java.sql.Date.valueOf(album.getDataUscita()));
            stmt2.setString(3, album.getGenere());
            stmt2.setInt(4, album.getArtista().getArtistaId());
            stmt2.setTimestamp(5, Timestamp.valueOf(album.getDataInserimento()));

            // Controllo se la data di aggiornamento è null prima di settarla
            if (album.getDataAggiornamento() != null) {
                stmt2.setTimestamp(6, Timestamp.valueOf(album.getDataAggiornamento()));
            } else {
                stmt2.setTimestamp(6, null);
            }

            stmt2.setInt(7, album_id);

            int righeCoinvolte = stmt2.executeUpdate();

            if (righeCoinvolte > 0) {
                aggiornato = new Album();
                aggiornato.setAlbum(album_id);
                aggiornato.setTitolo(album.getTitolo());
                aggiornato.setGenere(album.getGenere());
                aggiornato.setDataUscita(album.getDataUscita());
                aggiornato.setDataInserimento(album.getDataInserimento());
                aggiornato.setDataAggiornamento(album.getDataAggiornamento());
                aggiornato.setArtista(album.getArtista());

                log.info("Album con ID " + album_id + " aggiornato con successo.");
            } else {
                log.warning("Impossibile aggiornare l'album con ID: " + album_id);
            }
        } catch (SQLException e) {
            log.severe("Errore durante l'aggiornamento dell'album: " + e.getMessage());
            e.printStackTrace();
        }

        return aggiornato;
    }

    /**
     * Cancello un album dal database tramite il suo ID.
     *
     * @param album_id L'ID dell'album da cancellare.
     * @return L'oggetto Album cancellato.
     * @throws SQLException Se si verifica un errore durante l'operazione di cancellazione.
     */
    @Override
    public Album deleteById(Integer album_id) throws SQLException {
        Album rimosso = findById(album_id);
        String query = "DELETE FROM album WHERE album_id = ?";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, album_id);
            int righeCoinvolte = stmt.executeUpdate();

            if (righeCoinvolte > 0) {
                log.info("Album con ID " + album_id + " rimosso con successo.");
            } else {
                log.warning("Impossibile rimuovere l'album con ID: " + album_id);
            }
        } catch (SQLException e) {
            log.severe("Errore durante la rimozione dell'album con ID " + album_id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return rimosso;
    }

    /**
     * Trovo tutti gli album nel database.
     *
     * @return Un insieme (Set) di oggetti Album presenti nel database.
     * @throws SQLException Se si verifica un errore durante l'operazione di ricerca.
     */
    @Override
    public Set<Album> findAll() throws SQLException {
        Set<Album> albumList = new LinkedHashSet<>();
        
        String query = "SELECT a.album_id, a.titolo, a.data_uscita, a.genere, a.data_inserimento, a.data_aggiornamento, " +
                "ar.artista_id, ar.nome, ar.nazione, ar.anno_inizio, ar.data_inserimento AS artista_data_inserimento " +
                "FROM album a JOIN artisti ar ON a.artista_id = ar.artista_id ORDER BY a.data_uscita";

        try (Connection conn = MySQLConnection.open();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                Album album = new Album();
                album.setAlbum(result.getInt("album_id"));
                album.setTitolo(result.getString("titolo"));
                album.setDataUscita(result.getDate("data_uscita").toLocalDate());
                album.setGenere(result.getString("genere"));
                album.setDataInserimento(result.getTimestamp("data_inserimento").toLocalDateTime());

                Timestamp timestamp = result.getTimestamp("data_aggiornamento");
                if (timestamp != null) {
                    album.setDataAggiornamento(timestamp.toLocalDateTime());
                }

                Artista artista = new Artista();
                artista.setArtistaId(result.getInt("artista_id"));
                artista.setNome(result.getString("nome"));
                artista.setNazione(result.getString("nazione"));
                artista.setAnnoInizio(result.getInt("anno_inizio"));
                artista.setDataInserimento(result.getTimestamp("data_inserimento").toLocalDateTime());

                album.setArtista(artista);
                albumList.add(album);
            }

            log.info("Sono stati trovati " + albumList.size() + " album.");

        } catch (SQLException e) {

            log.severe("Errore durante il recupero degli album: " + e.getMessage());
            e.printStackTrace();
        }

        return albumList;
    }
}