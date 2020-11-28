package br.com.brovetech.votacao.repository;

import br.com.brovetech.votacao.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    List<Agenda> findAllByCloseDateBeforeAndProcessed(LocalDateTime now, Boolean processed);
}
