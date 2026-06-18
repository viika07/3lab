package com.fitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
public class FitnessServiceImpl implements FitnessService {

    @Autowired
    private GroupTrainingRepository groupRepo;

    @Autowired
    private PersonalTrainingRepository personalRepo;

    @Autowired
    private TrainerRepository trainerRepo;

    @Autowired
    private ClientRepository clientRepo;


    @Override
    public List<GroupTraining> getAllGroupTrainings() {
        List<GroupTraining> list = new ArrayList<>();
        groupRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public GroupTraining getGroupTraining(Long id) {
        return groupRepo.findById(id).orElse(null);
    }

    @Override
    public PersonalTraining getPersonalTraining(Long id) {
        return personalRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void saveGroupTraining(GroupTraining groupTraining) {
        if (groupTraining.getTrainerId() != null) {
            Trainer existingTrainer = trainerRepo.findById(groupTraining.getTrainerId()).orElse(null);
            if (existingTrainer != null) {
                groupTraining.setTrainer(existingTrainer);
            }
        }
        
        if (groupTraining.getStatus() == null) {
            groupTraining.setStatus("PLANNED");
        }
        if (groupTraining.getCurrentParticipants() == null) {
            groupTraining.setCurrentParticipants(0);
        }
        if (groupTraining.getTrainingType() == null) {
            groupTraining.setTrainingType("GROUP");
        }
        groupRepo.save(groupTraining);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        List<Trainer> list = new ArrayList<>();
        trainerRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteGroupTraining(Long id) {
        groupRepo.deleteById(id);
    }

    @Override
    public List<PersonalTraining> getAllPersonalTrainings() {
        List<PersonalTraining> list = new ArrayList<>();
        personalRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional
    public void savePersonalTraining(PersonalTraining personalTraining) {
        if (personalTraining.getStatus() == null) {
            personalTraining.setStatus("PLANNED");
        }
        if (personalTraining.getSessionsCompleted() == null) {
            personalTraining.setSessionsCompleted(0);
        }
        if (personalTraining.getClientProgress() == null) {
            personalTraining.setClientProgress(0);
        }
        if (personalTraining.getTrainingType() == null) {
            personalTraining.setTrainingType("PERSONAL");
        }

        // Сохраняем тренировку
        personalRepo.save(personalTraining);

        // Ищем существующего клиента по имени
        if (personalTraining.getClientName() != null && !personalTraining.getClientName().isEmpty()) {
            // Ищем клиента по имени (точное совпадение)
            List<Client> existingClients = clientRepo.findByNameContaining(personalTraining.getClientName());
            Client client = null;
            
            // Ищем точное совпадение имени
            for (Client c : existingClients) {
                if (c.getName().equals(personalTraining.getClientName())) {
                    client = c;
                    break;
                }
            }
            
            // Если клиент не найден, создаём нового
            if (client == null) {
                client = new Client(personalTraining.getClientName());
                clientRepo.save(client);
            }
            
            // Добавляем тренировку клиенту
            client.addTraining(personalTraining);
            clientRepo.save(client);
        }
    }

    @Override
    @Transactional
    public void deletePersonalTraining(Long id) {
        PersonalTraining training = personalRepo.findById(id).orElse(null);
        if (training != null) {
            for (Client client : training.getClients()) {
                client.getTrainings().remove(training);
                clientRepo.save(client);
            }
            training.getClients().clear();
            personalRepo.save(training);
            personalRepo.deleteById(id);
        }
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        clientRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional
    public void addParticipantsToGroup(Long groupId, int count) {
        GroupTraining group = getGroupTraining(groupId);
        if (group != null && count > 0 && group.isPlanned()) {
            group.addParticipants(count);
            groupRepo.save(group);
        }
    }

    @Override
    @Transactional
    public void addSingleParticipantToGroup(Long groupId) {
        GroupTraining group = getGroupTraining(groupId);
        if (group != null && group.isPlanned()) {
            group.addSingleParticipant();
            groupRepo.save(group);
        }
    }

    @Override
    @Transactional
    public void removeParticipantFromGroup(Long groupId) {
        GroupTraining group = getGroupTraining(groupId);
        if (group != null && group.isPlanned()) {
            group.removeParticipant();
            groupRepo.save(group);
        }
    }

    @Override
    public int getAvailableSlots(Long groupId) {
        GroupTraining group = getGroupTraining(groupId);
        return group != null ? group.getAvailableSlots() : 0;
    }

    @Override
    @Transactional
    public void startGroupTraining(Long id) {
        GroupTraining group = getGroupTraining(id);
        if (group != null && group.isPlanned()) {
            group.startTraining();
            groupRepo.save(group);
        }
    }

    @Override
    @Transactional
    public void completeGroupTraining(Long id) {
        GroupTraining group = getGroupTraining(id);
        if (group != null && group.isActive()) {
            group.completeTraining();
            groupRepo.save(group);
        }
    }

    @Override
    @Transactional
    public void startPersonalTraining(Long id) {
        PersonalTraining training = getPersonalTraining(id);
        if (training != null && training.isPlanned()) {
            training.startTraining();
            personalRepo.save(training);
        }
    }

    @Override
    @Transactional
    public void completePersonalTraining(Long id) {
        PersonalTraining training = getPersonalTraining(id);
        if (training != null && training.isActive()) {
            training.completeTraining();
            personalRepo.save(training);
        }
    }

    @Override
    @Transactional
    public void updateIntensityLevel(Long id, int newLevel) {
        PersonalTraining training = getPersonalTraining(id);
        if (training != null && (training.isPlanned() || training.isActive())) {
            training.updateIntensityLevel(newLevel);
            personalRepo.save(training);
        }
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerRepo.findById(id).orElse(null);
    }
    
    @Override
    public List<GroupTraining> getGroupTrainingsByTrainerId(Long trainerId) {
        List<GroupTraining> result = new ArrayList<>();
        for (GroupTraining group : getAllGroupTrainings()) {
            if (group.getTrainer() != null && group.getTrainer().getId().equals(trainerId)) {
                result.add(group);
            }
        }
        return result;
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return clientRepo.findByNameContaining(name);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepo.findById(id).orElse(null);
    }

    @Override
    public List<PersonalTraining> getPersonalTrainingsByClientId(Long clientId) {
        Client client = getClientById(clientId);
        if (client == null) {
            return new ArrayList<>();
        }
        
        List<TrainingSession> clientTrainings = client.getTrainings();
        List<PersonalTraining> result = new ArrayList<>();
        
        for (TrainingSession training : clientTrainings) {
            if (training instanceof PersonalTraining) {
                result.add((PersonalTraining) training);
            }
        }
        
        return result;
    }

    @Override
    @Transactional
    public void initTestData() {
        Trainer trainer1 = new Trainer("Елена Иванова", "Йога, Пилатес", 8);
        Trainer trainer2 = new Trainer("Алексей Смирнов", "Силовой тренинг, CrossFit", 10);
        Trainer trainer3 = new Trainer("Мария Петрова", "CrossFit, Функциональный тренинг", 5);
        Trainer trainer4 = new Trainer("Дмитрий Козлов", "Пилатес, Реабилитация", 6);
        Trainer trainer5 = new Trainer("Анна Сидорова", "Зумба, Танцы", 7);

        trainerRepo.save(trainer1);
        trainerRepo.save(trainer2);
        trainerRepo.save(trainer3);
        trainerRepo.save(trainer4);
        trainerRepo.save(trainer5);

        Client client1 = new Client("Владимир");
        Client client2 = new Client("Ольга Сидорова");
        Client client3 = new Client("Михаил Иванов");
        Client client4 = new Client("Сергей Николаев");
        Client client5 = new Client("Татьяна Ким");

        clientRepo.save(client1);
        clientRepo.save(client2);
        clientRepo.save(client3);
        clientRepo.save(client4);
        clientRepo.save(client5);

        GroupTraining group1 = new GroupTraining("Йога утром", 60, 500.0, trainer1, 101, 15);
        GroupTraining group2 = new GroupTraining("Силовой тренинг", 90, 700.0, trainer2, 102, 20);
        GroupTraining group3 = new GroupTraining("CrossFit", 75, 800.0, trainer3, 103, 12);
        GroupTraining group4 = new GroupTraining("Пилатес", 55, 600.0, trainer4, 101, 10);
        GroupTraining group5 = new GroupTraining("Зумба", 60, 550.0, trainer5, 104, 25);

        group1.addParticipants(8);
        group2.addParticipants(15);
        group3.addParticipants(12);
        group4.addParticipants(3);
        group5.addParticipants(20);

        groupRepo.save(group1);
        groupRepo.save(group2);
        groupRepo.save(group3);
        groupRepo.save(group4);
        groupRepo.save(group5);

        PersonalTraining personal1 = new PersonalTraining("Персональная силовая", 60, 1500.0, null, "Штанга, гантели", 8);
        PersonalTraining personal2 = new PersonalTraining("Персональная йога", 90, 2000.0, null, "Коврик, блоки, ремни", 5);
        PersonalTraining personal3 = new PersonalTraining("Реабилитация после операции", 45, 1200.0, null, "Эспандеры, мячи, ленты", 4);
        PersonalTraining personal4 = new PersonalTraining("Бокс", 60, 1800.0, null, "Бинты, перчатки, груша, скакалка", 9);
        PersonalTraining personal5 = new PersonalTraining("Стретчинг", 50, 1300.0, null, "Коврик, блоки, ремни", 6);

        personalRepo.save(personal1);
        personalRepo.save(personal2);
        personalRepo.save(personal3);
        personalRepo.save(personal4);
        personalRepo.save(personal5);

        client1.addTraining(personal1);
        client2.addTraining(personal2);
        client3.addTraining(personal3);
        client4.addTraining(personal4);
        client5.addTraining(personal5);

        clientRepo.save(client1);
        clientRepo.save(client2);
        clientRepo.save(client3);
        clientRepo.save(client4);
        clientRepo.save(client5);

        System.out.println("Тестовые данные успешно загружены!");
        System.out.println("Групповых тренировок: " + groupRepo.count());
        System.out.println("Персональных тренировок: " + personalRepo.count());
        System.out.println("Тренеров: " + trainerRepo.count());
        System.out.println("Клиентов: " + clientRepo.count());
    }
}