package enums;

public enum TypeAction {
    ADD("ajouté"),
    EDIT("modifié"),
    DELETE("supprimé");

    private final String libelle;

    TypeAction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return this.libelle;
    }
}
