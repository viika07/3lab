package com.fitness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PersonalTrainingRepository extends CrudRepository<PersonalTraining, Long> {

  //  @Query("SELECT pt FROM PersonalTraining pt JOIN pt.clients c WHERE c.id = :clientId")
 //   List<PersonalTraining> findByClientId(@Param("clientId") Long clientId);

    List<PersonalTraining> findByStatus(String status);
    // List<PersonalTraining> findByClients(Client client);
}