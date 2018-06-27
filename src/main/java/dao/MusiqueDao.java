package dao;

import database.SQLiteConnection;
import db.AuteurDb;
import db.MusiqueDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;

import javax.transaction.TransactionalException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusiqueDao extends AbstractDao<MusiqueDb> {

    private static final Logger LOG = LogManager.getLogger(MusiqueDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private AlbumDao albumDao = new AlbumDao();

    @Override
    public void insert(MusiqueDb musiqueDb) {
        try {
            // NEW MUSIC INSERTION
            PreparedStatement musiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery("musique", musiqueDb));
            musiqueStatement.executeUpdate();
            musiqueStatement.close();

            // INSERTED MUSIC CODE RETRIEVING
            PreparedStatement idMusiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.findBySpecificColumn(
                    "musique", "titreMusique", musiqueDb.getTitreMusique()));
            ResultSet result = idMusiqueStatement.executeQuery();
            result.next();
            Integer musiqueIdGenerated = result.getInt("codeMusique");
            idMusiqueStatement.close();

            // MUSIC/ARTIST(S) ASSIGNMENT
            PreparedStatement artistesMusiqueStatement = CONNECTION.prepareStatement("INSERT INTO posseder VALUES (?, ?)");

            for (AuteurDb artiste : musiqueDb.getListeAuteurs()) {
                artistesMusiqueStatement.setInt(1, musiqueIdGenerated);
                artistesMusiqueStatement.setInt(2, artiste.getIdentifiantAuteur());
                artistesMusiqueStatement.executeUpdate();
            }

            artistesMusiqueStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(MusiqueDb musiqueDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery("musique", musiqueDb));
            statement.executeUpdate();
            statement.close();

            // MUSIC/ARTIST(S) ASSIGNMENT
            Statement deletingArtistesMusiqueStatement = SQLiteConnection.getInstance().createStatement();
            deletingArtistesMusiqueStatement.execute("DELETE FROM posseder WHERE codeMusique = " + musiqueDb.getCodeMusique());
            deletingArtistesMusiqueStatement.close();

            PreparedStatement insertingArtistesMusiqueStatement = CONNECTION.prepareStatement("INSERT INTO posseder VALUES (?, ?)");

            for (AuteurDb artiste : musiqueDb.getListeAuteurs()) {
                insertingArtistesMusiqueStatement.setInt(1, musiqueDb.getCodeMusique());
                insertingArtistesMusiqueStatement.setInt(2, artiste.getIdentifiantAuteur());
                insertingArtistesMusiqueStatement.executeUpdate();
            }

            insertingArtistesMusiqueStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MusiqueDb musiqueDb) throws TransactionalException {
        try {
            MusiqueDao musiqueDao = new MusiqueDao();

            PreparedStatement artistesMusiqueStatement = CONNECTION.prepareStatement("DELETE FROM posseder WHERE codeMusique=" + musiqueDb.getCodeMusique());
            artistesMusiqueStatement.executeUpdate();
            artistesMusiqueStatement.close();

            PreparedStatement musiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery("musique", musiqueDb.getCodeMusique()));
            musiqueStatement.executeUpdate();
            musiqueStatement.close();

            Integer nbMusiques = musiqueDao.findAll().size();
            Statement statement = SQLiteConnection.getInstance().createStatement();
            statement.execute("UPDATE sqlite_sequence SET seq = '" + nbMusiques + "' WHERE name = 'musique'");
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MusiqueDb find(int id) throws TransactionalException {

        try {
            MusiqueDb musiqueDb = new MusiqueDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", id));
            ResultSet result = statement.executeQuery();
            result.next();
            musiqueDb.setCodeMusique(result.getInt("codeMusique"));
            musiqueDb.setTitreMusique(result.getString("titreMusique"));
            musiqueDb.setDureeMusique(result.getString("dureeMusique"));
            musiqueDb.setDateInsertionMusique(result.getString("dateInsertionMusique"));
            musiqueDb.setNomFichierMusique(result.getString("nomFichierMusique"));

            // MUSIC ARTIST(S) RETRIEVING
            PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                    "posseder", "codeMusique", musiqueDb.getCodeMusique()));
            ResultSet artistesResult = artistesStatement.executeQuery();
            AuteurDao auteurDao = new AuteurDao();
            List<AuteurDb> artistes = new ArrayList<>();

            while (artistesResult.next()) {
                AuteurDb auteur = auteurDao.find(artistesResult.getInt("identifiantAuteur"));
                artistes.add(auteur);
            }

            musiqueDb.setListeAuteurs(artistes);

            if (result.getInt("albumMusique") != 0) {
                musiqueDb.setAlbumMusique(albumDao.find(result.getInt("albumMusique")));
            }

            result.close();
            statement.close();

            return musiqueDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MusiqueDb> findAll() {

        try {
            List<MusiqueDb> musiquesList = new ArrayList<>();

            PreparedStatement musiquesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                    "musique"));
            ResultSet musiquesResult = musiquesStatement.executeQuery();


            while (musiquesResult.next()) {
                MusiqueDb musiqueDb = new MusiqueDb();
                List<AuteurDb> artistes = new ArrayList<>();

                musiqueDb.setCodeMusique(musiquesResult.getInt("codeMusique"));
                musiqueDb.setTitreMusique(musiquesResult.getString("titreMusique"));
                musiqueDb.setDureeMusique(musiquesResult.getString("dureeMusique"));
                musiqueDb.setDateInsertionMusique(musiquesResult.getString("dateInsertionMusique"));
                musiqueDb.setNomFichierMusique(musiquesResult.getString("nomFichierMusique"));

                // MUSIC ARTIST(S) RETRIEVING
                PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                        "posseder", "codeMusique", musiqueDb.getCodeMusique()));
                ResultSet artistesResult = artistesStatement.executeQuery();
                AuteurDao auteurDao = new AuteurDao();

                while (artistesResult.next()) {
                    AuteurDb auteur = auteurDao.find(artistesResult.getInt("identifiantAuteur"));
                    artistes.add(auteur);
                }

                if (musiquesResult.getInt("albumMusique") != 0) {
                    musiqueDb.setAlbumMusique(albumDao.find(musiquesResult.getInt("albumMusique")));
                }
                musiqueDb.setListeAuteurs(artistes);
                musiquesList.add(musiqueDb);
            }

            musiquesResult.close();
            musiquesStatement.close();

            return musiquesList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
