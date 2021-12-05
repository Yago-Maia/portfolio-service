package com.portfolio.repository;

import com.portfolio.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Portfolio findByName(String name);
    List<Portfolio> findByHasAssetsId(Long id);
}
