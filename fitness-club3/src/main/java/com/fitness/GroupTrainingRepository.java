package com.fitness;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GroupTrainingRepository extends CrudRepository<GroupTraining, Long> {
    List<GroupTraining> findByStatus(String status);
   // List<GroupTraining> findByTrainerId(Long trainerId);
    //List<GroupTraining> findByTrainer(Trainer trainer);
}