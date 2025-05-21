package com.itau.pix.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itau.pix.pix.model.ContaPix;

import java.util.UUID;

public interface ContaPixRepository extends JpaRepository<ContaPix, UUID> {
}
