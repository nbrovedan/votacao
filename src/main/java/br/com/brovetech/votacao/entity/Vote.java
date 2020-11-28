package br.com.brovetech.votacao.entity;

import br.com.brovetech.votacao.enumeration.VoteTypeEnum;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Table(name = "vote")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "associated_cpf", length = 11, nullable = false)
    private String associatedCpf;
    @Column(name = "vote_type", length = 1, nullable = false)
    @Enumerated(STRING)
    private VoteTypeEnum voteType;
    @ManyToOne
    @JoinColumn(name = "agenda_id")
    @ToString.Exclude
    private Agenda agenda;
}
