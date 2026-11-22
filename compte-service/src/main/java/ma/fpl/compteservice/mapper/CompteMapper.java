package ma.fpl.compteservice.mapper;

import ma.fpl.compteservice.dto.RequestCompteDto;
import ma.fpl.compteservice.dto.ResponseCompteDto;
import ma.fpl.compteservice.entities.Compte;
import org.springframework.stereotype.Component;

@Component
public class CompteMapper {

    public Compte toEntity(RequestCompteDto dto) {
        Compte compte = new Compte();
        compte.setNom(dto.nom());
        compte.setTel(dto.tel());
        compte.setSolde(dto.solde());
        return compte;
    }

    public ResponseCompteDto toDto(Compte compte) {
        return new ResponseCompteDto(compte.getId(), compte.getNom(), compte.getTel(), compte.getSolde());
    }

    public void updateEntity(Compte compte, RequestCompteDto dto) {
        compte.setNom(dto.nom());
        compte.setTel(dto.tel());
        compte.setSolde(dto.solde());
    }
}
