package sd.avaliacao.servico1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import sd.avaliacao.servico1.dtos.FazendaRecordDto;
import sd.avaliacao.servico1.models.Fazenda;
import sd.avaliacao.servico1.repositories.FazendaRepository;

@Service
public class FazendaService {
	private final FazendaRepository fazendaRepository;
	
	public FazendaService(FazendaRepository fazendaRepository) {
		this.fazendaRepository = fazendaRepository;
	}
	
	public List<Fazenda> getFazendas(){
		return fazendaRepository.findAll();
	}
	
	public Fazenda getFazenda(Long id) {
		return fazendaRepository.findById(id)
		    .orElseThrow(() -> new RuntimeException("Fazenda não encontrada com ID: " + id)); 
	}
	
	public Fazenda salvarFazenda(FazendaRecordDto fazendaRecordDto) {
		Fazenda fazenda = new Fazenda();
		fazenda.setData_registro(fazendaRecordDto.data_registro());
		fazenda.setLocalizacao(fazendaRecordDto.localizacao());
		fazenda.setNome_fazenda(fazendaRecordDto.nome_fazenda());	
		return fazendaRepository.save(fazenda);
	}
	
	public void excluirFazenda(Fazenda fazenda) {
		fazendaRepository.deleteById(fazenda.getId_fazenda());
	}
	
	public void excluirFazenda(Long id) {
	    if (fazendaRepository.existsById(id)) {
	        fazendaRepository.deleteById(id);
	    } else {
	        throw new RuntimeException("Fazenda não encontrada com ID: " + id);
	    }
	}
	
	public Fazenda atualizarFazenda(Long id, FazendaRecordDto fazendaDto) {
	    Fazenda fazendaExistente = fazendaRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Fazenda não encontrada com ID: " + id));

	    fazendaExistente.setNome_fazenda(fazendaDto.nome_fazenda());
	    fazendaExistente.setLocalizacao(fazendaDto.localizacao());
	    fazendaExistente.setData_registro(fazendaDto.data_registro());

	    return fazendaRepository.save(fazendaExistente);
	}
}
