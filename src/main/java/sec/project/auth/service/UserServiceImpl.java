/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.auth.service;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sec.project.auth.domain.Account;
import sec.project.auth.domain.Role;
import sec.project.auth.repository.AccountRepository;
import sec.project.auth.repository.RoleRepository;

/**
 *
 * @author J L
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private AccountRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    
    @Override
//    @Transactional
    public Account addRole(Account user, Role role) {
        if (role == null || user == null)
            return user;
        user = userRepo.findByUsername(user.getUsername());
        //role.getUsers().add(user);
        Logger.getLogger(this.getClass().getName()).info("addRole: adding role "+role.getName());
        user.getRoles().add(role);
//        try {
            user = userRepo.save(user);
//        } catch (Exception ex) {
//            Logger.getLogger(this.getClass().getName()).severe("SQL Exception: "+ex.getMessage());
//        }
        Logger.getLogger(this.getClass().getName()).info("addRole: user saved in Repo");
        //roleRepo.save(role);
        return user;
    }
    @Override
    public Account addRole(Account user, String role) {
        return this.addRole(user, roleRepo.findByName(role));
    }
    
    @Override
    @Transactional
    public boolean removeRole(Account user, Role role) {
        if (role == null || user == null)
            return false;
        user = userRepo.findByUsername(user.getUsername());
     //   role.getUsers().remove(user);
        user.getRoles().remove(role);
        
    //    roleRepo.save(role);
        userRepo.save(user);
        return true;
    }
    
    @Override
    public boolean removeRole(Account user, String role) {
        return this.removeRole(user, roleRepo.findByName(role));
    }
    
    
    @Override
    public Account save(Account user) {
        if (user == null) {
            return null;
        }
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPassword()));
        user = userRepo.save(user);
        Logger.getLogger(this.getClass().getName()).info("1st saving");
        return user;
    }
    
    @Override
    public Account findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    
    @Override 
    public Boolean isAdmin(Account user) {
        if (user.getRoles().contains(roleRepo.findByName("ADMIN"))) {
            return true;
        }
        return false;
    }
    
    @Override 
    public void setAdmin(Account user, Boolean admin) {
        if (admin) {
            this.addRole(user, "ADMIN");
        } else {
            this.removeRole(user, "ADMIN");
        }
    }
    
    @Override
    public Account getLoggedinUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().toString().equals("anonymousUser")) {
            Logger.getLogger(this.getClass().getName()).info("AUTH: " + auth.getPrincipal());
            User user = (User)auth.getPrincipal();
            return userRepo.findByUsername(user.getUsername());
        }
        else 
            return null;
    }

}
