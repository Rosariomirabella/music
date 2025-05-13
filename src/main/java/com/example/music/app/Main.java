package com.example.music.app;

import com.example.music.dao.ArtistaDaoImpl;
import com.example.music.dao.ArtistaDao;
import com.example.music.model.Album;
import com.example.music.model.Artista;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;
import java.sql.SQLException;
import com.example.music.dao.AlbumDao;
import com.example.music.dao.AlbumDaoImpl;


public class Main {
    public static void main(String[] args) {
        ArtistaDao artistaDao = new ArtistaDaoImpl();
        AlbumDao albumDao = new AlbumDaoImpl();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU PRINCIPALE ===");
            System.out.println("1. Gestisci Artisti");
            System.out.println("2. Gestisci Album");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    menuArtisti(scanner, artistaDao);
                    break;
                case "2":
                    menuAlbum(scanner, albumDao, artistaDao);
                    break;
                case "0":
                    running = false;
                    System.out.println("Uscita dal programma...");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }

        scanner.close();
    }

    private static void menuArtisti(Scanner scanner, ArtistaDao artistaDao) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== MENU ARTISTI ===");
            System.out.println("1. Aggiungi artista");
            System.out.println("2. Visualizza tutti gli artisti");
            System.out.println("3. Elimina tutti gli artisti");
            System.out.println("4. Trova artista per ID");
            System.out.println("5. Aggiorna artista per ID");
            System.out.println("6. Elimina un artista per ID");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");

            switch (scanner.nextLine()) {
                case "1":
                    aggiungiArtista(scanner, artistaDao);
                    break;
                case "2":
                    visualizzaArtisti(artistaDao);
                    break;
                case "3":
                    eliminaTuttiGliArtisti(artistaDao);
                    break;
                case "4":
                    trovaArtistaPerId(scanner, artistaDao);
                    break;
                case "5":
                    aggiornaArtistaPerId(scanner, artistaDao);
                    break;
                case "6":
                    eliminaArtistaPerId(scanner, artistaDao);
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private static void menuAlbum(Scanner scanner, AlbumDao albumDao, ArtistaDao artistaDao) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== MENU ALBUM ===");
            System.out.println("1. Aggiungi album");
            System.out.println("2. Visualizza tutti gli album");
            System.out.println("3. Trova album per ID");
            System.out.println("4. Aggiorna album per ID");
            System.out.println("5. Elimina album per ID");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");

            switch (scanner.nextLine()) {
                case "1":
                    aggiungiAlbum(scanner, albumDao, artistaDao);
                    break;
                case "2":
                    visualizzaAlbum(albumDao);
                    break;
                case "3":
                    trovaAlbumPerId(scanner, albumDao);
                    break;
                case "4":
                    aggiornaAlbum(scanner, albumDao, artistaDao);
                    break;
                case "5":
                    eliminaAlbum(scanner, albumDao);
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    // --- ARTISTI ---

    private static void aggiungiArtista(Scanner scanner, ArtistaDao artistaDao) {
        try {
            System.out.print("Nome artista: ");
            String nome = scanner.nextLine();
            System.out.print("Nazione: ");
            String nazione = scanner.nextLine();
            System.out.print("Anno inizio: ");
            int anno = Integer.parseInt(scanner.nextLine());

            Artista artista = new Artista();
            artista.setNome(nome);
            artista.setNazione(nazione);
            artista.setAnnoInizio(anno);
            artista.setDataInserimento(LocalDateTime.now());

            artista = artistaDao.save(artista);
            System.out.println("Artista aggiunto:\n" + artista);
        } catch (Exception e) {
            System.err.println("Errore durante l'inserimento dell'artista:");
            e.printStackTrace();
        }
    }

    private static void visualizzaArtisti(ArtistaDao artistaDao) {
        try {
            Set<Artista> artisti = artistaDao.findAll();
            artisti.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Errore durante la visualizzazione degli artisti:");
            e.printStackTrace();
        }
    }

    private static void eliminaTuttiGliArtisti(ArtistaDao artistaDao) {
        try {
            artistaDao.deleteAll();
            System.out.println("Tutti gli artisti eliminati.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione degli artisti:");
            e.printStackTrace();
        }
    }

    private static void trovaArtistaPerId(Scanner scanner, ArtistaDao artistaDao) {
        try {
            System.out.print("ID artista: ");
            int id = Integer.parseInt(scanner.nextLine());
            Artista artista = artistaDao.findById(id);
            //System.out.println(artista != null ? artista : "Artista non trovato.");
        } catch (SQLException e) {
            System.err.println("Errore durante la ricerca dell'artista:");
            e.printStackTrace();
        }
    }

    private static void aggiornaArtistaPerId(Scanner scanner, ArtistaDao artistaDao) {
        try {
            System.out.print("ID artista: ");
            int id = Integer.parseInt(scanner.nextLine());
            Artista artista = artistaDao.findById(id);
            if (artista != null) {
                System.out.print("Nuovo nome: ");
                artista.setNome(scanner.nextLine());
                System.out.print("Nuova nazione: ");
                artista.setNazione(scanner.nextLine());
                System.out.print("Nuovo anno di inizio: ");
                artista.setAnnoInizio(Integer.parseInt(scanner.nextLine()));
                artista.setDataAggiornamento(LocalDateTime.now());
                artista = artistaDao.update(id, artista);
                System.out.println("Artista aggiornato:\n" + artista);
            } else {
                System.out.println("Artista non trovato.");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento dell'artista:");
            e.printStackTrace();
        }
    }

    private static void eliminaArtistaPerId(Scanner scanner, ArtistaDao artistaDao) {
        try {
            System.out.print("ID artista: ");
            int id = Integer.parseInt(scanner.nextLine());
            Artista artista = artistaDao.deleteById(id);
            System.out.println(artista != null ? "Artista eliminato:\n" + artista : "Artista non trovato.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione dell'artista:");
            e.printStackTrace();
        }
    }

    // --- ALBUM ---

    private static void aggiungiAlbum(Scanner scanner, AlbumDao albumDao, ArtistaDao artistaDao) {
        try {
            System.out.print("Titolo album: ");
            String titolo = scanner.nextLine();
            System.out.print("Data uscita (YYYY-MM-DD): ");
            LocalDate dataUscita = LocalDate.parse(scanner.nextLine());
            System.out.print("Genere: ");
            String genere = scanner.nextLine();
            System.out.print("ID artista: ");
            int artistaId = Integer.parseInt(scanner.nextLine());

            Artista artista = artistaDao.findById(artistaId);
            if (artista == null) {
                System.out.println("Artista non trovato.");
                return;
            }

            Album album = new Album();
            album.setTitolo(titolo);
            album.setDataUscita(dataUscita);
            album.setGenere(genere);
            album.setArtista(artista);
            album.setDataInserimento(LocalDateTime.now());

            album = albumDao.save(album);
            System.out.println("Album aggiunto:\n" + album);
        } catch (Exception e) {
            System.err.println("Errore durante l'inserimento dell'album:");
            e.printStackTrace();
        }
    }

    private static void visualizzaAlbum(AlbumDao albumDao) {
        try {
            Set<Album> albumSet = albumDao.findAll();
            albumSet.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Errore durante la visualizzazione degli album:");
            e.printStackTrace();
        }
    }

    private static void trovaAlbumPerId(Scanner scanner, AlbumDao albumDao) {
        try {
            System.out.print("ID album: ");
            int id = Integer.parseInt(scanner.nextLine());
            Album album = albumDao.findById(id);
            //System.out.println(album != null ? album : "Album non trovato.");
        } catch (SQLException e) {
            System.err.println("Errore durante la ricerca dell'album:");
            e.printStackTrace();
        }
    }

    private static void aggiornaAlbum(Scanner scanner, AlbumDao albumDao, ArtistaDao artistaDao) {
        try {
            System.out.print("ID album: ");
            int id = Integer.parseInt(scanner.nextLine());

            Album album = albumDao.findById(id);
            if (album != null) {
                System.out.print("Nuovo titolo: ");
                album.setTitolo(scanner.nextLine());
                System.out.print("Nuova data uscita (YYYY-MM-DD): ");
                album.setDataUscita(LocalDate.parse(scanner.nextLine()));
                System.out.print("Nuovo genere: ");
                album.setGenere(scanner.nextLine());
                System.out.print("Nuovo ID artista: ");
                int idArtista = Integer.parseInt(scanner.nextLine());
                Artista artista = artistaDao.findById(idArtista);
                album.setArtista(artista);
                album.setDataAggiornamento(LocalDateTime.now());

                Album aggiornato = albumDao.update(id, album);
                System.out.println("Album aggiornato:\n" + aggiornato);
            } else {
                System.out.println("Album non trovato.");
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dell'album:");
            e.printStackTrace();
        }
    }

    private static void eliminaAlbum(Scanner scanner, AlbumDao albumDao) {
        try {
            System.out.print("ID album: ");
            int id = Integer.parseInt(scanner.nextLine());
            Album album = albumDao.deleteById(id);
            System.out.println(album != null ? "Album eliminato:\n" + album : "Album non trovato.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione dell'album:");
            e.printStackTrace();
        }
    }
}
