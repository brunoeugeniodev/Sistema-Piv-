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

import servico3.dtos.MensagensRecordDto;
import servico3.models.Mensagens;
import servico3.services.MensagensService;

@RestController
@RequestMapping("/api/mensagens")
public class MensagensController {

    @Autowired
    private MensagensService mensagensService;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping
    public List<Mensagens> getMensagens() {
        return mensagensService.getMensagens();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{id}")
    public ResponseEntity<Mensagens> getMensagensById(@PathVariable Long id) {
        try {
            Mensagens mensagens = mensagensService.getMensagens(id);
            return ResponseEntity.ok(mensagens);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping
    public ResponseEntity<Mensagens> salvarMensagens(@RequestBody MensagensRecordDto mensagensDto) {
        try {
            Mensagens novaMensagem = mensagensService.salvarMensagens(mensagensDto);
            return ResponseEntity.status(201).body(novaMensagem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMensagens(@PathVariable Long id) {
        try {
            mensagensService.excluirMensagens(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PutMapping("/{id}")
    public ResponseEntity<Mensagens> atualizarMensagens(@PathVariable Long id, @RequestBody MensagensRecordDto mensagensDto) {
        try {
            Mensagens mensagemAtualizada = mensagensService.atualizarMensagens(id, mensagensDto);
            return ResponseEntity.ok(mensagemAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
