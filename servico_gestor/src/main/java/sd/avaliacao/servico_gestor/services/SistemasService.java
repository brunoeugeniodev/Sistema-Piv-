package sd.avaliacao.servico_gestor.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SistemasService {
    private static final Logger logger = LoggerFactory.getLogger(SistemasService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Inject URLs using @Value from application.properties
    @Value("${servico1.url.usuarios}")
    private String url_servico1_usuarios;
    @Value("${servico1.url.fazendas}")
    private String url_servico1_fazendas;
    @Value("${servico2.url.pivos}")
    private String url_servico2_pivos;
    @Value("${servico2.url.historicos}")
    private String url_servico2_historicos;
    @Value("${servico3.url.plantas}")
    private String url_servico3_plantas;
    @Value("${servico3.url.guias}")
    private String url_servico3_guias;
    @Value("${servico3.url.sensores}")
    private String url_servico3_sensores;
    @Value("${servico3.url.mensagens}")
    private String url_servico3_mensagens;

    public SistemasService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // --- Getters for the URLs (existing and new) ---
    public String getUrlServico1Usuarios() {
        return url_servico1_usuarios;
    }

    public String getUrlServico1Fazendas() {
        return url_servico1_fazendas;
    }

    public String getUrlServico2Pivos() {
        return url_servico2_pivos;
    }

    public String getUrlServico2Historicos() {
        return url_servico2_historicos;
    }

    public String getUrlServico3Plantas() {
        return url_servico3_plantas;
    }

    public String getUrlServico3Guias() {
        return url_servico3_guias;
    }

    public String getUrlServico3Sensores() {
        return url_servico3_sensores;
    }

    public String getUrlServico3Mensagens() {
        return url_servico3_mensagens;
    }

    // --- Métodos Serviço 1 - API Usuários ---

    public ResponseEntity<String> getUsuarios() {
        return restTemplate.getForEntity(url_servico1_usuarios, String.class);
    }

    public ResponseEntity<String> getUsuarioById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico1_usuarios + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Usuário com ID {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarUsuario(RequestEntity<String> usuario) {
        return restTemplate.postForEntity(url_servico1_usuarios, usuario, String.class);
    }

    public ResponseEntity<String> atualizarUsuario(Long id, RequestEntity<String> usuario) {
        try {
            return restTemplate.exchange(url_servico1_usuarios + "/" + id, HttpMethod.PUT, usuario, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Usuário com ID {} não encontrado para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarUsuario(Long id) {
        try {
            restTemplate.delete(url_servico1_usuarios + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Usuário com ID {} não encontrado para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar usuário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Métodos Serviço 1 - API Fazendas ---

    public ResponseEntity<String> getFazendas() {
        return restTemplate.getForEntity(url_servico1_fazendas, String.class);
    }

    public ResponseEntity<String> getFazendaById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico1_fazendas + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Fazenda com ID {} não encontrada.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar fazenda com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar fazenda: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarFazenda(RequestEntity<String> fazenda) {
        return restTemplate.postForEntity(url_servico1_fazendas, fazenda, String.class);
    }

    public ResponseEntity<String> atualizarFazenda(Long id, RequestEntity<String> fazenda) {
        try {
            return restTemplate.exchange(url_servico1_fazendas + "/" + id, HttpMethod.PUT, fazenda, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Fazenda com ID {} não encontrada para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar fazenda com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar fazenda: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarFazenda(Long id) {
        try {
            restTemplate.delete(url_servico1_fazendas + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Fazenda com ID {} não encontrada para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar fazenda com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Long> getFazendaIdByUsuarioId(Long usuarioId) {
        ResponseEntity<String> usuarioResponse = getUsuarioById(usuarioId);

        if (usuarioResponse.getStatusCode().is2xxSuccessful() && usuarioResponse.getBody() != null) {
            try {
                JsonNode root = objectMapper.readTree(usuarioResponse.getBody());
                JsonNode fazendaNode = root.get("fazenda");
                if (fazendaNode != null && fazendaNode.has("id_fazenda")) {
                    Long idFazenda = fazendaNode.get("id_fazenda").asLong();
                    return ResponseEntity.ok(idFazenda);
                } else {
                    logger.warn("Fazenda não encontrada para o usuário com ID {}.", usuarioId);
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                logger.error("Erro ao parsear resposta JSON do usuário ou encontrar id_fazenda para usuarioId={}: {}", usuarioId, e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            logger.warn("Falha ao obter dados do usuário com ID {}. Status: {}", usuarioId, usuarioResponse.getStatusCode());
            return ResponseEntity.status(usuarioResponse.getStatusCode()).build();
        }
    }

    // --- Métodos Serviço 2 - API Pivôs ---

    public ResponseEntity<String> getPivos() {
        return restTemplate.getForEntity(url_servico2_pivos, String.class);
    }

    public ResponseEntity<String> getPivoById(String id) {
        try {
            return restTemplate.getForEntity(url_servico2_pivos + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Pivô com ID {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar pivô com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar pivô: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarPivo(RequestEntity<String> pivo) {
        return restTemplate.postForEntity(url_servico2_pivos, pivo, String.class);
    }

    public ResponseEntity<String> atualizarPivo(String id, RequestEntity<String> pivo) {
        try {
            return restTemplate.exchange(url_servico2_pivos + "/" + id, HttpMethod.PUT, pivo, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Pivô com ID {} não encontrado para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar pivô com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar pivô: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarPivo(String id) {
        try {
            restTemplate.delete(url_servico2_pivos + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Pivô com ID {} não encontrado para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar pivô com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Métodos Serviço 2 - API Históricos ---

    public ResponseEntity<String> getHistoricos() {
        return restTemplate.getForEntity(url_servico2_historicos, String.class);
    }

    public ResponseEntity<String> getHistoricoById(String id) {
        try {
            return restTemplate.getForEntity(url_servico2_historicos + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Histórico com ID {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar histórico com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar histórico: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarHistorico(RequestEntity<String> historico) {
        return restTemplate.postForEntity(url_servico2_historicos, historico, String.class);
    }

    public ResponseEntity<String> atualizarHistorico(String id, RequestEntity<String> historico) {
        try {
            return restTemplate.exchange(url_servico2_historicos + "/" + id, HttpMethod.PUT, historico, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Histórico com ID {} não encontrado para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar histórico com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar histórico: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarHistorico(String id) {
        try {
            restTemplate.delete(url_servico2_historicos + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Histórico com ID {} não encontrado para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar histórico com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Novos Métodos Serviço 3 - API Plantas ---

    public ResponseEntity<String> getPlantas() {
        return restTemplate.getForEntity(url_servico3_plantas, String.class);
    }

    public ResponseEntity<String> getPlantaById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico3_plantas + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Planta com ID {} não encontrada.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar planta com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar planta: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarPlanta(RequestEntity<String> plantaJson) {
        return restTemplate.postForEntity(url_servico3_plantas, plantaJson, String.class);
    }

    public ResponseEntity<String> atualizarPlanta(Long id, RequestEntity<String> plantaJson) {
        try {
            return restTemplate.exchange(url_servico3_plantas + "/" + id, HttpMethod.PUT, plantaJson, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Planta com ID {} não encontrada para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar planta com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar planta: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarPlanta(Long id) {
        try {
            restTemplate.delete(url_servico3_plantas + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Planta com ID {} não encontrada para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar planta com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Novos Métodos Serviço 3 - API Guias ---

    public ResponseEntity<String> getGuias() {
        return restTemplate.getForEntity(url_servico3_guias, String.class);
    }

    public ResponseEntity<String> getGuiaById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico3_guias + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Guia com ID {} não encontrada.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar guia com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar guia: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarGuia(RequestEntity<String> guiaJson) {
        return restTemplate.postForEntity(url_servico3_guias, guiaJson, String.class);
    }

    public ResponseEntity<String> atualizarGuia(Long id, RequestEntity<String> guiaJson) {
        try {
            return restTemplate.exchange(url_servico3_guias + "/" + id, HttpMethod.PUT, guiaJson, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Guia com ID {} não encontrada para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar guia com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar guia: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarGuia(Long id) {
        try {
            restTemplate.delete(url_servico3_guias + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Guia com ID {} não encontrada para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar guia com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Novos Métodos Serviço 3 - API Sensores ---

    public ResponseEntity<String> getSensores() {
        return restTemplate.getForEntity(url_servico3_sensores, String.class);
    }

    public ResponseEntity<String> getSensorById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico3_sensores + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Sensor com ID {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar sensor com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar sensor: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarSensor(RequestEntity<String> sensorJson) {
        return restTemplate.postForEntity(url_servico3_sensores, sensorJson, String.class);
    }

    public ResponseEntity<String> atualizarSensor(Long id, RequestEntity<String> sensorJson) {
        try {
            return restTemplate.exchange(url_servico3_sensores + "/" + id, HttpMethod.PUT, sensorJson, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Sensor com ID {} não encontrado para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar sensor com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar sensor: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarSensor(Long id) {
        try {
            restTemplate.delete(url_servico3_sensores + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Sensor com ID {} não encontrado para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar sensor com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Novos Métodos Serviço 3 - API Mensagens ---

    public ResponseEntity<String> getMensagens() {
        return restTemplate.getForEntity(url_servico3_mensagens, String.class);
    }

    public ResponseEntity<String> getMensagemById(Long id) {
        try {
            return restTemplate.getForEntity(url_servico3_mensagens + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Mensagem com ID {} não encontrada.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar mensagem com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao buscar mensagem: " + e.getMessage());
        }
    }

    public ResponseEntity<String> salvarMensagem(RequestEntity<String> mensagemJson) {
        return restTemplate.postForEntity(url_servico3_mensagens, mensagemJson, String.class);
    }

    public ResponseEntity<String> atualizarMensagem(Long id, RequestEntity<String> mensagemJson) {
        try {
            return restTemplate.exchange(url_servico3_mensagens + "/" + id, HttpMethod.PUT, mensagemJson, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Mensagem com ID {} não encontrada para atualização.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar mensagem com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao atualizar mensagem: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> deletarMensagem(Long id) {
        try {
            restTemplate.delete(url_servico3_mensagens + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Mensagem com ID {} não encontrada para exclusão.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar mensagem com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}