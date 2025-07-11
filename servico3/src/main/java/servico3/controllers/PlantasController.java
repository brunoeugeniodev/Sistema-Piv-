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

import servico3.dtos.PlantasRecordDto; 
import servico3.models.Plantas; 
import servico3.services.PlantasService; 

@RestController
@RequestMapping("/api/plantas") 
public class PlantasController {

	@Autowired
	private PlantasService plantasService; 
	
	@CrossOrigin(origins = "http://localhost")
	@GetMapping 
	public List<Plantas> getPlantas(){ 
		return plantasService.getPlantas();
	}
	
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/{id}") 
    public ResponseEntity<Plantas> getPlantasById(@PathVariable Long id) {
        try {
            Plantas plantas = plantasService.getPlantas(id); 
            return ResponseEntity.ok(plantas); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }

	@CrossOrigin(origins = "http://localhost")
	@PostMapping 
	public ResponseEntity<Plantas> salvarPlantas(@RequestBody PlantasRecordDto plantasDto) { 
        try {
            Plantas novaPlanta = plantasService.salvarPlantas(plantasDto); 
            return ResponseEntity.status(201).body(novaPlanta); 
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); 
        }
	}
	
	@CrossOrigin(origins = "http://localhost")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarPlantas(@PathVariable Long id) {
	    try {
	    	plantasService.excluirPlantas(id);
	        return ResponseEntity.noContent().build(); 
	    } catch (RuntimeException e) {
	        return ResponseEntity.notFound().build(); 
	    }
	}
	
	@CrossOrigin(origins = "http://localhost")
	@PutMapping("/{id}") 
	public ResponseEntity<Plantas> atualizarPlantas(@PathVariable Long id, @RequestBody PlantasRecordDto plantasDto) {
        try {
            Plantas plantaAtualizada = plantasService.atualizarPlantas(id, plantasDto); 
            return ResponseEntity.ok(plantaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
	}
}
