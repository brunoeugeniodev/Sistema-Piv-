package sd.avaliacao.servico1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sd.avaliacao.servico1.dtos.FazendaRecordDto;
import sd.avaliacao.servico1.models.Fazenda;
import sd.avaliacao.servico1.services.FazendaService;

@RestController
@RequestMapping("/api/fazendas")
public class FazendaController {
	@Autowired
	private FazendaService fazendaService;
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping
	public List<Fazenda> geFazendas(){
		return fazendaService.getFazendas();
	}
	
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{id}")
    public ResponseEntity<Fazenda> getFazendaById(@PathVariable Long id) {
        try {
            Fazenda fazenda = fazendaService.getFazenda(id);
            return ResponseEntity.ok(fazenda);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

	@CrossOrigin(origins = "http://localhost:8080")
	@PostMapping
	public Fazenda salvarFazenda(@RequestBody FazendaRecordDto fazenda) {
		return fazendaService.salvarFazenda(fazenda);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping
	public void deletarFazenda(@RequestBody Fazenda fazenda) {
		fazendaService.excluirFazenda(fazenda);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping("/{id}")
	public void deletarFazenda(@PathVariable Long id) {
	    fazendaService.excluirFazenda(id);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/{id}")
	public Fazenda atualizarFazenda(@PathVariable Long id, @RequestBody FazendaRecordDto fazendaDto) {
	    return fazendaService.atualizarFazenda(id, fazendaDto);
	}
}
