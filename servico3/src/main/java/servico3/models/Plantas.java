package servico3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "plantas")
public class Plantas implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id_planta;
	
	@Column(nullable = false)
	private String nome_area;
	
	@Column(nullable = false)
	private Long id_fazenda; 

    public Long getId_planta() {
        return id_planta;
    }

    public void setId_planta(Long id_planta) {
        this.id_planta = id_planta;
    }

    public String getNome_area() {
        return nome_area;
    }

    public void setNome_area(String nome_area) {
        this.nome_area = nome_area;
    }

    public Long getId_fazenda() {
        return id_fazenda;
    }

    public void setId_fazenda(Long id_fazenda) {
        this.id_fazenda = id_fazenda;
    }
}
