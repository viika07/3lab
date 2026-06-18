package com.fitness;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findByNameContaining(String name);
}