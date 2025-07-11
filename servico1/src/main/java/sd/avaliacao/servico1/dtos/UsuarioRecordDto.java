package sd.avaliacao.servico1.dtos;

public record UsuarioRecordDto(
    Long id_usuario,
    String nome,
    String email,
    String senha,
    String telefone,
    Long id_fazenda
) {}
