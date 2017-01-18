package sec.project.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;
import sec.project.auth.domain.Account;

@Entity
public class Signup extends AbstractPersistable<Long> {

    @NotBlank
    @Length(min = 3, max = 140)
    private String name;
    @NotBlank
    @Email
    private String email;
    @Length(min = 0, max = 140)
    private String address;
    @ManyToOne
    private Account owner;
    @ManyToOne
    private Event event;

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Signup() {
        super();
    }

    public Signup(String name, String address, String email) {
        this();
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
