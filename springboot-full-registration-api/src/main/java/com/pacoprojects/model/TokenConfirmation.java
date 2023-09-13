package com.pacoprojects.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table
@Entity(name = "TokenConfirmation")
public class TokenConfirmation {

    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "token_confirmation_sequence", sequenceName = "token_confirmation_sequence", allocationSize = 1)
    @GeneratedValue(generator = "token_confirmation_sequence", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @ManyToOne(targetEntity = ApplicationUser.class)
    @JoinColumn(
            name = "application_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "application_user_id_fk", value = ConstraintMode.CONSTRAINT))
    private ApplicationUser applicationUser;

    public TokenConfirmation(String token, LocalDateTime createdAt, LocalDateTime expiredAt, ApplicationUser applicationUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.applicationUser = applicationUser;
    }
}
