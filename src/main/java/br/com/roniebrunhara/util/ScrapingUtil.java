package br.com.roniebrunhara.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.roniebrunhara.dto.PartidaGoogleDTO;

public class ScrapingUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	
	private static final String BASE_URL = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO = "&hl=pt-BR";
	
	public static void main(String[] args) {
		String url = BASE_URL+"palmeiras+x+corinthians+08/08/2020"+COMPLEMENTO;
		ScrapingUtil scraping = new ScrapingUtil();
		scraping.obtemInfoPartida(url);
	}

	public PartidaGoogleDTO obtemInfoPartida(String url) {
		
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			String title = document.title();
			LOGGER.info("Titulo da pagina: {}",title);
		} catch (IOException e) {
			LOGGER.error("Error ao tentar conectar com Jsoup na url - {} - message -> {}",url,e.getMessage());
		}
		return null;
	}
}
