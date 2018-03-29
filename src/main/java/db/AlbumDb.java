package db;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "album")
public class AlbumDb {

    private Integer numeroAlbum;
    private String titreAlbum;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numeroAlbum")
    public Integer getNumeroAlbum() {
        return numeroAlbum;
    }

    public void setNumeroAlbum(Integer numeroAlbum) {
        this.numeroAlbum = numeroAlbum;
    }

    @Basic
    @Column(name = "titreAlbum")
    public String getTitreAlbum() {
        return titreAlbum;
    }

    public void setTitreAlbum(String titreAlbum) {
        this.titreAlbum = titreAlbum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumDb albumDb = (AlbumDb) o;
        return Objects.equals(numeroAlbum, albumDb.numeroAlbum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroAlbum);
    }
}
