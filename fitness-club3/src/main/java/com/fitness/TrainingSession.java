package com.fitness;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "training_sessions")
public abstract class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "training_type", nullable = false)
    private String trainingType;

    private String status = "PLANNED";

    @ManyToMany(mappedBy = "trainings")
    private List<Client> clients = new ArrayList<>();

    public TrainingSession(String title, Integer duration, String trainingType) {
        this.title = title;
        this.duration = duration;
        this.trainingType = trainingType;
        this.status = "PLANNED";
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isPlanned() {
        return "PLANNED".equals(this.status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }
}