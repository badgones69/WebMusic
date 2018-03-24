package db;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "musique")
public class MusiqueDb {

    private Integer codeMusique;
    private String titreMusique;
    private String dureeMusique;
    private String dateInsertionMusique;
    private String nomFichierMusique;
    private AlbumDb albumMusique;
    private List<PlaylistDb> listePlaylists;
    private List<AuteurDb> listeAuteurs;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codeMusique")
    public Integer getCodeMusique() {
        return codeMusique;
    }

    @Basic
    @Column(name = "titreMusique")
    public String getTitreMusique() {
        return titreMusique;
    }

    public void setTitreMusique(String titreMusique) {
        this.titreMusique = titreMusique;
    }

    @Basic
    @Column(name = "dureeMusique")
    public String getDureeMusique() {
        return dureeMusique;
    }

    public void setDureeMusique(String dureeMusique) {
        this.dureeMusique = dureeMusique;
    }

    @Basic
    @Column(name = "dateInsertionMusique")
    public String getDateInsertionMusique() {
        return dateInsertionMusique;
    }

    public void setDateInsertionMusique(String dateInsertionMusique) {
        this.dateInsertionMusique = dateInsertionMusique;
    }

    @Basic
    @Column(name = "nomFichierMusique")
    public String getNomFichierMusique() {
        return nomFichierMusique;
    }

    public void setNomFichierMusique(String nomFichierMusique) {
        this.nomFichierMusique = nomFichierMusique;
    }

    @ManyToOne
    @JoinColumn(name = "albumMusique", referencedColumnName = "numeroAlbum")
    public AlbumDb getAlbumMusique() {
        return albumMusique;
    }

    public void setAlbumMusique(AlbumDb albumMusique) {
        this.albumMusique = albumMusique;
    }

    @ManyToMany(mappedBy = "listeMusiques")
    public List<PlaylistDb> getListePlaylists() {
        return listePlaylists;
    }

    public void setListePlaylists(List<PlaylistDb> listePlaylists) {
        this.listePlaylists = listePlaylists;
    }

    @ManyToMany
    @JoinTable(name = "posseder",
            joinColumns = {
                    @JoinColumn(name = "codeMusique", referencedColumnName = "codeMusique")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "identifiantAuteur", referencedColumnName = "identifiantAuteur")
            })
    public List<AuteurDb> getListeAuteurs() {
        return listeAuteurs;
    }

    public void setListeAuteurs(List<AuteurDb> listeAuteurs) {
        this.listeAuteurs = listeAuteurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusiqueDb musiqueDb = (MusiqueDb) o;
        return Objects.equals(codeMusique, musiqueDb.codeMusique);
    }

    @Override
    public int hashCode() {

        return Objects.hash(codeMusique);
    }
}
