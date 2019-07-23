package mapper;

import dao.AlbumDao;
import dao.MusiqueDao;
import db.AuteurDb;
import db.MusiqueDb;
import dto.MusiqueDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MusiqueMapper {

    private static final Logger LOG = LogManager.getLogger(MusiqueMapper.class);

    private MusiqueMapper() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    // MusiqueDb TO MusiqueDto CONVERTING
    public static MusiqueDto toDto(MusiqueDb musiqueDb) {
        MusiqueDto musiqueDto = new MusiqueDto();

        musiqueDto.setCodeMusique(musiqueDb.getCodeMusique());
        musiqueDto.setTitreMusique(musiqueDb.getTitreMusique());
        musiqueDto.setDureeMusique(musiqueDb.getDureeMusique());
        musiqueDto.setDateActionMusique(musiqueDb.getDateActionMusique());
        musiqueDto.setNomFichierMusique(musiqueDb.getNomFichierMusique());
        musiqueDto.setAuteurs(artistsListInString(musiqueDb.getListeAuteurs()));

        if (musiqueDb.getAlbumMusique() == null) {
            musiqueDto.setNumeroAlbumMusique(null);
            musiqueDto.setTitreAlbumMusique("-");
        } else {
            musiqueDto.setNumeroAlbumMusique(musiqueDb.getAlbumMusique().getNumeroAlbum());
            musiqueDto.setTitreAlbumMusique(musiqueDb.getAlbumMusique().getTitreAlbum());
        }
        return musiqueDto;
    }

    // MusiqueDto TO MusiqueDb CONVERTING
    public static MusiqueDb toDb(MusiqueDto musiqueDto) {
        AlbumDao albumDao = new AlbumDao();
        MusiqueDao musiqueDao = new MusiqueDao();
        MusiqueDb musiqueDb = new MusiqueDb();

        musiqueDb.setCodeMusique(musiqueDto.getCodeMusique());
        musiqueDb.setTitreMusique(musiqueDto.getTitreMusique());
        musiqueDb.setDureeMusique(musiqueDto.getDureeMusique());
        musiqueDb.setDateActionMusique(musiqueDto.getDateActionMusique());
        musiqueDb.setNomFichierMusique(musiqueDto.getNomFichierMusique());
        musiqueDb.setListeAuteurs(musiqueDao.find(musiqueDto.getCodeMusique()).getListeAuteurs());

        if (musiqueDto.getNumeroAlbumMusique() == null) {
            musiqueDb.setAlbumMusique(null);
        } else {
            musiqueDb.setAlbumMusique(albumDao.find(musiqueDto.getNumeroAlbumMusique()));
        }
        return musiqueDb;
    }

    // METHOD TO DISPLAY MUSIC'S ARTIST(S) NAME(S) INLINE
    private static String artistsListInString(List<AuteurDb> artistsList) {
        StringBuilder stringListBuilder = new StringBuilder();

        for (int i = 0; i < artistsList.size(); i++) {
            if (artistsList.get(i).getPrenomAuteur() != null) {
                stringListBuilder.append(artistsList.get(i).getPrenomAuteur()).append(" ");
            }

            stringListBuilder.append(artistsList.get(i).getNomAuteur());

            if (i != artistsList.size() - 1) {
                stringListBuilder.append(", ");
            }
        }
        return stringListBuilder.toString();
    }
}
