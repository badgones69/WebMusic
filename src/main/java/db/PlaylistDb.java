package db;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "playlist")
public class PlaylistDb {

    private Integer idPlaylist;
    private String intitulePlaylist;
    private List<MusiqueDb> listeMusiques;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlaylist")
    public Integer getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Integer idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    @Basic
    @Column(name = "intitulePlaylist")
    public String getIntitulePlaylist() {
        return intitulePlaylist;
    }

    public void setIntitulePlaylist(String intitulePlaylist) {
        this.intitulePlaylist = intitulePlaylist;
    }

    @ManyToMany
    @JoinTable(name = "contenir",
            joinColumns = {
                    @JoinColumn(name = "idPlaylist", referencedColumnName = "idPlaylist")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "codeMusique", referencedColumnName = "codeMusique")
            })
    public List<MusiqueDb> getListeMusiques() {
        return listeMusiques;
    }

    public void setListeMusiques(List<MusiqueDb> listeMusiques) {
        this.listeMusiques = listeMusiques;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistDb that = (PlaylistDb) o;
        return Objects.equals(idPlaylist, that.idPlaylist);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idPlaylist);
    }
}
