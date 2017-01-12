package sec.project.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sec.project.auth.domain.Account;
import sec.project.auth.repository.AccountRepository;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.security.core.GrantedAuthority;
import sec.project.auth.domain.Role;
import sec.project.auth.repository.RoleRepository;
import sec.project.auth.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    //private Map<String, String> accountDetails;
    @Autowired
    private AccountRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        roleRepo.save(new Role("USER")); 
        roleRepo.save(new Role("ADMIN"));
        roleRepo.save(new Role("SIGNUP"));
        //this.accountDetails = new TreeMap<>();
        //this.accountDetails.put("ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        //userService.save(new Account("Ted", "ted", "$2a$06$rPAMTvmT7Y4FqpR6BU/Xpe.uQ3/y2MXNAdQz9ARF8dCBPjXz1.u66", "", false));
        Logger.getLogger(this.getClass().getName()).info("Adding 1st user TED");
        Account ted, al, bundy;
       ted = userService.save(new Account("Ted", "ted", "ted", "", false));
       userService.addRole(ted, "USER");
        Logger.getLogger(this.getClass().getName()).info("Adding 2nd user AL");
       al = userService.save(new Account("Al", "al", "al", "", true));
       userService.addRole(al, "ADMIN");
       bundy = userService.save(new Account("Bundy", "bundy", "bundy", "oioi", false));
       userService.addRole(bundy, "USER");

        
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        Set<GrantedAuthority> grantedAuthoritied = new HashSet<>();
        for (Role role: user.getRoles()) {
            grantedAuthoritied.add(new SimpleGrantedAuthority(role.getName()));
        }
//        grantedAuthoritied.add(new SimpleGrantedAuthority(("USER")));
//        if (user.getIsAdmin()) {
//            grantedAuthoritied.add(new SimpleGrantedAuthority(("ADMIN")));          
//        }
        
        
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPasswordHash(),
                true,
                true,
                true,
                true,
                grantedAuthoritied);
    }
}
