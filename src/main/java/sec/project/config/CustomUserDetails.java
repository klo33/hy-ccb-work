/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.config;

import java.util.Collection;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author J L
 */
public class CustomUserDetails extends User {
    public CustomUserDetails(String username, String password, Collection authorities) {
        
        super(username, password, true, true, true, true, authorities);
        
    }

}
