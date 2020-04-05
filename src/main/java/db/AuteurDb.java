package db;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "auteur")
public class AuteurDb {

    private Integer identifiantAuteur;
    private String nomAuteur;
    private String prenomAuteur;
    private List<MusiqueDb> listeOeuvres;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifiantAuteur")
    public Integer getIdentifiantAuteur() {
        return identifiantAuteur;
    }

    public void setIdentifiantAuteur(Integer identifiantAuteur) {
        this.identifiantAuteur = identifiantAuteur;
    }

    @Basic
    @Column(name = "nomAuteur")
    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    @Basic
    @Column(name = "prenomAuteur")
    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }

    @ManyToMany(mappedBy = "listeAuteurs")
    public List<MusiqueDb> getListeOeuvres() {
        return listeOeuvres;
    }

    public void setListeOeuvres(List<MusiqueDb> listeOeuvres) {
        this.listeOeuvres = listeOeuvres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuteurDb auteurDb = (AuteurDb) o;
        return Objects.equals(identifiantAuteur, auteurDb.identifiantAuteur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiantAuteur);
    }

    @Override
    public String toString() {
        String identite = "";

        if(this.prenomAuteur != null) {
            identite = prenomAuteur + " ";
        }
        identite += nomAuteur;

        return identite;
    }
}
