package br.com.roniebrunhara.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.roniebrunhara.dto.PartidaGoogleDTO;

public class ScrapingUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO = "&hl=pt-BR";
	
	public static void main(String[] args) {
		String vaiAcontecer = "vasco+x+flamengo+05/03/23";
		String jogoEncerrado = "palmeiras+x+corinthians+08/08/2020"; 
		String url = BASE_URL+vaiAcontecer+COMPLEMENTO;
		ScrapingUtil scraping = new ScrapingUtil();
		scraping.obtemInfoPartida(url);
	}

	public PartidaGoogleDTO obtemInfoPartida(String url) {
		PartidaGoogleDTO partidaGoogleDTO = new PartidaGoogleDTO();
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			String title = document.title();
			LOGGER.info("Titulo da pagina: {}",title);
			StatusPartida statusPartida = obtemStatusPartida(document);
			LOGGER.info("Status Partida: {}",statusPartida.toString());
			
			if(statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
				String tempoPartida = obtemTempoPartida(document);
				LOGGER.info("Tempo Partida: {}",tempoPartida);
			}
			
			String nomeEquipeCasa = recuperaNomeEquipeCasa(document);
			LOGGER.info("Nome equipe Casa: {}",nomeEquipeCasa);
			
			String nomeEquipeVisitante = recuperaNomeEquipeVisitante(document);
			LOGGER.info("Nome equipe Visitante: {}",nomeEquipeVisitante);
			
			String urlLogoEquipeCasa = recuperaLogoEquipeCasa(document);
			LOGGER.info("url Logo equipe casa: {}",urlLogoEquipeCasa);
			
			String urlLogoEquipeVisitante = recuperaLogoEquipeVisitante(document);
			LOGGER.info("url Logo equipe visitate: {}",urlLogoEquipeVisitante);
			
		} catch (IOException e) {
			LOGGER.error("Error ao tentar conectar com Jsoup na url - {} - message -> {}",url,e.getMessage());
		}
		return partidaGoogleDTO;
	}

	public StatusPartida obtemStatusPartida(Document document) {
		/*Situaçoes 
		 * 1 - partida nao iniciada
		 * 2 - partida iniciada
		 * 3 - partida encerrada
		 * 4 - penalidades
		 */
		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
		
		boolean isTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		
		if(!isTempoPartida) {
			String tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
			
			if(tempoPartida.contains("Pênaltis")) {
				statusPartida = StatusPartida.PARTIDA_PENALTIS;
			}
			//LOGGER.info(tempoPartida);
		}	
		
		isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		
		if(!isTempoPartida) {
			//Class mapeada quando vem como encerrado!
			String tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
			//LOGGER.info(statusPartida.toString());
		}	
		
		return statusPartida;
	}
	public String obtemTempoPartida(Document document) {
		String tempoPartida = null;
		//Jogo rolando ou intervalo ou penalidades
		boolean isTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		
		if(!isTempoPartida) {
			tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
		}
		
		isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		
		if(!isTempoPartida) {
			tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
		}
		
		return corrigeTempoPartida(tempoPartida);
	}
	
	public String corrigeTempoPartida(String tempo) {
		if(tempo.contains("'")) {
			//Substituir para regex
			return tempo.replace(" ","").replace("'", " min");
		}else {
			return tempo;
		}
	}
	
	public String recuperaNomeEquipeCasa(Document document) {
		Element element = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = element.select("span").text();
		return nomeEquipe;
	}
	
	public String recuperaNomeEquipeVisitante(Document document) {
		Element element = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = element.select("span").text();
		return nomeEquipe;
	}
	
	public String recuperaLogoEquipeCasa(Document document) {
		Element element = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo = element.select("img[class=imso_btl__mh-logo]").attr("src");
		return urlLogo;
	}
	
	public String recuperaLogoEquipeVisitante(Document document) {
		Element element = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo = element.select("img[class=imso_btl__mh-logo]").attr("src");
		return urlLogo;
	}
}
