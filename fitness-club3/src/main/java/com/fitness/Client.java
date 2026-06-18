package com.fitness;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
        
    @ManyToMany
    @JoinTable(
        name = "client_training_sessions",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "training_session_id")
    )
    private List<TrainingSession> trainings = new ArrayList<>();
        
    public Client(String name) {
        this.name = name;
        this.registrationDate = LocalDate.now();
    }
    
    public void addTraining(TrainingSession training) {
        this.trainings.add(training);
    }
}