package sec.project.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.auth.domain.Account;
import sec.project.auth.repository.AccountRepository;
import sec.project.auth.service.UserService;
import sec.project.domain.Event;
import sec.project.domain.Signup;
import sec.project.repository.EventRepository;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired EventRepository eventRepo;
    
    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private AccountRepository userRepo;
    
    @Autowired
    private HttpSession session;

    @Autowired
    protected AuthenticationManager authenticationManager;
    
    @Autowired 
    private UserService userService;

    @RequestMapping(value = "/events/{id}/form", method = RequestMethod.GET)
    public String loadForm(Model model, @PathVariable Long id) {
        Event event = eventRepo.findOne(id);
        model.addAttribute("event", event);
        model.addAttribute("signup", new Signup());
        return "form";
    }

    @RequestMapping(value = "/events/{id}/form", method = RequestMethod.POST)
    public String submitForm(Model model, @ModelAttribute Signup signup, @PathVariable Long id, HttpServletRequest request) {
        Event event = eventRepo.findOne(id);
        Account currentUser = userService.getLoggedinUser();
        if (currentUser != null) {
            signup.setOwner(currentUser);
            signup.setEvent(event);
            signupRepository.save(signup);
            return "redirect:/";
        } else {
            String passwd = randomString(6);
            Logger.getLogger(this.getClass().getName()).info("PASSWD: " + passwd);
            Account acc = userService.save(new Account(signup.getName(), signup.getEmail(), passwd, signup.getEmail(), Boolean.FALSE));
            userService.addRole(acc, "SIGNUP");          
            signup.setOwner(acc);
            signup.setEvent(event);
            signupRepository.save(signup);
            try {
                request.login(acc.getUsername(), passwd);
            } catch (ServletException ex) {
                Logger.getLogger(SignupController.class.getName()).log(Level.INFO, null, ex);
            }
            model.addAttribute("username", acc.getUsername());
            model.addAttribute("password", passwd);
            return "done";
        }
        
    }
    
  

        static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        static SecureRandom rnd = new SecureRandom();

        private String randomString( int len ){
           StringBuilder sb = new StringBuilder( len );
           for( int i = 0; i < len; i++ ) 
              sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
           return sb.toString();
        }


    
    private void authenticateUserAndSetSession(User user, HttpServletRequest request) {
        String username = user.getUsername();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
    
    @RequestMapping("/signups")
    public String signups(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().toString().equals("anonymousUser")) {
            Logger.getLogger(this.getClass().getName()).info("AUTH: " + auth.getPrincipal());
            User user = (User)auth.getPrincipal();
            model.addAttribute("signups", signupRepository.findByOwner(userRepo.findByUsername(user.getUsername())));
            return "signups";
        } else 
            return null;
    }
}
