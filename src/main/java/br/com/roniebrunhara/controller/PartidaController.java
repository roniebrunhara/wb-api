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

import br.com.roniebrunhara.dto.PartidaDTO;
import br.com.roniebrunhara.dto.PartidaResponseDTO;
import br.com.roniebrunhara.entity.Equipe;
import br.com.roniebrunhara.entity.Partida;
import br.com.roniebrunhara.exception.StandardError;
import br.com.roniebrunhara.service.PartidaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Api de Partida")
@RestController
@RequestMapping("/api/v1/partidas")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;
	
	@ApiOperation(value = "Buscar partida por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Partida.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@GetMapping("/{id}")
	public ResponseEntity<Partida> buscarPartidaPorId(@PathVariable("id") Long id){
		return ResponseEntity.ok().body(partidaService.buscarPartidaPorID(id));
	}
	
	@ApiOperation(value = "Listar partidas")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = PartidaResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@GetMapping
	public ResponseEntity<PartidaResponseDTO> listarPartidas(){
		return ResponseEntity.ok().body(partidaService.listarPartidas());
	}
	
	@ApiOperation(value = "Inserir partida")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Partida.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	@PostMapping
	public ResponseEntity<Partida> inserirPartida(@Valid @RequestBody PartidaDTO dto){
		Partida partida = partidaService.inserirPartida(dto);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(partida.getId(),Partida.class).toUri();
		
		return ResponseEntity.created(location).body(partida);
	}
	
	@ApiOperation(value = "Alterar partida")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Void.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Service Error", response = StandardError.class)


	})
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> alterarPartida(@PathVariable("id") Long id, @Valid @RequestBody PartidaDTO dto){
		partidaService.alterarPartida(id,dto);
		return ResponseEntity.noContent().build();
	}
}
