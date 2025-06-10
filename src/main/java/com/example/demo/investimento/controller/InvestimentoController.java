package com.example.demo.investimento.controller;

import com.example.demo.investimento.dto.InvestimentoDTO;
import com.example.demo.investimento.dto.response.VincularResponseDTO;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.service.InvestimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")    public List<InvestimentoDTO> listarTodos() {
        return investimentoService.listarTodos().stream()
                .map(InvestimentoDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<InvestimentoDTO> criar(@RequestBody Investimento investimento) {
        Investimento salvo = investimentoService.salvar(investimento);
        return ResponseEntity.ok(new InvestimentoDTO(salvo));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/toggle-ativo")
    public ResponseEntity<InvestimentoDTO> toggleAtivo(@PathVariable Long id) {
        Investimento investimentoAtualizado = investimentoService.toggleAtivo(id);
        return ResponseEntity.ok(new InvestimentoDTO(investimentoAtualizado));
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InvestimentoDTO> buscar(@PathVariable Long id) {
        Investimento investimento = investimentoService.buscarPorId(id);
        return ResponseEntity.ok(new InvestimentoDTO(investimento));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/investimentos/{id}")
    public ResponseEntity<?> deletarInvestimento(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{investimentoId}/usuario/{usuarioId}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#usuarioId, authentication.name)")
    public ResponseEntity<VincularResponseDTO> vincular(@PathVariable Long investimentoId, @PathVariable Long usuarioId) {
        Investimento investimento = investimentoService.vincularInvestimentoAUsuario(investimentoId, usuarioId);

        String mensagem;
        if (investimento.getUsuarios() != null && 
            investimento.getUsuarios().stream().anyMatch(usuario -> usuario.getId().equals(usuarioId))) {
            mensagem = "Investimento vinculado com sucesso.";
        } else {
            mensagem = "Investimento desvinculado com sucesso.";
        }

        VincularResponseDTO responseDTO = new VincularResponseDTO(new InvestimentoDTO(investimento), mensagem);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("@usuarioService.isOwnerOrAdmin(#usuarioId, authentication.name)")
    public List<InvestimentoDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return investimentoService.listarPorUsuario(usuarioId).stream()
                .map(InvestimentoDTO::new)
                .collect(Collectors.toList());
    }
}
