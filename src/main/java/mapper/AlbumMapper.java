package mapper;

import db.AlbumDb;
import dto.AlbumDto;

public class AlbumMapper {

    // AlbumDb TO AlbumDto CONVERTING
    public static AlbumDto toDto(AlbumDb auteurDb) {
        AlbumDto albumDto = new AlbumDto();

        albumDto.setNumeroAlbum(auteurDb.getNumeroAlbum());
        albumDto.setTitreAlbum(auteurDb.getTitreAlbum());

        if (auteurDb.getAnneeAlbum() == null) {
            albumDto.setAnneeAlbum("-");
        } else {
            albumDto.setAnneeAlbum(String.valueOf(auteurDb.getAnneeAlbum()));
        }
        return albumDto;
    }

    // AlbumDto TO AlbumDb CONVERTING
    public static AlbumDb toDb(AlbumDto albumDto) {
        AlbumDb auteurDb = new AlbumDb();

        auteurDb.setNumeroAlbum(albumDto.getNumeroAlbum());
        auteurDb.setTitreAlbum(albumDto.getTitreAlbum());

        if ("-".equals(albumDto.getAnneeAlbum())) {
            auteurDb.setAnneeAlbum(null);
        } else {
            auteurDb.setAnneeAlbum(Integer.parseInt(albumDto.getAnneeAlbum()));
        }
        return auteurDb;
    }
}
