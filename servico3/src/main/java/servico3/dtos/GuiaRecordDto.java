package servico3.dtos;

public record GuiaRecordDto(
	    Long id_guia, 
	    String quantidade_agua, 
	    String tempo_descanso,
	    Long id_planta 
	) {}
