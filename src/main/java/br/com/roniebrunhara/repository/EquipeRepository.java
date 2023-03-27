package br.com.roniebrunhara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.roniebrunhara.entity.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

}
