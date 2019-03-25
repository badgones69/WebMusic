package dao;

import database.SQLiteConnection;
import db.MusiqueDb;
import db.PlaylistDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistDao implements AbstractDao<PlaylistDb> {

    private static final Logger LOG = LogManager.getLogger(PlaylistDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private static final String PLAYLIST = "playlist";
    private static final String ID_PLAYLIST = "idPlaylist";
    private static final String INTITULE_PLAYLIST = "intitulePlaylist";
    private static final String SQL_EXCEPTION = "SQLException : ";


    @Override
    public void insert(PlaylistDb playlistDb) {
        try {
            // NEW PLAYLIST INSERTION
            try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery(PLAYLIST, playlistDb))) {
                statement.executeUpdate();
            }

            // INSERTED PLAYLIST CODE RETRIEVING
            Integer playlistIdGenerated;
            try (PreparedStatement idPlaylistStatement = CONNECTION.prepareStatement(DaoQueryUtils.findBySpecificColumn(
                    PLAYLIST, INTITULE_PLAYLIST, playlistDb.getIntitulePlaylist()))) {
                try (ResultSet result = idPlaylistStatement.executeQuery()) {
                    result.next();
                    playlistIdGenerated = result.getInt(ID_PLAYLIST);
                }
            }

            // PLAYLIST/MUSIC(S) ASSIGNMENT
            try (PreparedStatement musiquesPlaylistStatement = CONNECTION.prepareStatement("INSERT INTO contenir VALUES (?, ?)")) {

                for (MusiqueDb musique : playlistDb.getListeMusiques()) {
                    musiquesPlaylistStatement.setInt(1, musique.getCodeMusique());
                    musiquesPlaylistStatement.setInt(2, playlistIdGenerated);
                    musiquesPlaylistStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void update(PlaylistDb playlistDb) {
        try {
            try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery(PLAYLIST, playlistDb))) {
                statement.executeUpdate();
            }

            // PLAYLIST/MUSIC(S) ASSIGNMENT
            try (Statement deletingArtistesMusiqueStatement = SQLiteConnection.getInstance().createStatement()) {
                deletingArtistesMusiqueStatement.execute("DELETE FROM contenir WHERE idPlaylist = " + playlistDb.getIdPlaylist());
            }

            try (PreparedStatement insertingMusiquesPlaylistStatement = CONNECTION.prepareStatement("INSERT INTO contenir VALUES (?, ?)")) {

                for (MusiqueDb musique : playlistDb.getListeMusiques()) {
                    insertingMusiquesPlaylistStatement.setInt(1, musique.getCodeMusique());
                    insertingMusiquesPlaylistStatement.setInt(2, playlistDb.getIdPlaylist());
                    insertingMusiquesPlaylistStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void delete(PlaylistDb playlistDb) {
        try {
            PlaylistDao playlistDao = new PlaylistDao();

            try (PreparedStatement musiquesPlaylistStatement = CONNECTION.prepareStatement("DELETE FROM contenir WHERE idPlaylist=" + playlistDb.getIdPlaylist())) {
                musiquesPlaylistStatement.executeUpdate();
            }

            try (PreparedStatement playlistStatement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery(PLAYLIST, playlistDb.getIdPlaylist()))) {
                playlistStatement.executeUpdate();
            }

            Integer nbPlaylists = playlistDao.findAll().size();
            try (Statement statement = SQLiteConnection.getInstance().createStatement()) {
                statement.execute("UPDATE sqlite_sequence SET seq = '" + nbPlaylists + "' WHERE name = 'playlist'");
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public PlaylistDb find(int id) {
        PlaylistDb playlistDb = new PlaylistDb();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                PLAYLIST, id))) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                playlistDb.setIdPlaylist(result.getInt(ID_PLAYLIST));
                playlistDb.setIntitulePlaylist(result.getString(INTITULE_PLAYLIST));
                playlistDb.setDateActionPlaylist(result.getString("dateActionPlaylist"));
            }

            // PLAYLIST MUSIC(S) RETRIEVING
            List<MusiqueDb> musiques;
            try (PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                    "contenir", ID_PLAYLIST, playlistDb.getIdPlaylist()))) {
                try (ResultSet musiquesResult = artistesStatement.executeQuery()) {
                    MusiqueDao musiqueDao = new MusiqueDao();
                    musiques = new ArrayList<>();

                    while (musiquesResult.next()) {
                        MusiqueDb musique = musiqueDao.find(musiquesResult.getInt("codeMusique"));
                        musiques.add(musique);
                    }
                }
            }

            playlistDb.setListeMusiques(musiques);

            return playlistDb;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<PlaylistDb> findAll() {
        List<PlaylistDb> playlistsList = new ArrayList<>();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                PLAYLIST))) {
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    PlaylistDb playlistDb = new PlaylistDb();
                    List<MusiqueDb> musiques = new ArrayList<>();

                    playlistDb.setIdPlaylist(result.getInt(ID_PLAYLIST));
                    playlistDb.setIntitulePlaylist(result.getString(INTITULE_PLAYLIST));
                    playlistDb.setDateActionPlaylist(result.getString("dateActionPlaylist"));

                    // MUSIC ARTIST(S) RETRIEVING
                    try (PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                            "contenir", ID_PLAYLIST, playlistDb.getIdPlaylist()))) {
                        try (ResultSet musiquesResult = artistesStatement.executeQuery()) {
                            MusiqueDao musiqueDao = new MusiqueDao();

                            while (musiquesResult.next()) {
                                MusiqueDb musique = musiqueDao.find(musiquesResult.getInt("codeMusique"));
                                musiques.add(musique);
                            }
                        }
                    }
                    playlistDb.setListeMusiques(musiques);
                    playlistsList.add(playlistDb);
                }
            }
            return playlistsList;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
