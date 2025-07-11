package servico3.services;

import java.util.List;

import org.springframework.stereotype.Service;

import servico3.dtos.GuiaRecordDto;
import servico3.models.Guia;
import servico3.models.Plantas; 
import servico3.repositories.GuiaRepository;
import servico3.repositories.PlantasRepository; 

@Service
public class GuiaService {
	private final GuiaRepository guiaRepository;
	private final PlantasRepository plantasRepository; 
	
	public GuiaService(GuiaRepository guiaRepository, PlantasRepository plantasRepository) {
		this.guiaRepository = guiaRepository;
		this.plantasRepository = plantasRepository; 
	}
	
	public List<Guia> getGuias(){
		return guiaRepository.findAll();
	}
	
	public Guia getGuia(Long id) {
		return guiaRepository.findById(id)
		    .orElseThrow(() -> new RuntimeException("Guia não encontrada com ID: " + id)); 
	}
	
	public Guia salvarGuia(GuiaRecordDto guiaRecordDto) {
		Guia guia = new Guia();
		guia.setQuantidade_agua(Double.parseDouble(guiaRecordDto.quantidade_agua())); 
		guia.setTempo_descanso(guiaRecordDto.tempo_descanso());
		
		Plantas planta = plantasRepository.findById(guiaRecordDto.id_planta())
				.orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + guiaRecordDto.id_planta()));
		guia.setPlanta(planta);
		
		return guiaRepository.save(guia);
	}
	
	public void excluirGuia(Guia guia) {
		guiaRepository.deleteById(guia.getId_guia());
	}
	
	public void excluirGuia(Long id) {
	    if (guiaRepository.existsById(id)) {
	        guiaRepository.deleteById(id);
	    } else {
	        throw new RuntimeException("Guia não encontrada com ID: " + id);
	    }
	}
	
	public Guia atualizarGuia(Long id, GuiaRecordDto guiaDto) {
	    Guia guiaExistente = guiaRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Guia não encontrada com ID: " + id));

	    guiaExistente.setQuantidade_agua(Double.parseDouble(guiaDto.quantidade_agua()));
	    guiaExistente.setTempo_descanso(guiaDto.tempo_descanso());

	    if (guiaDto.id_planta() != null) {
	        Plantas planta = plantasRepository.findById(guiaDto.id_planta())
	                .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + guiaDto.id_planta()));
	        guiaExistente.setPlanta(planta);
	    }

	    return guiaRepository.save(guiaExistente);
	}
}
