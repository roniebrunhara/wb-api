package br.com.roniebrunhara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.roniebrunhara.entity.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

}
