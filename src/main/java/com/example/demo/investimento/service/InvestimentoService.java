package com.example.demo.investimento.service;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        investimentoRepository.deleteById(id);
    }

    public List<Investimento> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return investimentoRepository.findByUsuario(usuario);
        }

    public Investimento vincularInvestimentoAUsuario(Long investimentoId, Long usuarioId) {
        Investimento investimento = buscarPorId(investimentoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (investimento.getUsuario() != null && investimento.getUsuario().getId().equals(usuarioId)) {
            investimento.setUsuario(null);
        } else {
            investimento.setUsuario(usuario);
        }

        return investimentoRepository.save(investimento);
    }


}
