package sd.avaliacao.servico_gestor.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import sd.avaliacao.servico_gestor.services.SistemasService;

@RestController
@RequestMapping("/api/sistemas")
public class SistemasController {

    @Autowired
    private SistemasService sistemasService;

    // --- Métodos Serviço 1 - API Usuários ---

    @GetMapping("/usuarios")
    public ResponseEntity<String> getUsuarios() {
        return sistemasService.getUsuarios();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<String> getUsuarioById(@PathVariable Long id) {
        return sistemasService.getUsuarioById(id);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<String> createUsuario(@RequestBody String usuarioJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico1Usuarios().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioJson);
        return sistemasService.salvarUsuario(request);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<String> updateUsuario(@PathVariable Long id, @RequestBody String usuarioJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico1Usuarios().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioJson);
        return sistemasService.atualizarUsuario(id, request);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        return sistemasService.deletarUsuario(id);
    }

    @GetMapping("/usuarios/{usuarioId}/fazenda")
    public ResponseEntity<Long> getFazendaIdFromUsuario(@PathVariable Long usuarioId) {
        return sistemasService.getFazendaIdByUsuarioId(usuarioId);
    }

    // --- Métodos Serviço 1 - API Fazendas ---

    @GetMapping("/fazendas")
    public ResponseEntity<String> getFazendas() {
        return sistemasService.getFazendas();
    }

    @GetMapping("/fazendas/{id}")
    public ResponseEntity<String> getFazendaById(@PathVariable Long id) {
        return sistemasService.getFazendaById(id);
    }

    @PostMapping("/fazendas")
    public ResponseEntity<String> createFazenda(@RequestBody String fazendaJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico1Fazendas().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(fazendaJson);
        return sistemasService.salvarFazenda(request);
    }

    @PutMapping("/fazendas/{id}")
    public ResponseEntity<String> updateFazenda(@PathVariable Long id, @RequestBody String fazendaJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico1Fazendas().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(fazendaJson);
        return sistemasService.atualizarFazenda(id, request);
    }

    @DeleteMapping("/fazendas/{id}")
    public ResponseEntity<Void> deleteFazenda(@PathVariable Long id) {
        return sistemasService.deletarFazenda(id);
    }

    // --- Métodos Serviço 2 - API Pivôs ---

    @GetMapping("/pivos")
    public ResponseEntity<String> getPivos(){
        return sistemasService.getPivos();
    }

    @GetMapping("/pivos/{id}")
    public ResponseEntity<String> getPivoById(@PathVariable String id) {
        return sistemasService.getPivoById(id);
    }

    @PostMapping("/pivos")
    public ResponseEntity<String> createPivo(@RequestBody String pivoJson){
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico2Pivos().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(pivoJson);
        return sistemasService.salvarPivo(request);
    }

    @PutMapping("/pivos/{id}")
    public ResponseEntity<String> updatePivo(@PathVariable String id, @RequestBody String pivoJson){
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico2Pivos().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(pivoJson);
        return sistemasService.atualizarPivo(id, request);
    }

    @DeleteMapping("/pivos/{id}")
    public ResponseEntity<Void> deletePivo(@PathVariable String id){
        return sistemasService.deletarPivo(id);
    }

    // --- Métodos Serviço 2 - API Históricos ---

    @GetMapping("/historicos")
    public ResponseEntity<String> getHistoricos(){
        return sistemasService.getHistoricos();
    }

    @GetMapping("/historicos/{id}")
    public ResponseEntity<String> getHistoricoById(@PathVariable String id) {
        try {
            return sistemasService.getHistoricoById(id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar histórico: " + e.getMessage());
        }
    }

    @PostMapping("/historicos")
    public ResponseEntity<String> createHistorico(@RequestBody String historicoJson){
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico2Historicos().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(historicoJson);
        return sistemasService.salvarHistorico(request);
    }

    @PutMapping("/historicos/{id}")
    public ResponseEntity<String> updateHistorico(@PathVariable String id, @RequestBody String historicoJson){
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico2Historicos().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(historicoJson);
        return sistemasService.atualizarHistorico(id, request);
    }

    @DeleteMapping("/historicos/{id}")
    public ResponseEntity<Void> deleteHistorico(@PathVariable String id){
        try {
            return sistemasService.deletarHistorico(id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Novos Métodos Serviço 3 - API Plantas ---

    @GetMapping("/plantas")
    public ResponseEntity<String> getPlantas() {
        return sistemasService.getPlantas();
    }

    @GetMapping("/plantas/{id}")
    public ResponseEntity<String> getPlantaById(@PathVariable Long id) {
        return sistemasService.getPlantaById(id);
    }

    @PostMapping("/plantas")
    public ResponseEntity<String> createPlanta(@RequestBody String plantaJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico3Plantas().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(plantaJson);
        return sistemasService.salvarPlanta(request);
    }

    @PutMapping("/plantas/{id}")
    public ResponseEntity<String> updatePlanta(@PathVariable Long id, @RequestBody String plantaJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico3Plantas().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(plantaJson);
        return sistemasService.atualizarPlanta(id, request);
    }

    @DeleteMapping("/plantas/{id}")
    public ResponseEntity<Void> deletePlanta(@PathVariable Long id) {
        return sistemasService.deletarPlanta(id);
    }

    // --- Métodos Serviço 1 - API Guias ---

    @GetMapping("/guias")
    public ResponseEntity<String> getGuias() {
        return sistemasService.getGuias();
    }

    @GetMapping("/guias/{id}")
    public ResponseEntity<String> getGuiaById(@PathVariable Long id) {
        return sistemasService.getGuiaById(id);
    }

    @PostMapping("/guias")
    public ResponseEntity<String> createGuia(@RequestBody String guiaJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico3Guias().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(guiaJson);
        return sistemasService.salvarGuia(request);
    }


    @PutMapping("/guias/{id}")
    public ResponseEntity<String> updateGuia(@PathVariable Long id, @RequestBody String guiaJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico3Guias().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(guiaJson);
        return sistemasService.atualizarGuia(id, request);
    }

    @DeleteMapping("/guias/{id}")
    public ResponseEntity<Void> deleteGuia(@PathVariable Long id) {
        return sistemasService.deletarGuia(id);
    }

    // --- Métodos Serviço 1 - API Sensores ---

    @GetMapping("/sensores")
    public ResponseEntity<String> getSensores() {
        return sistemasService.getSensores();
    }

    @GetMapping("/sensores/{id}")
    public ResponseEntity<String> getSensorById(@PathVariable Long id) {
        return sistemasService.getSensorById(id);
    }

    @PostMapping("/sensores")
    public ResponseEntity<String> createSensor(@RequestBody String sensorJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico3Sensores().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(sensorJson);
        return sistemasService.salvarSensor(request);
    }

    @PutMapping("/sensores/{id}")
    public ResponseEntity<String> updateSensor(@PathVariable Long id, @RequestBody String sensorJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico3Sensores().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(sensorJson);
        return sistemasService.atualizarSensor(id, request);
    }

    @DeleteMapping("/sensores/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        return sistemasService.deletarSensor(id);
    }

    // --- Métodos Serviço 1 - API Mensagens ---

    @GetMapping("/mensagens")
    public ResponseEntity<String> getMensagens() {
        return sistemasService.getMensagens();
    }

    @GetMapping("/mensagens/{id}")
    public ResponseEntity<String> getMensagemById(@PathVariable Long id) {
        return sistemasService.getMensagemById(id);
    }

    @PostMapping("/mensagens")
    public ResponseEntity<String> createMensagem(@RequestBody String mensagemJson) {
        RequestEntity<String> request = RequestEntity
                .post(URI.create(sistemasService.getUrlServico3Mensagens().trim())) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(mensagemJson);
        return sistemasService.salvarMensagem(request);
    }

    @PutMapping("/mensagens/{id}")
    public ResponseEntity<String> updateMensagem(@PathVariable Long id, @RequestBody String mensagemJson) {
        RequestEntity<String> request = RequestEntity
                .put(URI.create(sistemasService.getUrlServico3Mensagens().trim() + "/" + id)) // .trim() adicionado
                .contentType(MediaType.APPLICATION_JSON)
                .body(mensagemJson);
        return sistemasService.atualizarMensagem(id, request);
    }

    @DeleteMapping("/mensagens/{id}")
    public ResponseEntity<Void> deleteMensagem(@PathVariable Long id) {
        return sistemasService.deletarMensagem(id);
    }
}