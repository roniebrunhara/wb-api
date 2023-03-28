package br.com.roniebrunhara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.roniebrunhara.dto.EquipeResponseDTO;
import br.com.roniebrunhara.entity.Equipe;
import br.com.roniebrunhara.exception.NotFoundException;
import br.com.roniebrunhara.repository.EquipeRepository;

@Service
public class EquipeService {
	
	@Autowired
	private EquipeRepository equipeRepository;

	public Equipe buscarEquipeId(Long id) {
		// TODO Auto-generated method stub
		return equipeRepository.findById(id)
				.orElseThrow(()->new NotFoundException("Nenhuma equipe encontrada com o id informado: "+id));
	}

	public EquipeResponseDTO listarEquipes() {
		EquipeResponseDTO equipes = new EquipeResponseDTO();
		equipes.setEquipes(equipeRepository.findAll());
		return equipes;
	}

}
