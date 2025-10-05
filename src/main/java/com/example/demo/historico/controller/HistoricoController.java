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
    
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("@historicoService.canAccessUserData(#usuarioId, authentication.name)")
    @Operation(summary = "Buscar histórico por usuário", 
               description = "Busca todo o histórico de investimentos de um usuário com resumo consolidado. Retorna lista de históricos + resumo geral com totais consolidados, quantidade de investimentos, e indicador geral de lucro/prejuízo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado com resumo consolidado incluindo: totalInvestido, totalRetornando, retornoGeral, percentualRetornoGeral, lucroPrejuizoGeral e quantidadeInvestimentos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<HistoricoComResumoDTO> buscarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId,
            Authentication authentication) {
        List<HistoricoResponseDTO> historicos = historicoService.buscarHistoricoPorUsuario(usuarioId, authentication.getName());
        HistoricoComResumoDTO response = historicoService.criarHistoricoComResumo(historicos);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/investimento/{investimentoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Buscar histórico por investimento", 
               description = "Busca todo o histórico de um investimento específico (apenas admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - apenas admins"),
            @ApiResponse(responseCode = "404", description = "Investimento não encontrado")
    })
    public ResponseEntity<List<HistoricoResponseDTO>> buscarPorInvestimento(
            @Parameter(description = "ID do investimento") @PathVariable Long investimentoId) {
        List<HistoricoResponseDTO> response = historicoService.buscarHistoricoPorInvestimento(investimentoId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/periodo")
    @PreAuthorize("@historicoService.canAccessUserData(#usuarioId, authentication.name)")
    @Operation(summary = "Buscar histórico por período", 
               description = "Busca histórico de um usuário em um período específico com resumo consolidado. Inclui análise financeira completa do período com totais e indicadores de performance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico do período encontrado com resumo financeiro consolidado: valores totais, rendimento percentual e classificação geral de lucro/prejuízo"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<HistoricoComResumoDTO> buscarPorPeriodo(
            @Parameter(description = "ID do usuário") @RequestParam Long usuarioId,
            @Parameter(description = "Data de início (formato: YYYY-MM)") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth dataInicio,
            @Parameter(description = "Data de fim (formato: YYYY-MM)") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth dataFim,
            Authentication authentication) {
        List<HistoricoResponseDTO> historicos = historicoService.buscarHistoricoPorUsuarioEPeriodo(
                usuarioId, dataInicio, dataFim, authentication.getName());
        HistoricoComResumoDTO response = historicoService.criarHistoricoComResumo(historicos);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/mes-ano/{mesAno}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Buscar histórico por mês/ano", 
               description = "Busca todos os registros de histórico de um mês/ano específico (apenas admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - apenas admins")
    })
    public ResponseEntity<List<HistoricoResponseDTO>> buscarPorMesAno(
            @Parameter(description = "Mês/Ano (formato: YYYY-MM)") 
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth mesAno) {
        List<HistoricoResponseDTO> response = historicoService.buscarHistoricoPorMesAno(mesAno);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/lucro/{usuarioId}")
    @PreAuthorize("@historicoService.canAccessUserData(#usuarioId, authentication.name)")
    @Operation(summary = "Buscar investimentos com lucro", 
               description = "Busca todos os investimentos que tiveram lucro para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de lucros encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<HistoricoResponseDTO>> buscarInvestimentosComLucro(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId,
            Authentication authentication) {
        List<HistoricoResponseDTO> response = historicoService.buscarInvestimentosComLucro(usuarioId, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/prejuizo/{usuarioId}")
    @PreAuthorize("@historicoService.canAccessUserData(#usuarioId, authentication.name)")
    @Operation(summary = "Buscar investimentos com prejuízo", 
               description = "Busca todos os investimentos que tiveram prejuízo para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de prejuízos encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<HistoricoResponseDTO>> buscarInvestimentosComPrejuizo(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId,
            Authentication authentication) {
        List<HistoricoResponseDTO> response = historicoService.buscarInvestimentosComPrejuizo(usuarioId, authentication.getName());
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
    
    @GetMapping("/ultimos12meses/{usuarioId}")
    @PreAuthorize("@historicoService.canAccessUserData(#usuarioId, authentication.name)")
    @Operation(summary = "Buscar histórico dos últimos 12 meses", 
               description = "Busca o histórico de investimentos de um usuário nos últimos 12 meses com resumo consolidado e indicador de lucro/prejuízo. Fornece análise completa da performance anual com métricas financeiras detalhadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico dos últimos 12 meses com resumo completo: soma de todos os investimentos e retornos, performance geral percentual, classificação de lucro/prejuízo consolidada e contagem total de investimentos realizados"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<HistoricoComResumoDTO> buscarHistoricoUltimos12Meses(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId,
            Authentication authentication) {
        List<HistoricoResponseDTO> historicos = historicoService.buscarHistoricoUltimos12Meses(usuarioId, authentication.getName());
        HistoricoComResumoDTO response = historicoService.criarHistoricoComResumo(historicos);
        return ResponseEntity.ok(response);
    }
}