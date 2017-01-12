package sec.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.auth.domain.Account;
import sec.project.domain.Signup;

public interface SignupRepository extends JpaRepository<Signup, Long> {
    List<Signup> findByOwner(Account acc);
}
