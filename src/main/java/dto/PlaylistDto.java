package dto;

import db.MusiqueDb;

import java.util.List;

public class PlaylistDto {

    private Integer idPlaylist;
    private String intitulePlaylist;
    private String dateActionPlaylist;
    private List<MusiqueDb> listeMusiques;
    private String nbMusicsPlaylist;

    public Integer getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Integer idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getIntitulePlaylist() {
        return intitulePlaylist;
    }

    public void setIntitulePlaylist(String intitulePlaylist) {
        this.intitulePlaylist = intitulePlaylist;
    }

    public String getDateActionPlaylist() {
        return dateActionPlaylist;
    }

    public void setDateActionPlaylist(String dateActionPlaylist) {
        this.dateActionPlaylist = dateActionPlaylist;
    }

    public List<MusiqueDb> getListeMusiques() {
        return listeMusiques;
    }

    public void setListeMusiques(List<MusiqueDb> listeMusiques) {
        this.listeMusiques = listeMusiques;
    }

    public String getNbMusicsPlaylist() {
        return nbMusicsPlaylist;
    }

    public void setNbMusicsPlaylist(String nbMusicsPlaylist) {
        this.nbMusicsPlaylist = nbMusicsPlaylist;
    }
}
