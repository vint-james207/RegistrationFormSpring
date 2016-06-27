package com.james.services;

import com.james.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jamesyburr on 6/27/16.
 */
public interface UserRepository extends CrudRepository<User, Integer>{
}
