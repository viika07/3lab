package com.fitness;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TrainerRepository extends CrudRepository<Trainer, Long> {
    List<Trainer> findBySpecialization(String specialization);
}