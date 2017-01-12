/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import sec.project.auth.domain.Account;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Joni Lehtola
 */
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
  
    @ManyToOne
    private Account creator;
    
    private Boolean isPrivate;
    
    private String name;
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @OneToMany(mappedBy = "event")
    List<Signup> signups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Account getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setSignups(List<Signup> signups) {
        this.signups = signups;
    }

    public List<Signup> getSignups() {
        return signups;
    }
    
}
