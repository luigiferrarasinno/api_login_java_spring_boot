package com.example.demo.investimento.repository;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    List<Investimento> findByUsuario(Usuario usuario);
}
