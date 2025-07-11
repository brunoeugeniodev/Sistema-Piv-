package sd.avaliacao.servico1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sd.avaliacao.servico1.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
