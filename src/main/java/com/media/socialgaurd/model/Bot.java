package com.media.socialgaurd.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // persona = the bot's personality description
    // e.g., "I am a helpful cooking bot"
    @Column(name = "persona_description", columnDefinition = "TEXT")
    private String personaDescription;
}
