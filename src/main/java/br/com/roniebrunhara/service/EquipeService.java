package br.com.roniebrunhara.service;


import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.roniebrunhara.dto.EquipeDTO;
import br.com.roniebrunhara.dto.EquipeResponseDTO;
import br.com.roniebrunhara.entity.Equipe;
import br.com.roniebrunhara.exception.BadRequestException;
import br.com.roniebrunhara.exception.NotFoundException;
import br.com.roniebrunhara.repository.EquipeRepository;

@Service
public class EquipeService {
	
	@Autowired
	private EquipeRepository equipeRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public Equipe buscarEquipeId(Long id) {
		// TODO Auto-generated method stub
		return equipeRepository.findById(id)
				.orElseThrow(()->new NotFoundException("Nenhuma equipe encontrada com o id informado: "+id));
	}
	
	public Equipe buscarEquipePorNome(String nomeEquipe) {
		return equipeRepository.findByNomeEquipe(nomeEquipe)
				.orElseThrow(()-> new NotFoundException("Nenhuma equipe encontrada com o nome informado: "+nomeEquipe));
	}

	public EquipeResponseDTO listarEquipes() {
		EquipeResponseDTO equipes = new EquipeResponseDTO();
		equipes.setEquipes(equipeRepository.findAll());
		return equipes;
	}

	public Equipe inserirEquipe(EquipeDTO dto) {
		boolean exist = equipeRepository.existsByNomeEquipe(dto.getNomeEquipe());
		if(exist) {
			throw new BadRequestException("Já existe uma equipe cadastrada com o nome informado.");
		}else {
			Equipe equipe = modelMapper.map(dto, Equipe.class);
			return equipeRepository.save(equipe);
		}
	}

	public void alterarEquipe(Long id, EquipeDTO dto) {
		boolean exist = equipeRepository.existsById(id);
		if(!exist) {
			throw new BadRequestException("Não foi possivel alterar a equipe: ID inexistente");
		}
		Equipe equipe = modelMapper.map(dto, Equipe.class);
		equipe.setId(id);
		equipeRepository.save(equipe);
		
	}

}
