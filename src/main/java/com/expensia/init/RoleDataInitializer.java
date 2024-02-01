package com.expensia.init;

import com.expensia.entity.Role;
import com.expensia.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initiateRoles();
    }

    public void initiateRoles(){
        createRoleIfNotExists("USER_ROLE");
        createRoleIfNotExists("USER_ADMIN");
    }
    public void createRoleIfNotExists(String roleName){
        if(!roleRepository.existsByName(roleName)){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
