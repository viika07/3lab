package com.fitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class FitnessController {

    @Autowired
    private FitnessService service;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/trainings";
    }
    
    @GetMapping("/trainings")
    public String getAllTrainings(Model model) {
        model.addAttribute("groupTrainings", service.getAllGroupTrainings());
        model.addAttribute("personalTrainings", service.getAllPersonalTrainings());
        return "trainings";
    }
    
    @GetMapping("/add-group")
    public String showAddGroupForm(Model model) {
        model.addAttribute("groupTraining", new GroupTraining());
        model.addAttribute("trainers", service.getAllTrainers());
        return "add-group";
    }
    
    @PostMapping("/add-group")
    public String addGroup(@ModelAttribute GroupTraining groupTraining) {
        service.saveGroupTraining(groupTraining);
        return "redirect:/trainings";
    }
    
    @GetMapping("/add-personal")
    public String showAddPersonalForm(Model model) {
        model.addAttribute("personalTraining", new PersonalTraining());
        model.addAttribute("clients", service.getAllClients());  // Добавляем список клиентов
        return "add-personal";
    }
    
    @PostMapping("/add-personal")
    public String addPersonal(@ModelAttribute PersonalTraining personalTraining) {
        service.savePersonalTraining(personalTraining);
        return "redirect:/trainings";
    }
    
    @GetMapping("/edit-group/{id}")
    public String editGroup(@PathVariable Long id, Model model) {
        model.addAttribute("groupTraining", service.getGroupTraining(id));
        return "edit-group";
    }
    
    @PostMapping("/edit-group")
    public String updateGroup(@ModelAttribute GroupTraining groupTraining) {
        service.saveGroupTraining(groupTraining);
        return "redirect:/trainings";
    }
    
    @GetMapping("/group-detail/{id}")
    public String groupDetail(@PathVariable Long id, Model model) {
        model.addAttribute("training", service.getGroupTraining(id));
        return "group-detail";
    }
    
    @GetMapping("/personal-detail/{id}")
    public String personalDetail(@PathVariable Long id, Model model) {
        model.addAttribute("training", service.getPersonalTraining(id));
        return "personal-detail";
    }
    
    @GetMapping("/add-participants/{groupId}")
    public String showAddParticipantsForm(@PathVariable Long groupId, Model model) {
        GroupTraining group = service.getGroupTraining(groupId);
        model.addAttribute("group", group);
        model.addAttribute("availableSlots", service.getAvailableSlots(groupId));
        return "add-participants";
    }
    
    @PostMapping("/add-participants/{groupId}")
    public String addParticipants(@PathVariable Long groupId, 
                                 @RequestParam int participantCount) {
        service.addParticipantsToGroup(groupId, participantCount);
        return "redirect:/trainings";
    }
    
    @GetMapping("/add-single-participant/{groupId}")
    public String addSingleParticipant(@PathVariable Long groupId) {
        service.addSingleParticipantToGroup(groupId);
        return "redirect:/trainings";
    }
    
    @GetMapping("/remove-participant/{groupId}")
    public String removeParticipant(@PathVariable Long groupId) {
        service.removeParticipantFromGroup(groupId);
        return "redirect:/trainings";
    }
    
    @GetMapping("/start-group/{id}")
    public String startGroupTraining(@PathVariable Long id) {
        service.startGroupTraining(id);
        return "redirect:/trainings";
    }
    
    @GetMapping("/complete-group/{id}")
    public String completeGroupTraining(@PathVariable Long id) {
        service.completeGroupTraining(id);
        return "redirect:/trainings";
    }
    
    @GetMapping("/start-personal/{id}")
    public String startPersonalTraining(@PathVariable Long id) {
        service.startPersonalTraining(id);
        return "redirect:/trainings";
    }
    
    @GetMapping("/complete-personal/{id}")
    public String completePersonalTraining(@PathVariable Long id) {
        service.completePersonalTraining(id);
        return "redirect:/trainings";
    }
    
    @PostMapping("/update-intensity/{id}")
    public String updateIntensity(@PathVariable Long id, @RequestParam int intensityLevel) {
        service.updateIntensityLevel(id, intensityLevel);
        return "redirect:/personal-detail/" + id;
    }
    
    @GetMapping("/delete-group/{id}")
    public String deleteGroup(@PathVariable Long id) {
        service.deleteGroupTraining(id);
        return "redirect:/trainings";
    }
    
    @GetMapping("/delete-personal/{id}")
    public String deletePersonal(@PathVariable Long id) {
        service.deletePersonalTraining(id);
        return "redirect:/trainings";
    }
    
    @PostMapping("/edit-personal")
    public String updatePersonal(@ModelAttribute PersonalTraining personalTraining) {
        service.savePersonalTraining(personalTraining);
        return "redirect:/trainings";
    }

    @GetMapping("/search-trainer")
    public String showSearchTrainerForm() {
        return "search-trainer";
    }
    
    @GetMapping("/trainer-trainings")
    public String getTrainerTrainings(@RequestParam Long trainerId, Model model) {
        Trainer trainer = service.getTrainerById(trainerId);
        if (trainer == null) {
            return "redirect:/search-trainer?error=notfound";
        }
        model.addAttribute("trainer", trainer);
        model.addAttribute("trainings", trainer.getGroupTrainings());
        return "trainer-trainings";
    }

    @GetMapping("/search-client")
    public String showSearchClientForm(Model model) {
        model.addAttribute("allClients", service.getAllClients());
        return "search-client";
    }
    
    @GetMapping("/client-trainings")
    public String getClientTrainings(@RequestParam Long clientId, Model model) {
        Client client = service.getClientById(clientId);
        if (client == null) {
            return "redirect:/search-client?error=notfound";
        }
        
        List<PersonalTraining> personalTrainings = service.getPersonalTrainingsByClientId(client.getId());
        
        model.addAttribute("client", client);
        model.addAttribute("personalTrainings", personalTrainings);
        model.addAttribute("allClients", service.getAllClients());
        
        return "client-trainings";
    }
}