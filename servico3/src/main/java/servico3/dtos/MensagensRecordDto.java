package servico3.dtos;

public record MensagensRecordDto(
	    Long id_mensagem, 
	    String tipo_mensagem,
	    String conteudo_mensagem,
	    String id_pivo 
	) {}
