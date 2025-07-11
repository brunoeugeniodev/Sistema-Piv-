package servico3.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import servico3.dtos.MensagensRecordDto;
import servico3.models.Mensagens;
import servico3.repositories.MensagensRepository;

@Service
public class MensagensService {
	private final MensagensRepository mensagensRepository;
	private final RestTemplate restTemplate;

	@Value("${gestor.api.base.url:http://localhost:8080/api/sistemas}")
	private String gestorApiBaseUrl;

	public MensagensService(MensagensRepository mensagensRepository, RestTemplate restTemplate) {
		this.mensagensRepository = mensagensRepository;
		this.restTemplate = restTemplate;
	}

	public List<Mensagens> getMensagens(){
		return mensagensRepository.findAll();
	}

	public Mensagens getMensagens(Long id) {
		return mensagensRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada com ID: " + id));
	}

	public Mensagens salvarMensagens(MensagensRecordDto mensagensRecordDto) {
		Mensagens mensagens = new Mensagens();

		mensagens.setTipo_mensagem(mensagensRecordDto.tipo_mensagem());
		mensagens.setConteudo_mensagem(mensagensRecordDto.conteudo_mensagem());

		if (mensagensRecordDto.id_pivo() == null || mensagensRecordDto.id_pivo().isEmpty()) {
			throw new RuntimeException("ID do Pivô é obrigatório.");
		}
		if (!verificarPivoExiste(mensagensRecordDto.id_pivo())) {
			throw new RuntimeException("Pivô não encontrado no serviço externo com ID: " + mensagensRecordDto.id_pivo());
		}
		mensagens.setId_pivo(mensagensRecordDto.id_pivo());

		return mensagensRepository.save(mensagens);
	}

	public void excluirMensagens(Long id) {
		if (mensagensRepository.existsById(id)) {
			mensagensRepository.deleteById(id);
		} else {
			throw new RuntimeException("Mensagem não encontrada com ID: " + id);
		}
	}

	public Mensagens atualizarMensagens(Long id, MensagensRecordDto mensagensDto) {
		Mensagens mensagensExistente = mensagensRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada com ID: " + id));

		mensagensExistente.setTipo_mensagem(mensagensDto.tipo_mensagem());
		mensagensExistente.setConteudo_mensagem(mensagensDto.conteudo_mensagem());

		if (mensagensDto.id_pivo() != null && !mensagensDto.id_pivo().isEmpty()) {
			if (!verificarPivoExiste(mensagensDto.id_pivo())) {
				throw new RuntimeException("Pivô não encontrado no serviço externo com ID: " + mensagensDto.id_pivo());
			}
			mensagensExistente.setId_pivo(mensagensDto.id_pivo());
		}

		return mensagensRepository.save(mensagensExistente);
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
