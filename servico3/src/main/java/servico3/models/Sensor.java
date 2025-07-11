package servico3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "sensor")
public class Sensor implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_sensor; 
	
	@Column(nullable = false)
	private String tipo_sensor;
	
	@Column(nullable = false)
	private String status_sensor;
	
	@Column(nullable = false)
	private String id_pivo; 
	
    public Long getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(Long id_sensor) {
        this.id_sensor = id_sensor;
    }

    public String getTipo_sensor() {
        return tipo_sensor;
    }

    public void setTipo_sensor(String tipo_sensor) {
        this.tipo_sensor = tipo_sensor;
    }

    public String getStatus_sensor() {
        return status_sensor;
    }

    public void setStatus_sensor(String status_sensor) {
        this.status_sensor = status_sensor;
    }

    public String getId_pivo() {
        return id_pivo;
    }

    public void setId_pivo(String id_pivo) {
        this.id_pivo = id_pivo;
    }
}
