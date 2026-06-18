package com.fitness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "personal_trainings")
public class PersonalTraining extends TrainingSession {

    private String equipment;

    @Column(name = "intensity_level", nullable = false)
    private Integer intensityLevel;

    private String status = "PLANNED";

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "sessions_completed")
    private Integer sessionsCompleted = 0;

    @Column(name = "client_progress")
    private Integer clientProgress = 0;

    @Transient
    private String clientName;

    public PersonalTraining(String title, Integer duration, Double price,
                            Client client, String equipment, Integer intensityLevel) {
        super(title, duration, "PERSONAL");
        this.equipment = equipment;
        this.intensityLevel = intensityLevel;
        this.status = "PLANNED";
        this.clientProgress = 0;
        this.sessionsCompleted = 0;

        if (client != null) {
            client.addTraining(this);
        }
    }

    
    public Client getClient() {
        return getClients().stream().findFirst().orElse(null);
    }

    public void startTraining() {
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }

    public void completeTraining() {
        this.status = "COMPLETED";
        this.endTime = LocalDateTime.now();
        this.sessionsCompleted++;
        updateProgress();
    }

    public void updateProgress() {
        this.clientProgress = Math.min(100, this.sessionsCompleted * 10);
    }

    public void updateIntensityLevel(int newLevel) {
        if (newLevel >= 1 && newLevel <= 10) {
            this.intensityLevel = newLevel;
        }
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isPlanned() {
        return "PLANNED".equals(this.status);
    }
}