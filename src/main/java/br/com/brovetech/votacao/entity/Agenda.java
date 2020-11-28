package br.com.brovetech.votacao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agenda", indexes = { @Index(name = "idx_result", columnList = "close_date,processed") })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", length = 150, nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "open_date")
    private LocalDateTime openDate;
    @Column(name = "close_date")
    private LocalDateTime closeDate;
    @Column(name = "processed")
    private Boolean processed;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenda", fetch = FetchType.EAGER)
    private List<Vote> votes;
}
