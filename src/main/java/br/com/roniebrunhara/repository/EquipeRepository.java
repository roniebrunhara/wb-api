package br.com.roniebrunhara.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.roniebrunhara.entity.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
	
	public Optional<Equipe> findByNomeEquipe(String nomeEquipe);
	public boolean existsByNomeEquipe(String nomeEquipe);

}
