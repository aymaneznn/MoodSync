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
@Table(name = "session_history")
public class SessionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_history_id_gen")
    @SequenceGenerator(name = "session_history_id_gen", sequenceName = "session_history_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private fr.moodsync.backend.models.User user;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "session_start")
    private Instant sessionStart;

    @Column(name = "session_end")
    private Instant sessionEnd;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "location")
    private String location;

}