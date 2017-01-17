/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.auth.domain.Account;
import sec.project.auth.domain.Role;
import sec.project.auth.repository.AccountRepository;
import sec.project.auth.repository.RoleRepository;
import sec.project.auth.service.UserService;
import sec.project.service.DbService;

/**
 *
 * @author J L
 */
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private AccountRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private DbService dbService;
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public String newuser(@ModelAttribute Account newuser, @RequestParam List<String> rolesSelected) {
        Set<Role> roles = newuser.getRoles();
        for (String role: rolesSelected) {
            roles.add(roleRepo.findByName(role));
        }
        userService.save(newuser);
        return "redirect:/users";
    }
    
    @RequestMapping(value="", method = RequestMethod.GET)
    public String listusers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("newuser", new Account());
        model.addAttribute("roles", roleRepo.findAll());
        return "userlist";
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        userRepo.delete(id);
        return "redirect:/users";
    }
    
    @RequestMapping(value="/{id}/promote", method = RequestMethod.PUT, params = "auth")
    public String promote(@PathVariable Long id, @RequestParam String auth) {
        Account acc = userRepo.findOne(id);
        userService.addRole(acc, auth);
        return "redirect:/users";
    }
    
    @RequestMapping(value="/{id}/denounce", method = RequestMethod.PUT, params = "auth")
    public String denounce(@PathVariable Long id, @RequestParam String auth) {
        Account acc = userRepo.findOne(id);
        userService.removeRole(acc, auth);
        return "redirect:/users";
    }
    
    @RequestMapping(value="/own", method=RequestMethod.GET)
    public String details(Model model) {
        Account acc = userService.getLoggedinUser();
        if (acc == null) {
            return null;
        }
        model.addAttribute("user", acc);
        return "userdetails";
    }

    /* FIXED FLAW 4: XSRF change password without consent */
    @RequestMapping(value = "/own/password", method = RequestMethod.PUT)
    public String pwd(@RequestParam String password, @RequestParam String newpassword1, @RequestParam String newpassword2) {
        Account acc = userService.getLoggedinUser();
        if (acc == null) {
            return null;
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(acc.getUsername(), password);
        Authentication authenticate = authManager.authenticate(token);
        if (authenticate.isAuthenticated() && newpassword1.equals(newpassword2)) {
            acc.setPassword(newpassword1);
            userService.save(acc);
            return "redirect:/users/own?success=pwchange";
        }
        return "redirect:/users/own?error=pwmatch";
    }
}
