package fr.moodsync.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "content_metadata")
public class ContentMetadatum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_metadata_id_gen")
    @SequenceGenerator(name = "content_metadata_id_gen", sequenceName = "content_metadata_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "creator")
    private String creator;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ColumnDefault("'[]'")
    @Column(name = "tags")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tags;

    @Column(name = "rating")
    private Double rating;

}