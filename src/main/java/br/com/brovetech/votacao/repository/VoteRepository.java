package br.com.brovetech.votacao.repository;

import br.com.brovetech.votacao.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("Select v from Vote v where v.agenda.id = :agenciaId and v.associatedCpf = :cpf")
    List<Vote> findAllByAgenciaIdAndCPF(Integer agenciaId, String cpf);
}
