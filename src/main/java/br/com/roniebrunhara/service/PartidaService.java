package br.com.roniebrunhara.service;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.roniebrunhara.dto.PartidaDTO;
import br.com.roniebrunhara.dto.PartidaGoogleDTO;
import br.com.roniebrunhara.dto.PartidaResponseDTO;
import br.com.roniebrunhara.entity.Partida;
import br.com.roniebrunhara.exception.NotFoundException;
import br.com.roniebrunhara.repository.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository partidaRepository;
	
	@Autowired
	private ModelMapper modelpMapper;
	
	@Autowired
	private EquipeService equipeService;
	
	public Partida buscarPartidaPorID(Long id) {
		return partidaRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("Nenhuma partida foi encontrado com id informado: "+id));
	}
	
	public PartidaResponseDTO listarPartidas() {
		PartidaResponseDTO partidas = new PartidaResponseDTO();
		partidas.setPartidas(partidaRepository.findAll());
		return partidas;
	}

	public Partida inserirPartida(PartidaDTO dto) {
		Partida partida = modelpMapper.map(dto, Partida.class);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		return salvarPartida(partida);
	}

	public void alterarPartida(Long id, PartidaDTO dto) {
		boolean exist = partidaRepository.existsById(id);
		if(!exist) {
			throw new NotFoundException("Não foi possivel atualizar a partida: ID inexistente");
		}
		Partida partida = buscarPartidaPorID(id);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		partida.setDataHoraPartida(dto.getDataHoraPartida());
		partida.setLocalPartida(dto.getLocalPartida());
	
		salvarPartida(partida);
	}

	private Partida salvarPartida(Partida partida) {
		return partidaRepository.save(partida);
	}

	public void atualizaPartida(Partida partida, PartidaGoogleDTO partidaGoogle) {
		partida.setPlacarEquipeCasa(partidaGoogle.getPlacarEquipeCasa());
		partida.setPlacarEquipeVisitante(partidaGoogle.getPlacarEquipeVisitante());
		partida.setGolsEquipeCasa(partidaGoogle.getGolsEquipeCasa());
		partida.setGolsEquipeVisitante(partidaGoogle.getGolsEquipeVisitante());
		partida.setPlacarEstendidoEquipeCasa(partidaGoogle.getPlacarEstendidoEquipeCasa());
		partida.setPlacarEstendidoEquipeVisitante(partidaGoogle.getPlacarEstendidoEquipeVisitante());
		partida.setTempoPartida(partidaGoogle.getTempoPartida());
		salvarPartida(partida);
		
	}

	public List<Partida> listarPartidasPeriodo() {
		
		return partidaRepository.listarPartidasPeriodo();
	}

	public Integer buscarQuantidadePartidasPeriodo() {
		
		return partidaRepository.buscarQuantidadePartidasPeriodo();
	}
}
