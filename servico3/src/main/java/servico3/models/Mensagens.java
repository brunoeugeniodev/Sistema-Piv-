package servico3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable; 

@Entity
@Table(name = "mensagens")
public class Mensagens implements Serializable { 
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_mensagem; 
	
	@Column(nullable = false)
	private String tipo_mensagem;
	
	@Column(nullable = false)
	private String conteudo_mensagem;
	
	@Column(nullable = false)
	private String id_pivo; 

    public Long getId_mensagem() {
        return id_mensagem;
    }

    public void setId_mensagem(Long id_mensagem) {
        this.id_mensagem = id_mensagem;
    }

    public String getTipo_mensagem() {
        return tipo_mensagem;
    }

    public void setTipo_mensagem(String tipo_mensagem) {
        this.tipo_mensagem = tipo_mensagem;
    }

    public String getConteudo_mensagem() {
        return conteudo_mensagem;
    }

    public void setConteudo_mensagem(String conteudo_mensagem) {
        this.conteudo_mensagem = conteudo_mensagem;
    }

    public String getId_pivo() {
        return id_pivo;
    }

    public void setId_pivo(String id_pivo) {
        this.id_pivo = id_pivo;
    }
}
