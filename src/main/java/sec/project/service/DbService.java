/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.service;

import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author J L
 */
@Service
public class DbService {
    private Connection connection;
    public DbService() {
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:sec", "sa", "");
        } catch (java.sql.SQLException ex) {
            
        }
    }
    
    public Connection getDbConnection() {
        return this.connection;
    }
    
 
    public void finalize() {
        try {
            this.connection.close();
        } catch(java.sql.SQLException ex) {
            
        }
    }

}
