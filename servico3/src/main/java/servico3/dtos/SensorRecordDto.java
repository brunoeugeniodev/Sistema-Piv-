package servico3.dtos;

public record SensorRecordDto(
	    Long id_sensor,
	    String status_sensor,
	    String tipo_sensor,
	    String id_pivo
	) {}
