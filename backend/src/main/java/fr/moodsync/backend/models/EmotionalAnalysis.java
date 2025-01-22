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
@Table(name = "emotional_analyses")
public class EmotionalAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emotional_analyses_id_gen")
    @SequenceGenerator(name = "emotional_analyses_id_gen", sequenceName = "emotional_analyses_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id")
    private fr.moodsync.backend.models.Post post;

    @Column(name = "emotion", nullable = false, length = 50)
    private String emotion;

    @Column(name = "intensity_score")
    private Double intensityScore;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}