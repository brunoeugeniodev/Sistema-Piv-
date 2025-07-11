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

import servico3.dtos.SensorRecordDto;
import servico3.models.Sensor;
import servico3.services.SensorService; 

@RestController
@RequestMapping("/api/sensores")
public class SensorController {

	@Autowired
	private SensorService sensorService; 
	
	@CrossOrigin(origins = "http://localhost")
	@GetMapping
	public List<Sensor> getSensores(){ 
		return sensorService.getSensores();
	}
	
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        try {
            Sensor sensor = sensorService.getSensor(id); 
            return ResponseEntity.ok(sensor); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }

	@CrossOrigin(origins = "http://localhost")
	@PostMapping 
	public ResponseEntity<Sensor> salvarSensor(@RequestBody SensorRecordDto sensorDto) { 
        try {
            Sensor novoSensor = sensorService.salvarSensor(sensorDto); 
            return ResponseEntity.status(201).body(novoSensor); 
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); 
        }
	}
	
	@CrossOrigin(origins = "http://localhost")
	@DeleteMapping("/{id}") 
	public ResponseEntity<Void> deletarSensor(@PathVariable Long id) {
	    try {
	    	sensorService.excluirSensor(id);
	        return ResponseEntity.noContent().build(); 
	    } catch (RuntimeException e) {
	        return ResponseEntity.notFound().build(); 
	    }
	}
	
	@CrossOrigin(origins = "http://localhost")
	@PutMapping("/{id}") 
	public ResponseEntity<Sensor> atualizarSensor(@PathVariable Long id, @RequestBody SensorRecordDto sensorDto) {
        try {
            Sensor sensorAtualizado = sensorService.atualizarSensor(id, sensorDto); 
            return ResponseEntity.ok(sensorAtualizado); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
	}
}
