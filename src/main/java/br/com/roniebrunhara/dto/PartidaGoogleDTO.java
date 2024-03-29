package br.com.roniebrunhara.dto;

import java.io.Serializable;

import br.com.roniebrunhara.util.StatusPartida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Com o lombok não precisamos gerar os get set manualmente, apenas pela notacao
//Com o lombok construtores vazio NoArgsConstructor
//com o lombok contrutores com todos os atributos
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaGoogleDTO implements Serializable{
	private static final long serialVersionUID = 1;
	
	private StatusPartida statusPartida;
	private String tempoPartida;
	
	//Informações da equipe CASA
	private String nomeEquipeCasa;
	private String urlLogoEquipeCasa;
	private Integer placarEquipeCasa;
	private String golsEquipeCasa;
	private Integer placarEstendidoEquipeCasa;

	//Informações da equipe Visitante
	private String nomeEquipeVisitante;
	private String urlLogoEquipeVisitante;
	private Integer placarEquipeVisitante;
	private String golsEquipeVisitante;
	private Integer placarEstendidoEquipeVisitante;
}
