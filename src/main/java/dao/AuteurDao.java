package dao;

import db.AuteurDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WebMusicDatabase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionalException;
import java.util.List;

public class AuteurDao extends AbstractDao<AuteurDb> {

    private static final Logger LOG = LogManager.getLogger(AuteurDao.class);

    @Inject
    @WebMusicDatabase
    EntityManager em;

    @Override
    public void persist(AuteurDb auteurDb) {
        em.persist(auteurDb);
    }

    @Override
    public void merge(AuteurDb auteurDb) throws TransactionalException {
        em.merge(auteurDb);
    }

    @Override
    public void remove(AuteurDb auteurDb) throws TransactionalException {
        em.remove(auteurDb);
    }

    @Override
    public AuteurDb find(int id) throws TransactionalException {
        return em.find(AuteurDb.class, id);
    }

    @Override
    public List<AuteurDb> findAll() {
        String sql = "SELECT a FROM AuteurDb a";
        return em.createQuery(sql, AuteurDb.class).getResultList();
    }
}
