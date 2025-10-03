package com.example.demo.carteira.controller;

import com.example.demo.carteira.service.CarteiraService;
import com.example.demo.carteira.dto.PosicaoCarteiraResponseDTO;
import com.example.demo.carteira.dto.ResumoCarteiraResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carteira")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResumoCarteiraResponseDTO> obterResumoCarteira(Authentication authentication) {
        ResumoCarteiraResponseDTO resumo = carteiraService.obterResumoCarteira(authentication.getName());
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/posicoes")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PosicaoCarteiraResponseDTO>> obterPosicoesCarteira(Authentication authentication) {
        List<PosicaoCarteiraResponseDTO> posicoes = carteiraService.obterPosicoesCarteira(authentication.getName());
        return ResponseEntity.ok(posicoes);
    }
}