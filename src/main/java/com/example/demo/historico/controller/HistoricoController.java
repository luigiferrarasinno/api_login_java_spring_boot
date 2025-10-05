package com.example.demo.historico.controller;

import com.example.demo.historico.dto.HistoricoRequestDTO;
import com.example.demo.historico.dto.HistoricoResponseDTO;
import com.example.demo.historico.dto.HistoricoComResumoDTO;
import com.example.demo.historico.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/historico")
@Tag(name = "Histórico", description = "API para gerenciamento de histórico de investimentos")
public class HistoricoController {
    
    @Autowired
    private HistoricoService historicoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Criar novo registro de histórico", 
               description = "Cria um novo registro de histórico de investimento para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Histórico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário ou investimento não encontrado")
    })
    public ResponseEntity<HistoricoResponseDTO> criarHistorico(
            @Valid @RequestBody HistoricoRequestDTO request,
            Authentication authentication) {
        HistoricoResponseDTO response = historicoService.criarHistorico(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("@historicoService.canAccessHistorico(#id, authentication.name)")
    @Operation(summary = "Buscar histórico por ID", 
               description = "Busca um registro específico de histórico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public ResponseEntity<HistoricoResponseDTO> buscarPorId(
            @Parameter(description = "ID do histórico") @PathVariable Long id,
            Authentication authentication) {
        HistoricoResponseDTO response = historicoService.buscarPorId(id, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/consultar")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Consultar histórico com filtros", 
               description = "Endpoint unificado para consultar histórico com diferentes filtros: período, tipo (lucro/prejuízo), últimos 12 meses, mês específico, ou por ID específico. O usuário é identificado automaticamente pelo token. Sempre retorna resumo consolidado com análise completa dos dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado com resumo consolidado: lista de investimentos + resumo geral (totalInvestido, totalRetornando, retornoGeral, percentualRetornoGeral, lucroPrejuizoGeral, quantidadeInvestimentos)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<HistoricoComResumoDTO> consultarHistorico(
            @Parameter(description = "ID específico do histórico") 
            @RequestParam(required = false) Long id,
            @Parameter(description = "Data de início do período (formato: YYYY-MM)") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth dataInicio,
            @Parameter(description = "Data de fim do período (formato: YYYY-MM)") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth dataFim,
            @Parameter(description = "Filtrar por tipo: 'lucro' ou 'prejuizo'") 
            @RequestParam(required = false) String tipo,
            @Parameter(description = "Buscar apenas os últimos 12 meses (true/false)") 
            @RequestParam(required = false, defaultValue = "false") Boolean ultimos12Meses,
            @Parameter(description = "Mês/Ano específico (formato: YYYY-MM)") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth mesAno,
            @Parameter(description = "ID do investimento específico") 
            @RequestParam(required = false) Long investimentoId,
            Authentication authentication) {
        
        List<HistoricoResponseDTO> historicos;
        
        // Buscar por ID específico do histórico
        if (id != null) {
            HistoricoResponseDTO historico = historicoService.buscarPorId(id, authentication.getName());
            historicos = List.of(historico);
        }
        // Buscar por investimento específico
        else if (investimentoId != null) {
            historicos = historicoService.buscarHistoricoPorInvestimentoDoUsuario(investimentoId, authentication.getName());
        }
        // Buscar por mês/ano específico
        else if (mesAno != null) {
            historicos = historicoService.buscarHistoricoPorMesAnoDoUsuario(mesAno, authentication.getName());
        }
        // Buscar últimos 12 meses
        else if (ultimos12Meses) {
            historicos = historicoService.buscarHistoricoUltimos12MesesDoUsuario(authentication.getName());
        }
        // Buscar por período
        else if (dataInicio != null && dataFim != null) {
            historicos = historicoService.buscarHistoricoPorPeriodoDoUsuario(dataInicio, dataFim, authentication.getName());
        }
        // Buscar por tipo (lucro/prejuízo)
        else if (tipo != null) {
            if ("lucro".equalsIgnoreCase(tipo)) {
                historicos = historicoService.buscarInvestimentosComLucroDoUsuario(authentication.getName());
            } else if ("prejuizo".equalsIgnoreCase(tipo)) {
                historicos = historicoService.buscarInvestimentosComPrejuizoDoUsuario(authentication.getName());
            } else {
                throw new RuntimeException("Tipo inválido. Use 'lucro' ou 'prejuizo'");
            }
        }
        // Buscar todos os históricos do usuário
        else {
            historicos = historicoService.buscarHistoricoDoUsuario(authentication.getName());
        }
        
        HistoricoComResumoDTO response = historicoService.criarHistoricoComResumo(historicos);
        return ResponseEntity.ok(response);
    }
    

    

    

    

    

    
    @PutMapping("/{id}")
    @PreAuthorize("@historicoService.canAccessHistorico(#id, authentication.name)")
    @Operation(summary = "Atualizar histórico", 
               description = "Atualiza um registro de histórico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Histórico, usuário ou investimento não encontrado")
    })
    public ResponseEntity<HistoricoResponseDTO> atualizarHistorico(
            @Parameter(description = "ID do histórico") @PathVariable Long id,
            @Valid @RequestBody HistoricoRequestDTO request,
            Authentication authentication) {
        HistoricoResponseDTO response = historicoService.atualizarHistorico(id, request, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("@historicoService.canAccessHistorico(#id, authentication.name)")
    @Operation(summary = "Deletar histórico", 
               description = "Remove um registro de histórico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Histórico deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public ResponseEntity<Void> deletarHistorico(
            @Parameter(description = "ID do histórico") @PathVariable Long id,
            Authentication authentication) {
        historicoService.deletarHistorico(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
    

}