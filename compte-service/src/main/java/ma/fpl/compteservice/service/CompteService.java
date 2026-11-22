package ma.fpl.compteservice.service;

import ma.fpl.compteservice.dto.RequestCompteDto;
import ma.fpl.compteservice.dto.ResponseCompteDto;
import ma.fpl.compteservice.dto.SoldeDto;
import ma.fpl.compteservice.entities.Compte;
import ma.fpl.compteservice.mapper.CompteMapper;
import ma.fpl.compteservice.repository.CompteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
    }

    private Compte trouverCompte(Long id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Compte introuvable : " + id));
    }

    public ResponseCompteDto ajouter(RequestCompteDto dto) {
        return compteMapper.toDto(compteRepository.save(compteMapper.toEntity(dto)));
    }

    public ResponseCompteDto modifier(Long id, RequestCompteDto dto) {
        Compte compte = trouverCompte(id);
        compteMapper.updateEntity(compte, dto);
        return compteMapper.toDto(compteRepository.save(compte));
    }

    public void supprimer(Long id) {
        compteRepository.delete(trouverCompte(id));
    }

    @Transactional(readOnly = true)
    public ResponseCompteDto afficherParId(Long id) {
        return compteMapper.toDto(trouverCompte(id));
    }

    @Transactional(readOnly = true)
    public List<ResponseCompteDto> afficherTous() {
        return compteRepository.findAll().stream().map(compteMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SoldeDto consulterSolde(Long id) {
        Compte compte = trouverCompte(id);
        return new SoldeDto(compte.getId(), compte.getSolde());
    }

    public ResponseCompteDto crediter(Long id, BigDecimal montant) {
        Compte compte = trouverCompte(id);
        compte.setSolde(compte.getSolde().add(montant));
        return compteMapper.toDto(compteRepository.save(compte));
    }

    public ResponseCompteDto debiter(Long id, BigDecimal montant) {
        Compte compte = trouverCompte(id);
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solde insuffisant : solde = " + compte.getSolde() + ", montant = " + montant);
        }
        compte.setSolde(compte.getSolde().subtract(montant));
        return compteMapper.toDto(compteRepository.save(compte));
    }
}
