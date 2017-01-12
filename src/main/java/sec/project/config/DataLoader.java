/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sec.project.auth.domain.Account;
import sec.project.auth.domain.Role;
import sec.project.auth.repository.AccountRepository;
import sec.project.auth.repository.RoleRepository;
import sec.project.auth.service.UserService;
import sec.project.domain.Event;
import sec.project.domain.Signup;
import sec.project.repository.EventRepository;
import sec.project.repository.SignupRepository;

/**
 *
 * @author J L
 */
@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private AccountRepository userRepo;
    
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private SignupRepository signupRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleRepository roleRepo;
  
    
    public void run(ApplicationArguments args) {
        //roleRepo.save(new Role("TESTI"));
        Account acc = userService.save(new Account("Testi-ilmoittautuja", "testi", "testi1", "testi@testi.com", Boolean.FALSE));
        userService.addRole(acc, "SIGNUP");
        Event event = new Event();
        event.setCreator(acc);
        event.setName("Testitapahtuma");
        eventRepo.save(event);
        Signup su = new Signup(acc.getName(), "Osoite", acc.getEmail());
        su.setOwner(acc);
        signupRepo.save(su);
        
        
    }
}
