package br.com.wandaymo.caju.authorizer.repository;

import br.com.wandaymo.caju.authorizer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);

}