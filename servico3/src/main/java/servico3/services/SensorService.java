package servico3.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import servico3.dtos.SensorRecordDto;
import servico3.models.Sensor;
import servico3.repositories.SensorRepository; 

@Service
public class SensorService {
	private final SensorRepository sensorRepository;
	private final RestTemplate restTemplate; 

    @Value("${gestor.api.base.url:http://localhost:8080/api/sistemas}") 
    private String gestorApiBaseUrl;
	
	public SensorService(SensorRepository sensorRepository, RestTemplate restTemplate) {
		this.sensorRepository = sensorRepository;
		this.restTemplate = restTemplate; 
	}
	
	public List<Sensor> getSensores(){
		return sensorRepository.findAll();
	}
	
	public Sensor getSensor(Long id) {
		return sensorRepository.findById(id)
		    .orElseThrow(() -> new RuntimeException("Sensor não encontrado com ID: " + id)); 
	}
	
	public Sensor salvarSensor(SensorRecordDto sensorRecordDto) {
		Sensor sensor = new Sensor();
		
		sensor.setTipo_sensor(sensorRecordDto.tipo_sensor()); 
		sensor.setStatus_sensor(sensorRecordDto.status_sensor());
		
        if (sensorRecordDto.id_pivo() == null || sensorRecordDto.id_pivo().isEmpty()) {
            throw new RuntimeException("ID do Pivô é obrigatório.");
        }
        if (!verificarPivoExiste(sensorRecordDto.id_pivo())) {
            throw new RuntimeException("Pivô não encontrado no serviço externo com ID: " + sensorRecordDto.id_pivo());
        }
        sensor.setId_pivo(sensorRecordDto.id_pivo()); 

		return sensorRepository.save(sensor);
	}
	
	public void excluirSensor(Long id) {
	    if (sensorRepository.existsById(id)) {
	        sensorRepository.deleteById(id);
	    } else {
	        throw new RuntimeException("Sensor não encontrado com ID: " + id);
	    }
	}
	
	public Sensor atualizarSensor(Long id, SensorRecordDto sensorDto) {
	    Sensor sensorExistente = sensorRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Sensor não encontrado com ID: " + id));

	    sensorExistente.setTipo_sensor(sensorDto.tipo_sensor());
	    sensorExistente.setStatus_sensor(sensorDto.status_sensor());

        if (sensorDto.id_pivo() != null && !sensorDto.id_pivo().isEmpty()) {
            if (!verificarPivoExiste(sensorDto.id_pivo())) {
                throw new RuntimeException("Pivô não encontrado no serviço externo com ID: " + sensorDto.id_pivo());
            }
            sensorExistente.setId_pivo(sensorDto.id_pivo());
        }

	    return sensorRepository.save(sensorExistente);
	}

    private boolean verificarPivoExiste(String pivoId) {
        String url = gestorApiBaseUrl + "/pivos/" + pivoId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao verificar pivô via Serviço Gestor: " + e.getMessage());
            return false;
        }
    }
}
