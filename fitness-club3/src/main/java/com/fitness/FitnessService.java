package com.fitness;

import java.util.List;

public interface FitnessService {
    // GroupTraining
    List<GroupTraining> getAllGroupTrainings();
    GroupTraining getGroupTraining(Long id);
    void saveGroupTraining(GroupTraining groupTraining);
    void deleteGroupTraining(Long id);
    
    // PersonalTraining
    List<PersonalTraining> getAllPersonalTrainings();
    PersonalTraining getPersonalTraining(Long id);
    void savePersonalTraining(PersonalTraining personalTraining);
    void deletePersonalTraining(Long id);
    
    // Participants
    void addParticipantsToGroup(Long groupId, int count);
    void addSingleParticipantToGroup(Long groupId);
    void removeParticipantFromGroup(Long groupId);
    int getAvailableSlots(Long groupId);
    
    // Training 
    void startGroupTraining(Long id);
    void completeGroupTraining(Long id);
    void startPersonalTraining(Long id);
    void completePersonalTraining(Long id);
    void updateIntensityLevel(Long id, int newLevel);

    Trainer getTrainerById(Long id);
    List<GroupTraining> getGroupTrainingsByTrainerId(Long trainerId);

    List<Client> searchClientsByName(String name);
    Client getClientById(Long id);
    List<PersonalTraining> getPersonalTrainingsByClientId(Long clientId);

    List<Trainer> getAllTrainers();
    
    List<Client> getAllClients();
    
    void initTestData();
}