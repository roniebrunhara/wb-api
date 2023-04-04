package br.com.roniebrunhara.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.roniebrunhara.entity.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

	@Query(name="listar_partidas_periodo", 
			value="SELECT * FROM PARTIDA P WHERE P.DATA_HORA_PARTIDA BETWEEN NOW() - INTERVAL 3 HOUR AND NOW() AND IFNULL(P.TEMPO_PARTIDA,'Vazio') != 'Encerrado'", 
			nativeQuery = true)
	public List<Partida> listarPartidasPeriodo();

	@Query(name="buscar_quantidade_partidas_periodo", 
			value="SELECT COUNT(*) FROM PARTIDA P WHERE P.DATA_HORA_PARTIDA BETWEEN NOW() - INTERVAL 3 HOUR AND NOW() AND IFNULL(P.TEMPO_PARTIDA,'Vazio') != 'Encerrado'", 
			nativeQuery = true)
	public Integer buscarQuantidadePartidasPeriodo();

}
