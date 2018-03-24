package dao;

import db.PlaylistDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WebMusicDatabase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionalException;
import java.util.List;

public class PlaylistDao extends AbstractDao<PlaylistDb> {

    private static final Logger LOG = LogManager.getLogger(PlaylistDao.class);

    @Inject
    @WebMusicDatabase
    EntityManager em;

    @Override
    public void persist(PlaylistDb playlistDb) {
        em.persist(playlistDb);
    }

    @Override
    public void merge(PlaylistDb playlistDb) throws TransactionalException {
        em.merge(playlistDb);
    }

    @Override
    public void remove(PlaylistDb playlistDb) throws TransactionalException {
        em.remove(playlistDb);
    }

    @Override
    public PlaylistDb find(int id) throws TransactionalException {
        return em.find(PlaylistDb.class, id);
    }

    @Override
    public List<PlaylistDb> findAll() {
        String sql = "SELECT a FROM PlaylistDb a";
        return em.createQuery(sql, PlaylistDb.class).getResultList();
    }
}
