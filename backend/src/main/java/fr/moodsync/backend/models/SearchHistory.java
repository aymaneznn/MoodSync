package fr.moodsync.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_history_id_gen")
    @SequenceGenerator(name = "search_history_id_gen", sequenceName = "search_history_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private fr.moodsync.backend.models.User user;

    @Column(name = "search_term", nullable = false)
    private String searchTerm;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "search_date")
    private Instant searchDate;

}