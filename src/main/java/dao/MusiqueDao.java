package dao;

import db.MusiqueDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WebMusicDatabase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.TransactionalException;
import java.util.List;

public class MusiqueDao extends AbstractDao<MusiqueDb> {

    private static final Logger LOG = LogManager.getLogger(MusiqueDao.class);

    @Inject
    @WebMusicDatabase
    EntityManager em;

    @Override
    public void persist(MusiqueDb musiqueDb) {
        em.persist(musiqueDb);
    }

    @Override
    public void merge(MusiqueDb musiqueDb) throws TransactionalException {
        em.merge(musiqueDb);
    }

    @Override
    public void remove(MusiqueDb musiqueDb) throws TransactionalException {
        em.remove(musiqueDb);
    }

    @Override
    public MusiqueDb find(int id) throws TransactionalException {
        return em.find(MusiqueDb.class, id);
    }

    @Override
    public List<MusiqueDb> findAll() {
        String sql = "SELECT a FROM MusiqueDb a";
        return em.createQuery(sql, MusiqueDb.class).getResultList();
    }
}
