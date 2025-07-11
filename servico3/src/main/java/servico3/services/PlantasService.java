package servico3.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import servico3.dtos.PlantasRecordDto;
import servico3.models.Plantas;
import servico3.repositories.PlantasRepository;

@Service
public class PlantasService {
	private final PlantasRepository plantasRepository;
	private final RestTemplate restTemplate;

    @Value("${gestor.api.base.url:http://localhost:8080/api/sistemas}") 
    private String gestorApiBaseUrl;
	
	public PlantasService(PlantasRepository plantasRepository, RestTemplate restTemplate) {
		this.plantasRepository = plantasRepository;
		this.restTemplate = restTemplate;
	}
	
	public List<Plantas> getPlantas(){
		return plantasRepository.findAll();
	}
	
	public Plantas getPlantas(Long id) {
		return plantasRepository.findById(id)
		    .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + id)); 
	}
	
	public Plantas salvarPlantas(PlantasRecordDto plantasRecordDto) {
		Plantas plantas = new Plantas();
		
		plantas.setNome_area(plantasRecordDto.nome_area()); 
		
        if (plantasRecordDto.id_fazenda() == null) {
            throw new RuntimeException("ID da Fazenda é obrigatório.");
        }
        if (!verificarFazendaExiste(plantasRecordDto.id_fazenda())) {
            throw new RuntimeException("Fazenda não encontrada no serviço externo com ID: " + plantasRecordDto.id_fazenda());
        }
        plantas.setId_fazenda(plantasRecordDto.id_fazenda()); 

		return plantasRepository.save(plantas);
	}
	
	public void excluirPlantas(Long id) {
	    if (plantasRepository.existsById(id)) {
	        plantasRepository.deleteById(id);
	    } else {
	        throw new RuntimeException("Planta não encontrada com ID: " + id);
	    }
	}
	
	public Plantas atualizarPlantas(Long id, PlantasRecordDto plantasDto) {
	    Plantas plantasExistente = plantasRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Planta não encontrada com ID: " + id));

	    plantasExistente.setNome_area(plantasDto.nome_area());

        if (plantasDto.id_fazenda() != null) {
            if (!verificarFazendaExiste(plantasDto.id_fazenda())) {
                throw new RuntimeException("Fazenda não encontrada no serviço externo com ID: " + plantasDto.id_fazenda());
            }
            plantasExistente.setId_fazenda(plantasDto.id_fazenda());
        }

	    return plantasRepository.save(plantasExistente);
	}

    private boolean verificarFazendaExiste(Long fazendaId) {
        String url = gestorApiBaseUrl + "/fazendas/" + fazendaId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao verificar fazenda via Serviço Gestor: " + e.getMessage());
            return false;
        }
    }
}
