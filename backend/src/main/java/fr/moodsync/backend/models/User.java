package fr.moodsync.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("'user'")
    @Column(name = "role", length = 50)
    private String role;

    @ColumnDefault("'{}'")
    @Column(name = "preferences")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> preferences;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @ColumnDefault("'independent'")
    @Column(name = "connection_type", length = 50)
    private String connectionType;

    @Column(name = "external_account_id")
    private String externalAccountId;

}