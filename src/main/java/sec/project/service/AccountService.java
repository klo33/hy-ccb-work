/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.service;

import java.sql.SQLException;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.auth.repository.AccountRepository;

/**
 *
 * @author Joni Lehtola
 */
@Service
public class AccountService {
    @Autowired
    private DbService dbService;
    @Autowired
    private AccountRepository userRepo;
    
    public void getUserinfo(String id) {
  
    }
    
}
