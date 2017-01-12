/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.auth.domain.Role;

/**
 *
 * @author Joni Lehtola
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}
