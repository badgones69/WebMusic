package mapper;

import dao.AlbumDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import dto.MusiqueDto;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import listeners.listMusic.ListMusicSelectionListener;

import java.util.List;

public class MusiqueMapper {

    // MusiqueDb TO MusiqueDto CONVERTING
    public static MusiqueDto toDto(MusiqueDb musiqueDb) {
        MusiqueDto musiqueDto = new MusiqueDto();

        musiqueDto.setCodeMusique(musiqueDb.getCodeMusique());
        musiqueDto.setTitreMusique(musiqueDb.getTitreMusique());
        musiqueDto.setDureeMusique(musiqueDb.getDureeMusique());
        musiqueDto.setDateInsertionMusique(musiqueDb.getDateInsertionMusique());
        musiqueDto.setNomFichierMusique(musiqueDb.getNomFichierMusique());
        musiqueDto.setAuteurs(artistsListInString(musiqueDb.getListeAuteurs()));

        if(musiqueDb.getAlbumMusique() == null) {
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
        MusiqueDb musiqueDb = new MusiqueDb();

        musiqueDb.setCodeMusique(musiqueDto.getCodeMusique());
        musiqueDb.setTitreMusique(musiqueDto.getTitreMusique());
        musiqueDb.setDureeMusique(musiqueDto.getDureeMusique());
        musiqueDb.setDateInsertionMusique(musiqueDto.getDateInsertionMusique());
        musiqueDb.setNomFichierMusique(musiqueDto.getNomFichierMusique());

        if(musiqueDto.getNumeroAlbumMusique() == null) {
            musiqueDb.setAlbumMusique(null);
        } else {
            musiqueDb.setAlbumMusique(albumDao.find(musiqueDto.getNumeroAlbumMusique()));
        }
        return musiqueDb;
    }

    // METHOD TO DISPLAY MUSIC'S ARTIST(S) NAME(S) INLINE
    private static String artistsListInString(List<AuteurDb> artistsList) {
        String stringList = "";

        for (int i = 0; i < artistsList.size(); i++) {
            if(artistsList.get(i).getPrenomAuteur() != null) {
                stringList += artistsList.get(i).getPrenomAuteur() + " ";
            }

            stringList += artistsList.get(i).getNomAuteur();

            if(i != artistsList.size()-1) {
                stringList += " & ";
            }
        }
        return stringList;
    }
}
