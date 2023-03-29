package br.com.roniebrunhara.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.roniebrunhara.dto.EquipeDTO;
import br.com.roniebrunhara.dto.EquipeResponseDTO;
import br.com.roniebrunhara.entity.Equipe;
import br.com.roniebrunhara.exception.StandardError;
import br.com.roniebrunhara.service.EquipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Api de Equipes")
@RestController
@RequestMapping("/api/v1/equipes")
public class EquipeController {

	@Autowired
	private EquipeService equipeService;
	
	@ApiOperation(value = "Buscar equipe por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Equipe.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@GetMapping("/{id}")
	public ResponseEntity<Equipe> buscarEquipeId(@PathVariable("id") Long id){
		return ResponseEntity.ok().body(equipeService.buscarEquipeId(id));
	}
	
	@ApiOperation(value = "Listar equipes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = EquipeResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@GetMapping
	public ResponseEntity<EquipeResponseDTO> listaEquipes(){
		return ResponseEntity.ok().body(equipeService.listarEquipes());
	}
	
	
	@ApiOperation(value = "Inserir equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = EquipeResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@PostMapping
	public ResponseEntity<Equipe> inserirEquipe(@Valid @RequestBody EquipeDTO dto){
		Equipe equipe = equipeService.inserirEquipe(dto);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(equipe.getId()).toUri();
		return ResponseEntity.created(location).body(equipe);
	}
	
	@ApiOperation(value = "Alterar equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No content", response = Void.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@PutMapping("/{id}")
	public ResponseEntity<Void> alterarEquipe(@PathVariable("id") Long id, @Valid @RequestBody EquipeDTO dto){
		equipeService.alterarEquipe(id,dto);
		return ResponseEntity.noContent().build();
	}
	
}
