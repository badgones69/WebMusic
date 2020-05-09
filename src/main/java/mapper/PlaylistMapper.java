package mapper;

import dao.PlaylistDao;
import db.PlaylistDb;
import dto.PlaylistDto;
import utils.LogUtils;

public class PlaylistMapper {

    private PlaylistMapper() {
        LogUtils.generateConstructorLog(PlaylistMapper.class);
    }

    // PlaylistDb TO PlaylistDto CONVERTING
    public static PlaylistDto toDto(PlaylistDb playlistDb) {
        PlaylistDto playlistDto = new PlaylistDto();
        Integer nbMusiques = playlistDb.getListeMusiques().size();

        playlistDto.setIdPlaylist(playlistDb.getIdPlaylist());
        playlistDto.setIntitulePlaylist(playlistDb.getIntitulePlaylist());
        playlistDto.setDateActionPlaylist(String.valueOf(playlistDb.getDateActionPlaylist()));
        playlistDto.setListeMusiques(playlistDb.getListeMusiques());

        playlistDto.setNbMusicsPlaylist(nbMusiques.toString());

        if (nbMusiques <= 1) {
            playlistDto.setNbMusicsPlaylist(playlistDto.getNbMusicsPlaylist() + " musique");
        } else {
            playlistDto.setNbMusicsPlaylist(playlistDto.getNbMusicsPlaylist() + " musiques");
        }

        return playlistDto;
    }

    // PlaylistDto TO PlaylistDb CONVERTING
    public static PlaylistDb toDb(PlaylistDto playlistDto) {
        PlaylistDb playlistDb = new PlaylistDb();
        PlaylistDao playlistDao = new PlaylistDao();

        playlistDb.setIdPlaylist(playlistDto.getIdPlaylist());
        playlistDb.setIntitulePlaylist(playlistDto.getIntitulePlaylist());
        playlistDb.setDateActionPlaylist(playlistDto.getDateActionPlaylist());
        playlistDb.setListeMusiques(playlistDao.find(playlistDto.getIdPlaylist()).getListeMusiques());

        return playlistDb;
    }
}
