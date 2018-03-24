package dao;

import db.AlbumDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WebMusicDatabase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionalException;
import java.util.List;

public class AlbumDao extends AbstractDao<AlbumDb> {

    private static final Logger LOG = LogManager.getLogger(AlbumDao.class);

    @Inject
    @WebMusicDatabase
    EntityManager em;

    @Override
    public void persist(AlbumDb albumDb) {
        em.persist(albumDb);
    }

    @Override
    public void merge(AlbumDb albumDb) throws TransactionalException {
        em.merge(albumDb);
    }

    @Override
    public void remove(AlbumDb albumDb) throws TransactionalException {
        em.remove(albumDb);
    }

    @Override
    public AlbumDb find(int id) throws TransactionalException {
        return em.find(AlbumDb.class, id);
    }

    @Override
    public List<AlbumDb> findAll() {
        String sql = "SELECT a FROM AlbumDb a";
        return em.createQuery(sql, AlbumDb.class).getResultList();
    }
}
