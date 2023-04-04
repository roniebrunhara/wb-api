package br.com.roniebrunhara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.roniebrunhara.dto.PartidaGoogleDTO;
import br.com.roniebrunhara.entity.Partida;
import br.com.roniebrunhara.util.ScrapingUtil;
import br.com.roniebrunhara.util.StatusPartida;
//Verificar hibernate nao esta criando as tabelas
@Service
public class ScrapingService {
	
	@Autowired
	private ScrapingUtil scrapingUtil;
	
	@Autowired
	private PartidaService partidaService;
	
	public void verificaPartidaPeriodo() {
		Integer quantidadePartida = partidaService.buscarQuantidadePartidasPeriodo();

		if(quantidadePartida > 0) {
			List<Partida> partidas = partidaService.listarPartidasPeriodo();


			partidas.forEach(partida ->{
				String urlPartida = scrapingUtil.montarUrlGoogle(
						partida.getEquipeCasa().getNomeEquipe(),
						partida.getEquipeVisitante().getNomeEquipe());
				
				PartidaGoogleDTO partidaGoogle = scrapingUtil.obtemInfoPartida(urlPartida);
				if(partidaGoogle.getStatusPartida() != StatusPartida.PARTIDA_NAO_INICIADA) {
					partidaService.atualizaPartida(partida,partidaGoogle);
				}
			});
		}
	}

}
