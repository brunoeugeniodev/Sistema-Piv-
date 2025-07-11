package servico3.controllers;

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

import servico3.dtos.GuiaRecordDto;
import servico3.models.Guia;
import servico3.services.GuiaService;

@RestController
@RequestMapping("/api/guias")
public class GuiaController {

	@Autowired
	private GuiaService guiaService;
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping 
	public List<Guia> getGuias(){ 
		return guiaService.getGuias();
	}
	
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{id}")
    public ResponseEntity<Guia> getGuiaById(@PathVariable Long id) {
        try {
            Guia guia = guiaService.getGuia(id);
            return ResponseEntity.ok(guia);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

	@CrossOrigin(origins = "http://localhost:8080")
	@PostMapping 
	public Guia salvarGuia(@RequestBody GuiaRecordDto guiaDto) { 
		return guiaService.salvarGuia(guiaDto);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarGuia(@PathVariable Long id) {
	    try {
	        guiaService.excluirGuia(id);
	        return ResponseEntity.noContent().build(); 
	    } catch (RuntimeException e) {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/{id}") 
	public Guia atualizarGuia(@PathVariable Long id, @RequestBody GuiaRecordDto guiaDto) {
	    return guiaService.atualizarGuia(id, guiaDto);
	}
}
