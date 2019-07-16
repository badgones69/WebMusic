package enums;

public enum TypeVersion {

    APPLICATION("Application-Version"),
    SQLITE("SQLite-Version"),
    JUNIT("JUnit-Version"),
    JAVA_EE_API("JavaEE-API-Version"),
    FONT_AWESOME("FontAwesome-Version"),
    LOG4J("Log4j-Version");

    private final String libelle;

    TypeVersion(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return this.libelle;
    }
}
