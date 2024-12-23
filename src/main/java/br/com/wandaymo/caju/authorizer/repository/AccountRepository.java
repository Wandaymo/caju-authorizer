package br.com.wandaymo.caju.authorizer.repository;

import br.com.wandaymo.caju.authorizer.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> { }