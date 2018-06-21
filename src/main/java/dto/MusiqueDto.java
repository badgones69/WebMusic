package dto;

import db.PlaylistDb;

import java.util.List;

public class MusiqueDto {

    private Integer codeMusique;
    private String titreMusique;
    private String dureeMusique;
    private String dateInsertionMusique;
    private String nomFichierMusique;
    private Integer numeroAlbumMusique;
    private String titreAlbumMusique;
    private List<PlaylistDb> listePlaylists;
    private String auteurs;

    public Integer getCodeMusique() {
        return codeMusique;
    }

    public void setCodeMusique(Integer codeMusique) {
        this.codeMusique = codeMusique;
    }

    public String getTitreMusique() {
        return titreMusique;
    }

    public void setTitreMusique(String titreMusique) {
        this.titreMusique = titreMusique;
    }

    public String getDureeMusique() {
        return dureeMusique;
    }

    public void setDureeMusique(String dureeMusique) {
        this.dureeMusique = dureeMusique;
    }

    public String getDateInsertionMusique() {
        return dateInsertionMusique;
    }

    public void setDateInsertionMusique(String dateInsertionMusique) {
        this.dateInsertionMusique = dateInsertionMusique;
    }

    public String getNomFichierMusique() {
        return nomFichierMusique;
    }

    public void setNomFichierMusique(String nomFichierMusique) {
        this.nomFichierMusique = nomFichierMusique;
    }

    public Integer getNumeroAlbumMusique() {
        return numeroAlbumMusique;
    }

    public void setNumeroAlbumMusique(Integer numeroAlbumMusique) {
        this.numeroAlbumMusique = numeroAlbumMusique;
    }

    public String getTitreAlbumMusique() {
        return titreAlbumMusique;
    }

    public void setTitreAlbumMusique(String titreAlbumMusique) {
        this.titreAlbumMusique = titreAlbumMusique;
    }

    public List<PlaylistDb> getListePlaylists() {
        return listePlaylists;
    }

    public void setListePlaylists(List<PlaylistDb> listePlaylists) {
        this.listePlaylists = listePlaylists;
    }

    public String getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(String auteurs) {
        this.auteurs = auteurs;
    }
}
