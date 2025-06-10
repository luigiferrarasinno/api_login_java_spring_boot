package com.example.demo.investimento.service;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final UsuarioRepository usuarioRepository;

    public InvestimentoService(InvestimentoRepository investimentoRepository, UsuarioRepository usuarioRepository) {
        this.investimentoRepository = investimentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Investimento> listarTodos() {
        return investimentoRepository.findAll();
    }

    public Investimento salvar(Investimento investimento) {
        return investimentoRepository.save(investimento);
    }

    public Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
    }

    public void deletar(Long id) {
        Optional<Investimento> investimento = investimentoRepository.findById(id);

        if (investimento.isEmpty()) {
            throw new AccessDeniedException("Acesso negado"); // ou uma exceção mais descritiva
        }

        investimentoRepository.deleteById(id);
    }


    // Lista investimentos de um usuário específico
    public List<Investimento> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Set<Investimento> investimentos = usuario.getInvestimentos();
        return investimentos.stream().collect(Collectors.toList());
    }

    // Vincula ou desvincula um investimento a um usuário
    @Transactional
    public Investimento vincularInvestimentoAUsuario(Long investimentoId, Long usuarioId) {
        Investimento investimento = buscarPorId(investimentoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (investimento.getUsuarios().contains(usuario)) {
            // Se já vinculado, remove a associação dos dois lados
            investimento.getUsuarios().remove(usuario);
            usuario.getInvestimentos().remove(investimento);
        } else {
            // Se não vinculado, adiciona a associação dos dois lados
            investimento.getUsuarios().add(usuario);
            usuario.getInvestimentos().add(investimento);
        }

        // Salva as duas entidades para garantir sincronização
        usuarioRepository.save(usuario);
        return investimentoRepository.save(investimento);
    }
}
