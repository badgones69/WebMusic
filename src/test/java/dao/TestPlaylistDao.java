package dao;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import db.PlaylistDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestPlaylistDao {

    private static final Logger LOG = LogManager.getLogger(TestPlaylistDao.class);
    private static final String SQL_EXCEPTION = "SQLException : ";

    private PlaylistDao playlistDao;
    private PlaylistDb playlistDb;
    private AlbumDao albumDao;
    private AlbumDb albumMusique;
    private AuteurDao auteurDao;
    private AuteurDb artisteMusique;
    private List<AuteurDb> artistesMusique;
    private MusiqueDao musiqueDao;
    private List<MusiqueDb> musiques;
    private MusiqueDb musique1Playlist;
    private MusiqueDb musique2Playlist;

    private Integer idAuteur;
    private Integer idPlaylist1;
    private Integer idPlaylist2;

    @Before
    public void initialize() {
        Home.initializeDB();

        playlistDao = new PlaylistDao();
        playlistDb = new PlaylistDb();
        albumDao = new AlbumDao();
        albumMusique = new AlbumDb();
        auteurDao = new AuteurDao();
        artisteMusique = new AuteurDb();
        artistesMusique = new ArrayList<>();
        musiqueDao = new MusiqueDao();
        musique1Playlist = new MusiqueDb();
        musique2Playlist = new MusiqueDb();
        musiques = new ArrayList<>();

        albumMusique.setTitreAlbum("albumMusique");

        artisteMusique.setNomAuteur("nom1Test");
        artisteMusique.setPrenomAuteur("prenom1Test");

        artistesMusique.add(artisteMusique);

        musique1Playlist.setTitreMusique("musiqueTest");
        musique1Playlist.setDureeMusique("00:03:57");
        musique1Playlist.setDateActionMusique("09/04/2018");
        musique1Playlist.setNomFichierMusique("musiqueTest.mp3");
        musique1Playlist.setAlbumMusique(albumMusique);
        musique1Playlist.setListeAuteurs(artistesMusique);

        musique2Playlist.setTitreMusique("musiqueTest2");
        musique2Playlist.setDureeMusique("00:04:21");
        musique2Playlist.setDateActionMusique("25/04/2018");
        musique2Playlist.setNomFichierMusique("musiqueTest2.mp3");
        musique2Playlist.setAlbumMusique(albumMusique);
        musique2Playlist.setListeAuteurs(artistesMusique);

        musiques.add(musique1Playlist);
        musiques.add(musique2Playlist);


        playlistDb.setIntitulePlaylist("playlistTest");
        playlistDb.setDateActionPlaylist("02/11/2018");
        playlistDb.setListeMusiques(musiques);
    }

    @Test
    public void insert() {
        auteurDao.insert(artisteMusique);
        DaoTestsUtils.setIdentifiantToAuteur(artisteMusique);
        idAuteur = artisteMusique.getIdentifiantAuteur();
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musique1Playlist);
        DaoTestsUtils.setCodeToMusique(musique1Playlist);
        musiqueDao.insert(musique2Playlist);
        DaoTestsUtils.setCodeToMusique(musique2Playlist);
        playlistDao.insert(playlistDb);
        DaoTestsUtils.setIdToPlaylist(playlistDb);
        idPlaylist1 = playlistDb.getIdPlaylist();

        try {
            PlaylistDb resultDb = new PlaylistDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", playlistDb.getIdPlaylist()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setIdPlaylist(result.getInt("idPlaylist"));
            resultDb.setIntitulePlaylist(result.getString("intitulePlaylist"));
            resultDb.setDateActionPlaylist(result.getString("dateActionPlaylist"));

            assertTrue(playlistDb.getIdPlaylist().equals(resultDb.getIdPlaylist()));
            assertTrue(playlistDb.getIntitulePlaylist().equals(resultDb.getIntitulePlaylist()));
            assertTrue(playlistDb.getDateActionPlaylist().equals(resultDb.getDateActionPlaylist()));

            result.close();
            statement.close();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

    }

    @Test
    public void update() {
        auteurDao.insert(artisteMusique);
        DaoTestsUtils.setIdentifiantToAuteur(artisteMusique);
        idAuteur = artisteMusique.getIdentifiantAuteur();
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musique1Playlist);
        DaoTestsUtils.setCodeToMusique(musique1Playlist);
        musiqueDao.insert(musique2Playlist);
        DaoTestsUtils.setCodeToMusique(musique2Playlist);
        playlistDao.insert(playlistDb);
        DaoTestsUtils.setIdToPlaylist(playlistDb);
        playlistDb.setIntitulePlaylist("playlistTestUpdated");
        playlistDb.setDateActionPlaylist("07/11/2018");
        idPlaylist1 = playlistDb.getIdPlaylist();
        playlistDao.update(playlistDb);

        try {
            PlaylistDb resultDb = new PlaylistDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", playlistDb.getIdPlaylist()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setIntitulePlaylist(result.getString("intitulePlaylist"));
            resultDb.setDateActionPlaylist(result.getString("dateActionPlaylist"));

            assertTrue("playlistTestUpdated".equals(resultDb.getIntitulePlaylist()));
            assertTrue("07/11/2018".equals(resultDb.getDateActionPlaylist()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void delete() {
        auteurDao.insert(artisteMusique);
        DaoTestsUtils.setIdentifiantToAuteur(artisteMusique);
        idAuteur = artisteMusique.getIdentifiantAuteur();
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musique1Playlist);
        DaoTestsUtils.setCodeToMusique(musique1Playlist);
        musiqueDao.insert(musique2Playlist);
        DaoTestsUtils.setCodeToMusique(musique2Playlist);
        playlistDao.insert(playlistDb);
        DaoTestsUtils.setIdToPlaylist(playlistDb);
        idPlaylist1 = playlistDb.getIdPlaylist();
        playlistDao.delete(playlistDb);

        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", playlistDb.getIdPlaylist()));
            assertTrue(statement.execute());
            ResultSet result = statement.executeQuery();
            assertTrue(result.isClosed());

            result.close();
            statement.close();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void find() {
        auteurDao.insert(artisteMusique);
        DaoTestsUtils.setIdentifiantToAuteur(artisteMusique);
        idAuteur = artisteMusique.getIdentifiantAuteur();
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musique1Playlist);
        DaoTestsUtils.setCodeToMusique(musique1Playlist);
        musiqueDao.insert(musique2Playlist);
        DaoTestsUtils.setCodeToMusique(musique2Playlist);
        playlistDao.insert(playlistDb);
        DaoTestsUtils.setIdToPlaylist(playlistDb);
        idPlaylist1 = playlistDb.getIdPlaylist();

        try {

            PlaylistDb playlist = playlistDao.find(playlistDb.getIdPlaylist());

            assertTrue(playlistDb.getIdPlaylist().equals(playlist.getIdPlaylist()));
            assertTrue(playlistDb.getIntitulePlaylist().equals(playlist.getIntitulePlaylist()));
            assertTrue(playlistDb.getDateActionPlaylist().equals(playlist.getDateActionPlaylist()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune playlist n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        PlaylistDb playlistDb2 = new PlaylistDb();
        playlistDb2.setIntitulePlaylist("playlistTest2");
        playlistDb2.setDateActionPlaylist("05/11/2018");
        playlistDb2.setListeMusiques(musiques);

        auteurDao.insert(artisteMusique);
        DaoTestsUtils.setIdentifiantToAuteur(artisteMusique);
        idAuteur = artisteMusique.getIdentifiantAuteur();
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musique1Playlist);
        DaoTestsUtils.setCodeToMusique(musique1Playlist);
        musiqueDao.insert(musique2Playlist);
        DaoTestsUtils.setCodeToMusique(musique2Playlist);
        playlistDao.insert(playlistDb);
        DaoTestsUtils.setIdToPlaylist(playlistDb);
        idPlaylist1 = playlistDb.getIdPlaylist();
        playlistDao.insert(playlistDb2);
        DaoTestsUtils.setIdToPlaylist(playlistDb2);
        idPlaylist2 = playlistDb2.getIdPlaylist();

        try {
            List<PlaylistDb> playlistsList = playlistDao.findAll();

            assertTrue(playlistsList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune playlist n'est présent dans la base de données.");
        }
    }

    // DATABASE CLEARING (TEST DATA REMOVING)
    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        statement.executeUpdate("DELETE FROM album WHERE titreAlbum = 'albumMusique'");
        statement.executeUpdate("DELETE FROM contenir WHERE idPlaylist IN (" +
                idPlaylist1 + ", " + idPlaylist2 + ")");
        statement.executeUpdate("DELETE FROM playlist WHERE intitulePlaylist IN ('playlistTest', 'playlistTest2', 'playlistTestUpdated')");
        statement.executeUpdate("DELETE FROM posseder WHERE identifiantAuteur = " + idAuteur);
        statement.executeUpdate("DELETE FROM musique WHERE titreMusique IN ('musiqueTest', 'musiqueTest2', 'musiqueTestUpdated')");
        statement.executeUpdate("DELETE FROM auteur WHERE prenomAuteur = 'prenom1Test'");

        Integer lastIdAlbum = 0;
        Integer lastIdPlaylist = 0;
        Integer lastIdMusique = 0;
        Integer lastIdAuteur = 0;

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("album"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdAlbum = sequenceResult.getInt(0);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("playlist"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdPlaylist = sequenceResult.getInt(0);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("musique"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdMusique = sequenceResult.getInt(0);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("auteur"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdAuteur = sequenceResult.getInt(0);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdAlbum + "' WHERE name = 'album'");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdPlaylist + "' WHERE name = 'playlist'");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdMusique + "' WHERE name = 'musique'");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdAuteur + "' WHERE name = 'auteur'");
        statement.close();
    }
}
