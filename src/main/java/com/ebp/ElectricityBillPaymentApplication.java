package com.ebp;

import com.ebp.entities.Role;
import com.ebp.entities.User;
import com.ebp.repository.roleRepository;
import com.ebp.repository.userRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class ElectricityBillPaymentApplication implements CommandLineRunner{


    @Autowired
    private roleRepository roleRepository;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ElectricityBillPaymentApplication.class, args);
    }

    /*
    Entity
    Repository(I)
    Service(I)
    Service Implementation
    Controller
    Exception
    Helper
    Configurations
    Security
    Data Transfer
     */

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {

        Role r1 = new Role();
        r1.setId(106);
        r1.setRole("ROLE_ADMIN");

        Role r2 = new Role();
        r2.setId(205);
        r2.setRole("ROLE_NORMAL");

        List<Role> roles = List.of(r1, r2);
        this.roleRepository.saveAll(roles);

    }
}
