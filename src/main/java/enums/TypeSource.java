package enums;

public enum TypeSource {

    ALBUM("album"),
    ARTIST("artiste"),
    COMMON(""),
    MUSIC("musique"),
    PLAYLIST("playlist");

    private final String libelle;

    TypeSource(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return this.libelle;
    }
}
