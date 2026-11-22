package ma.fpl.compteservice.controller;

import jakarta.validation.Valid;
import ma.fpl.compteservice.dto.OperationDto;
import ma.fpl.compteservice.dto.RequestCompteDto;
import ma.fpl.compteservice.dto.ResponseCompteDto;
import ma.fpl.compteservice.dto.SoldeDto;
import ma.fpl.compteservice.service.CompteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCompteDto ajouter(@Valid @RequestBody RequestCompteDto dto) {
        return compteService.ajouter(dto);
    }

    @PutMapping("/{id}")
    public ResponseCompteDto modifier(@PathVariable Long id, @Valid @RequestBody RequestCompteDto dto) {
        return compteService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        compteService.supprimer(id);
    }

    @GetMapping("/{id}")
    public ResponseCompteDto afficherParId(@PathVariable Long id) {
        return compteService.afficherParId(id);
    }

    @GetMapping
    public List<ResponseCompteDto> afficherTous() {
        return compteService.afficherTous();
    }

    @GetMapping("/{id}/solde")
    public SoldeDto consulterSolde(@PathVariable Long id) {
        return compteService.consulterSolde(id);
    }

    @PostMapping("/{id}/credit")
    public ResponseCompteDto crediter(@PathVariable Long id, @Valid @RequestBody OperationDto dto) {
        return compteService.crediter(id, dto.montant());
    }

    @PostMapping("/{id}/debit")
    public ResponseCompteDto debiter(@PathVariable Long id, @Valid @RequestBody OperationDto dto) {
        return compteService.debiter(id, dto.montant());
    }
}
