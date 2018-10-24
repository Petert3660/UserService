package com.ptconsultancy.repositories;

import com.ptconsultancy.entities.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(String username);
}
