/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Joni Lehtola
 */
@Controller
public class RootController {
    @RequestMapping("/")
    public String root() {
       
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean actualUser = false;
        for (GrantedAuthority a : auth.getAuthorities()) {
            Logger.getLogger(this.getClass().getName()).info(a.getAuthority());
            if (!a.getAuthority().equals("SIGNUP"))
                actualUser = true;
        }
        if (actualUser)
            return "redirect:/events";
        else
            return "redirect:/signups";
    }
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password invalid");
        }
        return "login";
    }
}
