package dao;

import database.SQLiteConnection;
import db.PlaylistDb;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DaoQueryUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestPlaylistDao {

    private PlaylistDao playlistDao;
    private PlaylistDb playlistDb;

    @Before
    public void initialize() throws Exception {

        playlistDao = new PlaylistDao();
        playlistDb = new PlaylistDb();
        playlistDb.setIdPlaylist(1);
        playlistDb.setIntitulePlaylist("playlistTest");
    }

    @Test
    public void insert() {
        playlistDao.insert(playlistDb);

        try {
            PlaylistDb resultDb = new PlaylistDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", playlistDb.getIdPlaylist()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setIdPlaylist(result.getInt("idPlaylist"));
            resultDb.setIntitulePlaylist(result.getString("intitulePlaylist"));

            assertTrue(playlistDb.getIdPlaylist().equals(resultDb.getIdPlaylist()));
            assertTrue(playlistDb.getIntitulePlaylist().equals(resultDb.getIntitulePlaylist()));

            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void update() {
        playlistDao.insert(playlistDb);
        playlistDb.setIntitulePlaylist("playlistTestUpdated");
        playlistDao.update(playlistDb);

        try {
            PlaylistDb resultDb = new PlaylistDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", playlistDb.getIdPlaylist()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setIntitulePlaylist(result.getString("intitulePlaylist"));

            assertTrue("playlistTestUpdated".equals(resultDb.getIntitulePlaylist()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {

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
            e.printStackTrace();
        }
    }

    @Test
    public void find() {
        playlistDao.insert(playlistDb);

        try {

            PlaylistDb playlist = playlistDao.find(playlistDb.getIdPlaylist());

            assertTrue(playlistDb.getIdPlaylist().equals(playlist.getIdPlaylist()));
            assertTrue(playlistDb.getIntitulePlaylist().equals(playlist.getIntitulePlaylist()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune playlist n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        PlaylistDb playlistDb2 = new PlaylistDb();
        playlistDb2.setIdPlaylist(2);
        playlistDb2.setIntitulePlaylist("playlistTest2");

        playlistDao.insert(playlistDb);
        playlistDao.insert(playlistDb2);

        try {
            List<PlaylistDb> playlistsList = playlistDao.findAll();

            assertTrue(playlistsList.size() == 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune playlist n'est présent dans la base de données.");
        }
    }

    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        statement.executeUpdate("DELETE FROM playlist");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '0' WHERE name = 'playlist'");
    }
}
