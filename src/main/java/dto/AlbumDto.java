package dto;

public class AlbumDto {

    private Integer numeroAlbum;
    private String titreAlbum;
    private String anneeAlbum;
    private String nbMusicsAlbum;

    public Integer getNumeroAlbum() {
        return numeroAlbum;
    }

    public void setNumeroAlbum(Integer numeroAlbum) {
        this.numeroAlbum = numeroAlbum;
    }

    public String getTitreAlbum() {
        return titreAlbum;
    }

    public void setTitreAlbum(String titreAlbum) {
        this.titreAlbum = titreAlbum;
    }

    public String getAnneeAlbum() {
        return anneeAlbum;
    }

    public void setAnneeAlbum(String anneeAlbum) {
        this.anneeAlbum = anneeAlbum;
    }

    public String getNbMusicsAlbum() {
        return nbMusicsAlbum;
    }

    public void setNbMusicsAlbum(String nbMusicsAlbum) {
        this.nbMusicsAlbum = nbMusicsAlbum;
    }
}
