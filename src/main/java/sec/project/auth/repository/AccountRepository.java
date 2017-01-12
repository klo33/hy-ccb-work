/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.auth.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.auth.domain.Account;

/**
 *
 * @author J L
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
        public Account findByUsername(String username);
}
