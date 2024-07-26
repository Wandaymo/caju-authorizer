package br.com.wandaymo.caju.authorizer.repository;

import br.com.wandaymo.caju.authorizer.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> { }