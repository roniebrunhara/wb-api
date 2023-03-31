package br.com.roniebrunhara.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.roniebrunhara.dto.PartidaGoogleDTO;

@Service
public class ScrapingUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO = "&hl=pt-BR";
	private static final String CASA = "casa";
	private static final String VISITANTE = "visitante";
	private static final String SRC = "src";
	private static final String SPAN = "span";
	private static final String PENALTIS = "Pênaltis";
	
	private static final String DIV_PLACAR_EQUIPE_CASA = "div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]";
	private static final String DIV_PLACAR_EQUIPE_VISITANTE = "div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]";
	
	
	private static final String DIV_PENALIDADE = "div[class=imso_mh_s__psn-sc]";
	private static final String DIV_GOLS_EQUIPE_CASA      = "div[class=imso_gs__tgs imso_gs__left-team]";
	private static final String DIV_GOLS_EQUIPE_VISITANTE = "div[class=imso_gs__tgs imso_gs__right-team]";
	private static final String DIV_ITEM_GOL = "div[class=imso_gs__gs-r]";
	
	private static final String DIV_DADOS_EQUIPE_CASA = "div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]";
	private static final String DIV_DADOS_EQUIPE_VISITANTE = "div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]";
	
	private static final String ITEM_LOGO = "img[class=imso_btl__mh-logo]";
	private static final String DIV_PARTIDA_ANDAMENTO = "div[class=imso_mh__lv-m-stts-cont]";
	private static final String DIV_PARTIDA_ENCERRADA = "span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]";

	public PartidaGoogleDTO obtemInfoPartida(String url) {
		PartidaGoogleDTO partidaGoogle = new PartidaGoogleDTO();
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			String title = document.title();
			LOGGER.info("Titulo da pagina: {}",title);
			StatusPartida statusPartida = obtemStatusPartida(document);
			LOGGER.info("Status Partida: {}",statusPartida.toString());
			partidaGoogle.setStatusPartida(statusPartida.toString());
			
			if(statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
				String tempoPartida = obtemTempoPartida(document);
				LOGGER.info("Tempo Partida: {}",tempoPartida);
				partidaGoogle.setTempoPartida(tempoPartida);
				
				Integer placarEquipeCasa = recuperaPlacarEquipe(document,DIV_PLACAR_EQUIPE_CASA);
				LOGGER.info("placar Equipe Casa: {}",placarEquipeCasa);
				partidaGoogle.setPlacarEquipeCasa(placarEquipeCasa);
				
				Integer placarEquipeVisitante = recuperaPlacarEquipe(document,DIV_PLACAR_EQUIPE_VISITANTE);
				LOGGER.info("placar Equipe Visitante: {}",placarEquipeVisitante);
				partidaGoogle.setPlacarEquipeVisitante(placarEquipeVisitante);
				
				String golsEquipeCasa = recuperaGolsEquipe(document,DIV_GOLS_EQUIPE_CASA);
				LOGGER.info("Gols Equipe casa: {}",golsEquipeCasa);
				partidaGoogle.setGolsEquipeCasa(golsEquipeCasa);
				
				String golsEquipeVisitante = recuperaGolsEquipe(document,DIV_GOLS_EQUIPE_VISITANTE);
				LOGGER.info("Gols Equipe visitante: {}",golsEquipeVisitante);
				partidaGoogle.setGolsEquipeVisitante(golsEquipeVisitante);
				
				Integer placaEstendidoCasa      = buscaPenalidades(document,CASA);
				LOGGER.info("Placar estendido Equipe casa: {}",placaEstendidoCasa);
				partidaGoogle.setPlacarEstendidoEquipeCasa(placaEstendidoCasa);
				
				Integer placaEstendidoVisitante = buscaPenalidades(document,VISITANTE);
				LOGGER.info("Placar estendido Equipe visitante: {}",placaEstendidoVisitante);
				partidaGoogle.setPlacarEstendidoEquipeVisitante(placaEstendidoVisitante);
			}
			
			String nomeEquipeCasa = recuperaNomeEquipe(document,DIV_DADOS_EQUIPE_CASA);
			LOGGER.info("Nome equipe Casa: {}",nomeEquipeCasa);
			partidaGoogle.setNomeEquipeCasa(nomeEquipeCasa);
			
			String nomeEquipeVisitante = recuperaNomeEquipe(document,DIV_DADOS_EQUIPE_VISITANTE);
			LOGGER.info("Nome equipe Visitante: {}",nomeEquipeVisitante);
			partidaGoogle.setNomeEquipeVisitante(nomeEquipeVisitante);
			
			String urlLogoEquipeCasa = recuperaLogoEquipe(document,DIV_DADOS_EQUIPE_CASA);
			LOGGER.info("url Logo equipe casa: {}",urlLogoEquipeCasa);
			partidaGoogle.setUrlLogoEquipeCasa(urlLogoEquipeCasa);
			
			String urlLogoEquipeVisitante = recuperaLogoEquipe(document,DIV_DADOS_EQUIPE_VISITANTE);
			LOGGER.info("url Logo equipe visitate: {}",urlLogoEquipeVisitante);
			partidaGoogle.setUrlLogoEquipeVisitante(urlLogoEquipeVisitante);
			
			return partidaGoogle;
			
		} catch (IOException e) {
			LOGGER.error("Error ao tentar conectar com Jsoup na url - {} - message -> {}",url,e.getMessage());
		}
		return null;
	}

	public StatusPartida obtemStatusPartida(Document document) {
		/*Situaçoes 
		 * 1 - partida nao iniciada
		 * 2 - partida iniciada
		 * 3 - partida encerrada
		 * 4 - penalidades
		 */
		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
		
		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		
		if(!isTempoPartida) {
			String tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
			
			if(tempoPartida.contains(PENALTIS)) {
				statusPartida = StatusPartida.PARTIDA_PENALTIS;
			}
			//LOGGER.info(tempoPartida);
		}	
		
		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		
		if(!isTempoPartida) {
			//Class mapeada quando vem como encerrado!
			String tempoPartida = document.select(DIV_PARTIDA_ENCERRADA).first().text();
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
			//LOGGER.info(statusPartida.toString());
		}	
		
		return statusPartida;
	}
	public String obtemTempoPartida(Document document) {
		String tempoPartida = null;
		//Jogo rolando ou intervalo ou penalidades
		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		
		if(!isTempoPartida) {
			tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
		}
		
		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		
		if(!isTempoPartida) {
			tempoPartida = document.select(DIV_PARTIDA_ENCERRADA).first().text();
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
	
	public String recuperaNomeEquipe(Document document, String itemHtml) {
		Element element = document.selectFirst(itemHtml);
		String nomeEquipe = element.select(SPAN).text();
		return nomeEquipe;
	}
	
	
	public String recuperaLogoEquipe(Document document, String itemHtml) {
		Element element = document.selectFirst(itemHtml);
		String urlLogo = element.select(ITEM_LOGO).attr(SRC);
		return urlLogo;
	}
	
	public Integer recuperaPlacarEquipe(Document document, String itemHtml) {
		String placarEquipe = document.selectFirst(itemHtml).text();
		return formataPlacarStringToInteger(placarEquipe);
	}
	
	public String recuperaGolsEquipe(Document document, String itemHtml) {
		List<String> golsEquipe = new ArrayList<>();
		Elements elements = document.select(itemHtml)
				.select(DIV_ITEM_GOL);
		
		elements.forEach(e ->{
			String infoGol = e.select(DIV_ITEM_GOL).text();
			golsEquipe.add(infoGol);
		});
			
		
		return String.join(", ", golsEquipe);
	}
	
	public Integer buscaPenalidades(Document document, String tipoEquipe) {
		boolean isPenalidades = document.select(DIV_PENALIDADE).isEmpty();
		if(!isPenalidades) {
			String penalidades = document.select(DIV_PENALIDADE).text();
			String penalidadeCompleta = penalidades.substring(0,5).replace(" ", "");
			String[] divisao = penalidadeCompleta.split("-");
			return CASA.equals(tipoEquipe)?formataPlacarStringToInteger(divisao[0]):formataPlacarStringToInteger(divisao[1]);
		}
		
		return null;
	}
	
	public Integer formataPlacarStringToInteger(String placar) {
		Integer valor;
		try {
			valor = Integer.parseInt(placar);
		} catch (Exception e) {
			valor = 0;
		}
		return valor;
	}
	
	public String montarUrlGoogle(String nomeEquipeCasa,String nomeEquipeVisitante) {
		try {
			String equipeCasa = nomeEquipeCasa.replace(" ","+").replace("-", "+");
			String equipeVisitante = nomeEquipeVisitante.replace(" ","+").replace("-", "+");
			
			return BASE_URL+equipeCasa+"+x+"+equipeVisitante+COMPLEMENTO;
			
		}catch (Exception e) {
			LOGGER.error("Error: {} ",e.getMessage());
		}
		
		return null;
	}
	
	public String montarUrlGoogleComData(String nomeEquipeCasa,String nomeEquipeVisitante,String data) {
		try {
			String equipeCasa = nomeEquipeCasa.replace(" ","+").replace("-", "+");
			String equipeVisitante = nomeEquipeVisitante.replace(" ","+").replace("-", "+");
			
			return BASE_URL+equipeCasa+"+x+"+equipeVisitante+data+COMPLEMENTO;
			
		}catch (Exception e) {
			LOGGER.error("Error: {} ",e.getMessage());
		}
		
		return null;
	}
}
