package com.expensia.service;

import com.expensia.entity.User;

import java.util.Optional;

public interface UserService {
    User getUserById(Long userId);
    User getUserByUsername(String username);
    User getUserByUsernameOrEmail(String username, String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void saveUser(User user);

    void updateUser(User user);
    User findByEmail(String email);
    // Additional methods based on your business requirements
}

// Now how does interface help in injection to use in controller class

//service interfaces establish a contract for implementations, promoting loose coupling.
//In Spring, dependencies can be injected in one line using either @Autowired on the constructor or,
//alternatively, leveraging Lombok's @AllArgsConstructor for automatic constructor injection in the implementing class.