package br.com.roniebrunhara.task;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.roniebrunhara.service.ScrapingService;
import br.com.roniebrunhara.util.DataUtil;

@Configuration
@EnableScheduling
public class PartidaTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartidaTask.class);
	private static final String TIME_ZONE = "America/Sao_Paulo";
	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
	
	@Autowired
	private ScrapingService scrapingService;
	
	//Teste
	@Scheduled(cron="0/30 * 19-23 * * MON-FRI", zone = TIME_ZONE)
	public void taskPartidaTercaFeira() {
		inicializaAgendamento("taskPartidaTercaFeira()");
	}
	
	@Scheduled(cron="0/30 * 19-23 * * WED", zone = TIME_ZONE)
	public void taskPartidaQuartaFeira() {
		inicializaAgendamento("taskPartidaQuartaFeira()");
	}
	
	@Scheduled(cron="0/30 * 19-23 * * THU", zone = TIME_ZONE)
	public void taskPartidaQuintaFeira() {
		inicializaAgendamento("taskPartidaQuintaFeira()");
	}
	
	@Scheduled(cron="0/30 * 16-23 * * SAT", zone = TIME_ZONE)
	public void taskPartidaSabado() {
		inicializaAgendamento("taskPartidaSabado()");
	}
	
	@Scheduled(cron="0/30 * 11-13 * * SUN", zone = TIME_ZONE)
	public void taskPartidaDomingoManha() {
		inicializaAgendamento("taskPartidaDomingoManha()");
	}
	
	@Scheduled(cron="0/30 * 16-23 * * SUN", zone = TIME_ZONE)
	public void taskPartidaDomingoTarde() {
		inicializaAgendamento("taskPartidaDomingoTarde()");
	}
	
	private void inicializaAgendamento(String diaSemana){
		this.gravaLogInfo(String.format("%s : %s", diaSemana,DataUtil.formatarDateEmString(new Date(),DD_MM_YYYY_HH_MM_SS)));
		scrapingService.verificaPartidaPeriodo();
	}
	
	private void gravaLogInfo(String mensagem) {
		LOGGER.info(mensagem);
	}
	
	
}
