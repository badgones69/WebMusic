package dto;

public class AuteurDto {

    private Integer identifiantAuteur;
    private String nomAuteur;
    private String prenomAuteur;

    public Integer getIdentifiantAuteur() {
        return identifiantAuteur;
    }

    public void setIdentifiantAuteur(Integer identifiantAuteur) {
        this.identifiantAuteur = identifiantAuteur;
    }

    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }
}
