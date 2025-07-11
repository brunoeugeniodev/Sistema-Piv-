package sd.avaliacao.servico1.services;

import java.util.List;
import java.util.Objects; // Importação necessária para Objects.equals()

import org.springframework.stereotype.Service;

import sd.avaliacao.servico1.dtos.UsuarioRecordDto;
import sd.avaliacao.servico1.models.Fazenda; // Importar Fazenda
import sd.avaliacao.servico1.models.Usuario;
import sd.avaliacao.servico1.repositories.UsuarioRepository;
import sd.avaliacao.servico1.repositories.FazendaRepository; // Importar FazendaRepository


@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final FazendaRepository fazendaRepository; 
	
    public UsuarioService(UsuarioRepository usuarioRepository, FazendaRepository fazendaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.fazendaRepository = fazendaRepository;
    }
	
    public List<Usuario> getUsuarios(){
        return usuarioRepository.findAll();
    }
	
    public Usuario salvarUsuario(UsuarioRecordDto usuarioRecordDto){ 
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRecordDto.nome());
        usuario.setEmail(usuarioRecordDto.email());
        usuario.setSenha(usuarioRecordDto.senha());
        usuario.setTelefone(usuarioRecordDto.telefone());
        if (usuarioRecordDto.id_fazenda() == null) {
            throw new RuntimeException("ID da Fazenda é obrigatório para salvar o usuário.");
        }
        Fazenda fazenda = fazendaRepository.findById(usuarioRecordDto.id_fazenda())
            .orElseThrow(() -> new RuntimeException("Fazenda não encontrada para o ID: " + usuarioRecordDto.id_fazenda()));
        
        usuario.setFazenda(fazenda); 
        
        return usuarioRepository.save(usuario);
    }
    
    public Usuario getUsuario(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }
	
    public void excluirUsuario(Usuario usuario) {
        usuarioRepository.deleteById(usuario.getId_usuario());
    }
	
    public Usuario atualizarUsuario(long id, UsuarioRecordDto usuarioDto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuarioExistente.setNome(usuarioDto.nome());
        usuarioExistente.setEmail(usuarioDto.email());
        usuarioExistente.setSenha(usuarioDto.senha());
        usuarioExistente.setTelefone(usuarioDto.telefone());
        Long idFazendaExistente = (usuarioExistente.getFazenda() != null) ? usuarioExistente.getFazenda().getId_fazenda() : null;

        if (usuarioDto.id_fazenda() != null && !Objects.equals(idFazendaExistente, usuarioDto.id_fazenda())) {
            Fazenda novaFazenda = fazendaRepository.findById(usuarioDto.id_fazenda())
                .orElseThrow(() -> new RuntimeException("Nova Fazenda não encontrada para o ID: " + usuarioDto.id_fazenda()));
            usuarioExistente.setFazenda(novaFazenda);
        }

        return usuarioRepository.save(usuarioExistente);
    }
	
    public void excluirUsuario(long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }
}
