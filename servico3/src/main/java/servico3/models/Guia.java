package servico3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable; 

@Entity
@Table(name = "guia")
public class Guia implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_guia; 

	@Column(nullable = false)
	private double quantidade_agua;
	
	@Column(nullable = false)
	private String tempo_descanso;
	
	@ManyToOne 
	@JoinColumn(name = "id_planta_fk", nullable = false) 
	private Plantas planta; 

    public Long getId_guia() {
        return id_guia;
    }

    public void setId_guia(Long id_guia) {
        this.id_guia = id_guia;
    }

    public double getQuantidade_agua() {
        return quantidade_agua;
    }

    public void setQuantidade_agua(double quantidade_agua) {
        this.quantidade_agua = quantidade_agua;
    }

    public String getTempo_descanso() {
        return tempo_descanso;
    }

    public void setTempo_descanso(String tempo_descanso) {
        this.tempo_descanso = tempo_descanso;
    }

    public Plantas getPlanta() {
        return planta;
    }

    public void setPlanta(Plantas planta) {
        this.planta = planta;
    }
}
