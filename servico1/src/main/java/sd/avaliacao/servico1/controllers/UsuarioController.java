package sd.avaliacao.servico1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importação necessária para ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sd.avaliacao.servico1.dtos.UsuarioRecordDto;
import sd.avaliacao.servico1.models.Usuario;
import sd.avaliacao.servico1.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;
	
    @CrossOrigin(origins = "http://localhost:8080")
	@GetMapping
	public List<Usuario> getUsuarios(){
		return usuarioService.getUsuarios();
	}
	
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{id}") 
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuario(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

	@CrossOrigin(origins = "http://localhost:8080")
	@PostMapping
	public Usuario salvarUsuario(@RequestBody UsuarioRecordDto usuarioRecordDto) {
		return usuarioService.salvarUsuario(usuarioRecordDto);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping
	public void deletarUsuario(@RequestBody Usuario usuario) {
		usuarioService.excluirUsuario(usuario);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/{id}")
	public Usuario atualizarUsuario(@PathVariable long id, @RequestBody UsuarioRecordDto usuarioDto) {
	    return usuarioService.atualizarUsuario(id, usuarioDto);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping("/{id}")
	public void deletarUsuario(@PathVariable long id) {
	    usuarioService.excluirUsuario(id);
	}
}
