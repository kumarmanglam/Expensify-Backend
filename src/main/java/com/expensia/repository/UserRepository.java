package com.expensia.repository;

import com.expensia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // get User from Db using username
    Optional<User> findByUsername(String username);
    // return User with matching username or email
    Optional<User> findByUsernameOrEmail(String username, String email);

    // check whether exists with given email in Db
    Boolean existsByEmail(String email);
    // check whether User exists with given username in DB
    Boolean existsByUsername(String username);
}

// Note
// Optional is a container object that may or may not contain a non-null value.
//methods returning Optional indicate that the result may or may not be present,
// allowing for more explicit handling of potential null values in query results

//Optional provides methods like get(), orElse(), and isPresent() for value retrieval
//and checks, promoting safe handling of potentially absent values in a single line of code.