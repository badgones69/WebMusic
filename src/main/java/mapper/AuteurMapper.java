package mapper;

import db.AuteurDb;
import dto.AuteurDto;
import utils.LogUtils;

public class AuteurMapper {

    private AuteurMapper() {
        LogUtils.generateConstructorLog(AuteurMapper.class);
    }

    // AuteurDb TO AuteurDto CONVERTING
    public static AuteurDto toDto(AuteurDb auteurDb) {
        AuteurDto auteurDto = new AuteurDto();

        auteurDto.setIdentifiantAuteur(auteurDb.getIdentifiantAuteur());
        auteurDto.setNomAuteur(auteurDb.getNomAuteur());

        if (auteurDb.getPrenomAuteur() == null) {
            auteurDto.setPrenomAuteur("-");
        } else {
            auteurDto.setPrenomAuteur(auteurDb.getPrenomAuteur());
        }
        return auteurDto;
    }

    // AuteurDto TO AuteurDb CONVERTING
    public static AuteurDb toDb(AuteurDto auteurDto) {
        AuteurDb auteurDb = new AuteurDb();

        auteurDb.setIdentifiantAuteur(auteurDto.getIdentifiantAuteur());
        auteurDb.setNomAuteur(auteurDto.getNomAuteur());

        if ("-".equals(auteurDto.getPrenomAuteur())) {
            auteurDb.setPrenomAuteur(null);
        } else {
            auteurDb.setPrenomAuteur(auteurDto.getPrenomAuteur());
        }
        return auteurDb;
    }
}
