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
@Table(name = "music_history")
public class MusicHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_history_id_gen")
    @SequenceGenerator(name = "music_history_id_gen", sequenceName = "music_history_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private fr.moodsync.backend.models.User user;

    @Column(name = "track_name", nullable = false)
    private String trackName;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "genre", length = 100)
    private String genre;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "played_at")
    private Instant playedAt;

    @Column(name = "emotion_detected", length = 50)
    private String emotionDetected;

}