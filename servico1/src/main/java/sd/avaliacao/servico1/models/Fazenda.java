package sd.avaliacao.servico1.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fazenda")
public class Fazenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id_fazenda;
	
	@Column(nullable = false)
	private String nome_fazenda;
	
	@Column(nullable = false)
	private String localizacao;
	
	@Column(nullable = false)
	private String data_registro;

	@OneToMany(mappedBy = "fazenda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Usuario> usuarios = new HashSet<>();
	
	public long getId_fazenda() {
		return id_fazenda;
	}

	public void setId_fazenda(long id_fazenda) {
		this.id_fazenda = id_fazenda;
	}

	public String getNome_fazenda() {
		return nome_fazenda;
	}

	public void setNome_fazenda(String nome_fazenda) {
		this.nome_fazenda = nome_fazenda;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getData_registro() {
		return data_registro;
	}

	public void setData_registro(String data_registro) {
		this.data_registro = data_registro;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
