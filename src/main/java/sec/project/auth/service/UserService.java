/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.auth.service;

import sec.project.auth.domain.Account;
import sec.project.auth.domain.Role;

/**
 *
 * @author J L
 */
public interface UserService {
    Account save(Account user);
    Account findByUsername(String username);
    Boolean isAdmin(Account user);
    void setAdmin(Account user, Boolean admin);
    Account addRole(Account user, Role role);
    Account addRole(Account user, String role);
    boolean removeRole(Account user, Role role);
    boolean removeRole(Account user, String role);
    Account getLoggedinUser();
}
