package com.example.demo.repository;

import com.example.demo.entity.Substation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstationRepository extends JpaRepository<Substation, Long> {
}
