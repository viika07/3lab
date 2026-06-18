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
@Table(name = "group_trainings")
public class GroupTraining extends TrainingSession {

    @Transient
    private Long trainerId;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    private String status = "PLANNED";

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "actual_participants")
    private Integer actualParticipants = 0;

    @Transient
    private String trainerName;

    public GroupTraining(String title, Integer duration, Double price,
                         Trainer trainer, Integer roomNumber, Integer maxParticipants) {
        super(title, duration, "GROUP");
        this.trainer = trainer;
        this.roomNumber = roomNumber;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0;
        this.status = "PLANNED";
    }

    public void addParticipants(int count) {
        int newCount = this.currentParticipants + count;
        this.currentParticipants = Math.min(newCount, this.maxParticipants);
    }

    public void addSingleParticipant() {
        if (this.currentParticipants < this.maxParticipants) {
            this.currentParticipants++;
        }
    }

    public void removeParticipant() {
        if (this.currentParticipants > 0) {
            this.currentParticipants--;
        }
    }

    public void addClient(Client client) {
        if (!getClients().contains(client) && currentParticipants < maxParticipants) {
            client.addTraining(this);  
            currentParticipants++;
        }
    }

    //Удалить клиента.
    public void removeClient(Client client) {
        if (getClients().contains(client)) {
            client.getTrainings().remove(this);
            currentParticipants--;
        }
    }

    public int getAvailableSlots() {
        return this.maxParticipants - this.currentParticipants;
    }

    public boolean hasAvailableSlots() {
        return this.currentParticipants < this.maxParticipants;
    }

    public void startTraining() {
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }

    public void completeTraining() {
        this.status = "COMPLETED";
        this.endTime = LocalDateTime.now();
        this.actualParticipants = this.currentParticipants;
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isPlanned() {
        return "PLANNED".equals(this.status);
    }
}