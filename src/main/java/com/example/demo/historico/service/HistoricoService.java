package com.example.demo.historico.service;

import com.example.demo.historico.dto.HistoricoRequestDTO;
import com.example.demo.historico.dto.HistoricoResponseDTO;
import com.example.demo.historico.dto.ResumoHistoricoDTO;
import com.example.demo.historico.dto.HistoricoComResumoDTO;
import com.example.demo.historico.model.Historico;
import com.example.demo.historico.repository.HistoricoRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.repository.UsuarioRepository;
import com.example.demo.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoricoService {
    
    @Autowired
    private HistoricoRepository historicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private InvestimentoRepository investimentoRepository;
    
    /**
     * Verifica se o usuário autenticado pode acessar o histórico
     * Admin pode acessar tudo, usuário comum só seus próprios dados
     */
    public boolean canAccessHistorico(Long historicoId, String emailUsuarioAuth) {
        Optional<Usuario> usuarioAuth = usuarioRepository.findByEmail(emailUsuarioAuth);
        
        if (usuarioAuth.isEmpty()) return false;
        
        Usuario usuarioLogado = usuarioAuth.get();
        
        // Admin pode acessar qualquer histórico
        if ("ROLE_ADMIN".equals(usuarioLogado.getRole())) {
            return true;
        }
        
        // Verificar se o histórico existe e pertence ao usuário
        Optional<Historico> historico = historicoRepository.findById(historicoId);
        return historico.isPresent() && 
               historico.get().getUsuario().getId().equals(usuarioLogado.getId());
    }
    
    /**
     * Verifica se o usuário autenticado pode acessar os dados de outro usuário
     * Admin pode acessar qualquer usuário, usuário comum só seus próprios dados
     */
    public boolean canAccessUserData(Long usuarioId, String emailUsuarioAuth) {
        Optional<Usuario> usuarioAuth = usuarioRepository.findByEmail(emailUsuarioAuth);
        
        if (usuarioAuth.isEmpty()) return false;
        
        Usuario usuarioLogado = usuarioAuth.get();
        
        // Admin pode acessar dados de qualquer usuário
        if ("ROLE_ADMIN".equals(usuarioLogado.getRole())) {
            return true;
        }
        
        // Usuário comum só pode acessar seus próprios dados
        return usuarioLogado.getId().equals(usuarioId);
    }
    
    /**
     * Verifica se o usuário autenticado pode criar/alterar histórico para outro usuário
     */
    public boolean canModifyUserHistorico(Long usuarioId, String emailUsuarioAuth) {
        return canAccessUserData(usuarioId, emailUsuarioAuth);
    }
    
    @Transactional
    public HistoricoResponseDTO criarHistorico(HistoricoRequestDTO request, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode criar histórico para o usuário solicitado
        if (!canModifyUserHistorico(request.getUsuarioId(), emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para criar histórico para este usuário");
        }
        
        // Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + request.getUsuarioId()));
        
        // Verificar se o investimento existe
        Investimento investimento = investimentoRepository.findById(request.getInvestimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Investimento não encontrado com ID: " + request.getInvestimentoId()));
        
        // Verificar se já existe um registro para o mesmo usuário, investimento e mês/ano
        Optional<Historico> historicoExistente = historicoRepository
                .findByUsuarioIdAndInvestimentoIdAndMesAnoRegistro(
                        request.getUsuarioId(), 
                        request.getInvestimentoId(), 
                        request.getMesAnoRegistro());
        
        Historico historico;
        if (historicoExistente.isPresent()) {
            // Atualizar registro existente
            historico = historicoExistente.get();
            historico.setTotalInvestido(request.getTotalInvestido());
            historico.setTotalRetornando(request.getTotalRetornando());
        } else {
            // Criar novo registro
            historico = new Historico(investimento, usuario, 
                                    request.getTotalInvestido(), 
                                    request.getTotalRetornando(), 
                                    request.getMesAnoRegistro());
        }
        
        historico = historicoRepository.save(historico);
        return converterParaResponseDTO(historico);
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarHistoricoPorUsuario(Long usuarioId, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar os dados do usuário solicitado
        if (!canAccessUserData(usuarioId, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar os dados deste usuário");
        }
        
        // Verificar se o usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + usuarioId));
        
        List<Historico> historicos = historicoRepository.findByUsuarioIdOrderByDataRegistroDesc(usuarioId);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarHistoricoPorInvestimento(Long investimentoId) {
        // Verificar se o investimento existe
        investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Investimento não encontrado com ID: " + investimentoId));
        
        List<Historico> historicos = historicoRepository.findByInvestimentoId(investimentoId);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarHistoricoPorMesAno(YearMonth mesAno) {
        List<Historico> historicos = historicoRepository.findByMesAnoRegistro(mesAno);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarHistoricoPorUsuarioEPeriodo(Long usuarioId, 
                                                                        YearMonth dataInicio, 
                                                                        YearMonth dataFim,
                                                                        String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar os dados do usuário solicitado
        if (!canAccessUserData(usuarioId, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar os dados deste usuário");
        }
        
        // Verificar se o usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + usuarioId));
        
        List<Historico> historicos = historicoRepository.findByUsuarioIdAndPeriodo(usuarioId, dataInicio, dataFim);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarInvestimentosComLucro(Long usuarioId, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar os dados do usuário solicitado
        if (!canAccessUserData(usuarioId, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar os dados deste usuário");
        }
        
        // Verificar se o usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + usuarioId));
        
        List<Historico> historicos = historicoRepository.findInvestimentosComLucro(usuarioId);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarInvestimentosComPrejuizo(Long usuarioId, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar os dados do usuário solicitado
        if (!canAccessUserData(usuarioId, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar os dados deste usuário");
        }
        
        // Verificar se o usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + usuarioId));
        
        List<Historico> historicos = historicoRepository.findInvestimentosComPrejuizo(usuarioId);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public HistoricoResponseDTO buscarPorId(Long id, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar este histórico
        if (!canAccessHistorico(id, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar este histórico");
        }
        
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Histórico não encontrado com ID: " + id));
        return converterParaResponseDTO(historico);
    }
    
    @Transactional
    public HistoricoResponseDTO atualizarHistorico(Long id, HistoricoRequestDTO request, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar/modificar este histórico
        if (!canAccessHistorico(id, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para modificar este histórico");
        }
        
        // Verificar se pode modificar para o novo usuário (caso tenha mudado)
        if (!canModifyUserHistorico(request.getUsuarioId(), emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para alterar histórico para este usuário");
        }
        
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Histórico não encontrado com ID: " + id));
        
        // Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + request.getUsuarioId()));
        
        // Verificar se o investimento existe
        Investimento investimento = investimentoRepository.findById(request.getInvestimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Investimento não encontrado com ID: " + request.getInvestimentoId()));
        
        // Atualizar dados
        historico.setUsuario(usuario);
        historico.setInvestimento(investimento);
        historico.setTotalInvestido(request.getTotalInvestido());
        historico.setTotalRetornando(request.getTotalRetornando());
        historico.setMesAnoRegistro(request.getMesAnoRegistro());
        
        historico = historicoRepository.save(historico);
        return converterParaResponseDTO(historico);
    }
    
    @Transactional
    public void deletarHistorico(Long id, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar/modificar este histórico
        if (!canAccessHistorico(id, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para deletar este histórico");
        }
        
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Histórico não encontrado com ID: " + id));
        
        historicoRepository.delete(historico);
    }
    
    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> buscarHistoricoUltimos12Meses(Long usuarioId, String emailUsuarioAuth) {
        // Verificar se o usuário autenticado pode acessar os dados do usuário solicitado
        if (!canAccessUserData(usuarioId, emailUsuarioAuth)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar os dados deste usuário");
        }
        
        // Verificar se o usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com ID: " + usuarioId));
        
        // Calcular o período dos últimos 12 meses
        YearMonth dataFim = YearMonth.now();
        YearMonth dataInicio = dataFim.minusMonths(11); // 12 meses incluindo o atual
        
        List<Historico> historicos = historicoRepository.findByUsuarioIdAndPeriodo(usuarioId, dataInicio, dataFim);
        return historicos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    private HistoricoResponseDTO converterParaResponseDTO(Historico historico) {
        // Determinar se é lucro ou prejuízo
        BigDecimal retorno = historico.calcularRetorno();
        String lucroPrejuizo = retorno.compareTo(BigDecimal.ZERO) >= 0 ? "LUCRO" : "PREJUIZO";
        
        return new HistoricoResponseDTO(
                historico.getId(),
                historico.getInvestimento().getId(),
                historico.getInvestimento().getNome(),
                historico.getInvestimento().getSimbolo(),
                historico.getUsuario().getId(),
                historico.getUsuario().getNomeUsuario(),
                historico.getTotalInvestido(),
                historico.getTotalRetornando(),
                retorno,
                historico.calcularPercentualRetorno(),
                lucroPrejuizo,
                historico.getMesAnoRegistro(),
                historico.getDataRegistro()
        );
    }
    
    /**
     * Calcula o resumo de uma lista de históricos
     */
    public ResumoHistoricoDTO calcularResumoHistoricos(List<HistoricoResponseDTO> historicos) {
        if (historicos.isEmpty()) {
            return new ResumoHistoricoDTO(0, BigDecimal.ZERO, BigDecimal.ZERO, 
                    BigDecimal.ZERO, BigDecimal.ZERO, "NEUTRO", 0, 0);
        }
        
        int quantidadeInvestimentos = historicos.size();
        BigDecimal totalInvestidoGeral = BigDecimal.ZERO;
        BigDecimal totalRetornandoGeral = BigDecimal.ZERO;
        int investimentosComLucro = 0;
        int investimentosComPrejuizo = 0;
        
        for (HistoricoResponseDTO historico : historicos) {
            totalInvestidoGeral = totalInvestidoGeral.add(historico.getTotalInvestido());
            totalRetornandoGeral = totalRetornandoGeral.add(historico.getTotalRetornando());
            
            if ("LUCRO".equals(historico.getLucroPrejuizo())) {
                investimentosComLucro++;
            } else if ("PREJUIZO".equals(historico.getLucroPrejuizo())) {
                investimentosComPrejuizo++;
            }
        }
        
        BigDecimal retornoGeral = totalRetornandoGeral.subtract(totalInvestidoGeral);
        
        BigDecimal percentualRetornoGeral = BigDecimal.ZERO;
        if (totalInvestidoGeral.compareTo(BigDecimal.ZERO) > 0) {
            percentualRetornoGeral = retornoGeral
                    .divide(totalInvestidoGeral, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        
        String lucroPrejuizoGeral = retornoGeral.compareTo(BigDecimal.ZERO) >= 0 ? "LUCRO" : "PREJUIZO";
        
        return new ResumoHistoricoDTO(
                quantidadeInvestimentos,
                totalInvestidoGeral,
                totalRetornandoGeral,
                retornoGeral,
                percentualRetornoGeral,
                lucroPrejuizoGeral,
                investimentosComLucro,
                investimentosComPrejuizo
        );
    }
    
    /**
     * Cria uma resposta com históricos e resumo
     */
    public HistoricoComResumoDTO criarHistoricoComResumo(List<HistoricoResponseDTO> historicos) {
        ResumoHistoricoDTO resumo = calcularResumoHistoricos(historicos);
        return new HistoricoComResumoDTO(historicos, resumo);
    }
}