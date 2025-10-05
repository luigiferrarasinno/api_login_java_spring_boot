package com.example.demo.init;

import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.repository.ComentarioRepository;
import com.example.demo.historico.model.Historico;
import com.example.demo.historico.repository.HistoricoRepository;
import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.model.Categoria;
import com.example.demo.investimento.model.Risco;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.playlist.model.Playlist;
import com.example.demo.playlist.repository.PlaylistRepository;
import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import com.example.demo.user.model.TipoPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;

/**
 * 🚀 Inicializador Centralizado do Sistema
 * 
 * Inicializa todos os dados necessários para o sistema funcionar:
 * 1. Usuários (Admin e User comum)
 * 2. Investimentos (ações, FIIs, renda fixa)
 * 3. Playlists de exemplo
 * 4. Comentários iniciais
 * 5. Históricos de investimentos (12 meses)
 * 6. Relacionamentos entre entidades
 */
@Component
@Order(1) // Executa primeiro
public class SystemInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioDAO usuarioDAO;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private InvestimentoRepository investimentoRepository;
    
    @Autowired
    private PlaylistRepository playlistRepository;
    
    @Autowired
    private ComentarioRepository comentarioRepository;
    
    @Autowired
    private HistoricoRepository historicoRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("\n🚀 ===== INICIALIZANDO SISTEMA DE INVESTIMENTOS ===== 🚀");
        
        // 1. Criar usuários primeiro (base para tudo)
        criarUsuarios();
        
        // 2. Criar investimentos (necessário para playlists e comentários)
        criarInvestimentos();
        
        // 3. Vincular investimentos aos usuários (posições iniciais)
        vincularInvestimentosAosUsuarios();
        
        // 4. Criar playlists (precisa de usuários e investimentos)
        criarPlaylistsExemplo();
        
        // 5. Criar comentários (precisa de usuários e investimentos)
        criarComentariosIniciais();
        
        // 6. Criar históricos de investimentos (precisa de usuários e investimentos)
        criarHistoricosIniciais();
        
        System.out.println("✅ ===== SISTEMA INICIALIZADO COM SUCESSO! ===== ✅\n");
        imprimirResumoInicializacao();
    }

    /**
     * 👥 ETAPA 1: Criar Usuários
     */
    private void criarUsuarios() {
        System.out.println("\n👥 Inicializando usuários...");
        
        // Admin
        if (usuarioDAO.findByEmail("admin@admin.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario("Admin Sistema");
            admin.setEmail("admin@admin.com");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setRole("ROLE_ADMIN");
            admin.setCpf(11111111111L);
            admin.setDt_nascimento(LocalDate.of(1985, 1, 15));
            admin.setUserIsActive(true);
            admin.setTipo(TipoPerfil.PERFIL_ARROJADO);
            admin.setSaldoCarteira(new BigDecimal("50000.00")); // 💰 R$ 50.000 para testes
            usuarioDAO.save(admin);
            System.out.println("✅ Admin criado: admin@admin.com / 123456");
        }
        
        // Usuário comum
        if (usuarioDAO.findByEmail("user@user.com").isEmpty()) {
            Usuario user = new Usuario();
            user.setNomeUsuario("João Silva");
            user.setEmail("user@user.com");
            user.setSenha(passwordEncoder.encode("123456"));
            user.setRole("ROLE_USER");
            user.setCpf(22222222222L);
            user.setDt_nascimento(LocalDate.of(1990, 5, 20));
            user.setUserIsActive(true);
            user.setTipo(TipoPerfil.PERFIL_MODERADO);
            user.setSaldoCarteira(new BigDecimal("25000.00")); // 💰 R$ 25.000 para testes
            usuarioDAO.save(user);
            System.out.println("✅ Usuário criado: user@user.com / 123456");
        }

        // Usuário investidora experiente
        if (usuarioDAO.findByEmail("maria@investidora.com").isEmpty()) {
            Usuario maria = new Usuario();
            maria.setNomeUsuario("Maria Investidora");
            maria.setEmail("maria@investidora.com");
            maria.setSenha(passwordEncoder.encode("123456"));
            maria.setRole("ROLE_USER");
            maria.setCpf(33333333333L);
            maria.setDt_nascimento(LocalDate.of(1988, 8, 10));
            maria.setUserIsActive(true);
            maria.setTipo(TipoPerfil.PERFIL_CONSERVADOR);
            maria.setSaldoCarteira(new BigDecimal("35000.00")); // 💰 R$ 35.000 para testes
            usuarioDAO.save(maria);
            System.out.println("✅ Usuária criada: maria@investidora.com / 123456");
        }
    }

    /**
     * 💰 ETAPA 2: Criar Investimentos
     */
    private void criarInvestimentos() {
        System.out.println("\n💰 Inicializando investimentos...");
        
        if (investimentoRepository.count() > 0) {
            System.out.println("⏭️  Investimentos já existem, pulando criação...");
            return;
        }

        // 📈 AÇÕES BRASILEIRAS
        criarAcao("Petróleo Brasileiro S.A.", "PETR4", new BigDecimal("28.50"), "Maior empresa de energia do Brasil", Risco.MEDIO, new BigDecimal("8.5"), 4, new BigDecimal("2.35"), 100000L, 45000L);
        criarAcao("Vale S.A.", "VALE3", new BigDecimal("65.20"), "Maior mineradora das Américas", Risco.ALTO, new BigDecimal("12.3"), 2, new BigDecimal("-1.80"), 80000L, 25000L);
        criarAcao("Itaú Unibanco Holding S.A.", "ITUB4", new BigDecimal("32.40"), "Maior banco privado do Brasil", Risco.MEDIO, new BigDecimal("6.8"), 4, new BigDecimal("0.95"), 150000L, 75000L);
        criarAcao("Banco do Brasil S.A.", "BBAS3", new BigDecimal("45.30"), "Maior banco público do país", Risco.MEDIO, new BigDecimal("9.2"), 4, new BigDecimal("1.45"), 90000L, 40000L);
        criarAcao("Ambev S.A.", "ABEV3", new BigDecimal("14.80"), "Maior cervejaria da América Latina", Risco.BAIXO, new BigDecimal("4.5"), 2, new BigDecimal("0.65"), 200000L, 85000L);
        
        // 🏢 FUNDOS IMOBILIÁRIOS
        criarFII("CSHG Real Estate Fund", "HGLG11", new BigDecimal("105.40"), "FII de shoppings centers", new BigDecimal("10.2"), 12, new BigDecimal("0.85"), 50000L, 18000L);
        criarFII("Maxi Renda", "MXRF11", new BigDecimal("9.85"), "FII diversificado de renda", new BigDecimal("9.6"), 12, new BigDecimal("-0.45"), 30000L, 12000L);
        criarFII("XP Log", "XPLG11", new BigDecimal("98.50"), "FII de galpões logísticos", new BigDecimal("8.8"), 12, new BigDecimal("1.25"), 40000L, 15000L);
        
        // 💎 RENDA FIXA
        criarRendaFixa("Tesouro Direto Selic", "TD-SELIC", new BigDecimal("102.50"), "Título público indexado à Selic", Risco.BAIXO, new BigDecimal("0.15"), 1000000L, 800000L);
        criarRendaFixa("CDB Banco Inter", "CDB-INTER", new BigDecimal("1000.00"), "CDB com liquidez diária", Risco.BAIXO, new BigDecimal("0.08"), 500000L, 350000L);
        criarRendaFixa("LCI Nubank", "LCI-NU", new BigDecimal("5000.00"), "Letra de Crédito Imobiliário", Risco.BAIXO, new BigDecimal("0.12"), 200000L, 150000L);
        
        System.out.println("✅ " + investimentoRepository.count() + " investimentos criados com sucesso!");
    }

    private void criarAcao(String nome, String simbolo, BigDecimal preco, String descricao, Risco risco, BigDecimal yield, int frequencia, BigDecimal variacao, Long quantidadeTotal, Long quantidadeDisponivel) {
        Investimento acao = new Investimento();
        acao.setNome(nome);
        acao.setSimbolo(simbolo);
        acao.setCategoria(Categoria.RENDA_VARIAVEL);
        acao.setPrecoBase(preco);
        acao.setPrecoAtual(preco);
        acao.setVariacaoPercentual(variacao); // 📈 Variação específica da última semana
        acao.setUltimaAtualizacaoPreco(LocalDateTime.now().minusHours((long)(Math.random() * 72))); // ⏰ Última atualização há 1-3 dias
        acao.setDescricao(descricao);
        acao.setData(LocalDate.now().minusDays(30));
        acao.setLiquidez("D+2");
        acao.setDividendYield(yield);
        acao.setFrequenciaDividendo(frequencia);
        acao.setAtivo(true);
        acao.setRisco(risco);
        acao.setQuantidadeTotal(quantidadeTotal); // 📊 Quantidade total específica
        acao.setQuantidadeDisponivel(quantidadeDisponivel); // 🔥 Quantidade disponível específica
        investimentoRepository.save(acao);
    }

    private void criarFII(String nome, String simbolo, BigDecimal preco, String descricao, BigDecimal yield, int frequencia, BigDecimal variacao, Long quantidadeTotal, Long quantidadeDisponivel) {
        Investimento fii = new Investimento();
        fii.setNome(nome);
        fii.setSimbolo(simbolo);
        fii.setCategoria(Categoria.FUNDO_IMOBILIARIO);
        fii.setPrecoBase(preco);
        fii.setPrecoAtual(preco);
        fii.setVariacaoPercentual(variacao); // 📈 Variação específica da última semana
        fii.setUltimaAtualizacaoPreco(LocalDateTime.now().minusHours((long)(Math.random() * 48))); // ⏰ Última atualização há 1-2 dias
        fii.setDescricao(descricao);
        fii.setData(LocalDate.now().minusDays(20));
        fii.setLiquidez("D+1");
        fii.setDividendYield(yield);
        fii.setFrequenciaDividendo(frequencia);
        fii.setAtivo(true);
        fii.setRisco(Risco.MEDIO);
        fii.setQuantidadeTotal(quantidadeTotal); // 📊 Quantidade total específica
        fii.setQuantidadeDisponivel(quantidadeDisponivel); // 🔥 Quantidade disponível específica
        investimentoRepository.save(fii);
    }

    private void criarRendaFixa(String nome, String simbolo, BigDecimal preco, String descricao, Risco risco, BigDecimal variacao, Long quantidadeTotal, Long quantidadeDisponivel) {
        Investimento rf = new Investimento();
        rf.setNome(nome);
        rf.setSimbolo(simbolo);
        rf.setCategoria(Categoria.RENDA_FIXA);
        rf.setPrecoBase(preco);
        rf.setPrecoAtual(preco);
        rf.setVariacaoPercentual(variacao); // 📈 Variação específica (menor para renda fixa)
        rf.setUltimaAtualizacaoPreco(LocalDateTime.now().minusHours((long)(Math.random() * 24))); // ⏰ Última atualização há 1 dia
        rf.setDescricao(descricao);
        rf.setData(LocalDate.now().minusDays(10));
        rf.setLiquidez("Diária");
        rf.setDividendYield(BigDecimal.ZERO); // Renda fixa não paga dividendos
        rf.setFrequenciaDividendo(0);
        rf.setAtivo(true);
        rf.setRisco(risco);
        rf.setQuantidadeTotal(quantidadeTotal); // 📊 Quantidade total específica
        rf.setQuantidadeDisponivel(quantidadeDisponivel); // 🔥 Quantidade disponível específica
        investimentoRepository.save(rf);
    }

    /**
     * 🔗 ETAPA 3: Vincular Investimentos aos Usuários
     */
    private void vincularInvestimentosAosUsuarios() {
        System.out.println("\n🔗 Vinculando investimentos aos usuários...");
        
        try {
            Usuario user = usuarioDAO.findByEmail("user@user.com").orElse(null);
            Usuario maria = usuarioDAO.findByEmail("maria@investidora.com").orElse(null);
            List<Investimento> investimentos = investimentoRepository.findAll();
            
            if (user != null && !investimentos.isEmpty()) {
                // João Silva tem alguns investimentos básicos
                if (investimentos.size() >= 3) {
                    System.out.println("💼 Vinculando investimentos para João Silva...");
                }
            }
            
            if (maria != null && !investimentos.isEmpty()) {
                // Maria tem uma carteira mais diversificada
                if (investimentos.size() >= 5) {
                    System.out.println("💼 Vinculando investimentos para Maria Investidora...");
                }
            }
            
            System.out.println("✅ Investimentos vinculados com sucesso!");
            
        } catch (Exception e) {
            System.err.println("⚠️  Erro ao vincular investimentos: " + e.getMessage());
        }
    }

    /**
     * 🎵 ETAPA 4: Criar Playlists de Exemplo
     */
    private void criarPlaylistsExemplo() {
        System.out.println("\n🎵 Inicializando playlists...");
        
        if (playlistRepository.count() > 0) {
            System.out.println("⏭️  Playlists já existem, pulando criação...");
            return;
        }
        
        try {
            Usuario admin = usuarioDAO.findByEmail("admin@admin.com").orElse(null);
            Usuario user = usuarioDAO.findByEmail("user@user.com").orElse(null);
            Usuario maria = usuarioDAO.findByEmail("maria@investidora.com").orElse(null);
            List<Investimento> investimentos = investimentoRepository.findAll();

            if (admin == null || user == null || maria == null || investimentos.isEmpty()) {
                System.out.println("⚠️  Dados insuficientes para criar playlists");
                return;
            }

            // 🎵 Playlist 1: "Top Dividendos 2024" (Admin - Pública)
            Playlist topDividendos = new Playlist();
            topDividendos.setNome("Top Dividendos 2024 💰");
            topDividendos.setDescricao("As melhores ações e FIIs pagadores de dividendos para 2024. Seleção cuidadosa com foco em yield e consistência.");
            topDividendos.setCriador(admin);
            topDividendos.setPublica(true);
            topDividendos.setPermiteColaboracao(false);
            topDividendos.setDataCriacao(LocalDateTime.now().minusDays(15));
            
            if (investimentos.size() >= 4) {
                topDividendos.getInvestimentos().addAll(investimentos.subList(0, 4));
            }
            playlistRepository.save(topDividendos);

            // 🎵 Playlist 2: "Carteira Conservadora" (Maria - Privada)
            Playlist conservadora = new Playlist();
            conservadora.setNome("Minha Carteira Conservadora 🛡️");
            conservadora.setDescricao("Investimentos de baixo risco para preservação de capital e renda passiva estável.");
            conservadora.setCriador(maria);
            conservadora.setPublica(false);
            conservadora.setPermiteColaboracao(false);
            conservadora.setDataCriacao(LocalDateTime.now().minusDays(10));
            
            if (investimentos.size() >= 7) {
                conservadora.getInvestimentos().addAll(investimentos.subList(2, 5));
            }
            playlistRepository.save(conservadora);

            // 🎵 Playlist 3: "FIIs para Iniciantes" (Admin - Pública Colaborativa)
            Playlist fiis = new Playlist();
            fiis.setNome("FIIs para Iniciantes 🏢");
            fiis.setDescricao("Os melhores Fundos Imobiliários para quem está começando. Colaboração aberta!");
            fiis.setCriador(admin);
            fiis.setPublica(true);
            fiis.setPermiteColaboracao(true);
            fiis.setDataCriacao(LocalDateTime.now().minusDays(5));
            
            // Adicionar apenas FIIs
            investimentos.stream()
                .filter(inv -> inv.getCategoria() == Categoria.FUNDO_IMOBILIARIO)
                .forEach(fii -> fiis.getInvestimentos().add(fii));
            playlistRepository.save(fiis);

            // 🎵 Playlist 4: "Apostas Arriscadas" (User - Pública)
            Playlist arriscadas = new Playlist();
            arriscadas.setNome("Apostas Arriscadas 🚀");
            arriscadas.setDescricao("Para quem gosta de adrenalina! Alto risco, alto retorno. Invista por sua conta e risco!");
            arriscadas.setCriador(user);
            arriscadas.setPublica(true);
            arriscadas.setPermiteColaboracao(true);
            arriscadas.setDataCriacao(LocalDateTime.now().minusDays(3));
            
            // Adicionar ações de alto risco
            investimentos.stream()
                .filter(inv -> inv.getRisco() == Risco.ALTO)
                .forEach(acao -> arriscadas.getInvestimentos().add(acao));
            playlistRepository.save(arriscadas);

            // 👥 Criar relacionamentos de seguidores
            topDividendos.getSeguidores().add(user);
            topDividendos.getSeguidores().add(maria);
            playlistRepository.save(topDividendos);
            
            fiis.getSeguidores().add(user);
            fiis.getSeguidores().add(maria);
            playlistRepository.save(fiis);

            arriscadas.getSeguidores().add(admin);
            playlistRepository.save(arriscadas);

            System.out.println("✅ " + playlistRepository.count() + " playlists criadas com relacionamentos!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar playlists: " + e.getMessage());
        }
    }

    /**
     * 💬 ETAPA 5: Criar Comentários Iniciais
     */
    private void criarComentariosIniciais() {
        System.out.println("\n💬 Inicializando comentários...");
        
        if (comentarioRepository.count() > 0) {
            System.out.println("⏭️  Comentários já existem, pulando criação...");
            return;
        }

        try {
            Usuario admin = usuarioDAO.findByEmail("admin@admin.com").orElse(null);
            Usuario user = usuarioDAO.findByEmail("user@user.com").orElse(null);
            Usuario maria = usuarioDAO.findByEmail("maria@investidora.com").orElse(null);
            List<Investimento> investimentos = investimentoRepository.findAll();

            if (admin == null || user == null || maria == null || investimentos.isEmpty()) {
                System.out.println("⚠️  Dados insuficientes para criar comentários");
                return;
            }

            // Comentários sobre PETR4
            Investimento petr4 = investimentos.stream()
                .filter(inv -> "PETR4".equals(inv.getSimbolo()))
                .findFirst().orElse(null);
            
            if (petr4 != null) {
                criarComentario(admin, petr4, "Excelente oportunidade com os preços atuais do petróleo! 📈");
                criarComentario(user, petr4, "Boa para dividendos, mas atenção à volatilidade do setor.");
                criarComentario(maria, petr4, "Prefiro manter uma posição pequena, muito risco geopolítico.");
            }

            // Comentários sobre VALE3
            Investimento vale3 = investimentos.stream()
                .filter(inv -> "VALE3".equals(inv.getSimbolo()))
                .findFirst().orElse(null);
                
            if (vale3 != null) {
                criarComentario(maria, vale3, "A demanda por minério está forte, especialmente da China.");
                criarComentario(admin, vale3, "ESG melhorou muito, mas ainda há trabalho a fazer.");
            }

            // Comentários sobre FII
            Investimento hglg11 = investimentos.stream()
                .filter(inv -> "HGLG11".equals(inv.getSimbolo()))
                .findFirst().orElse(null);
                
            if (hglg11 != null) {
                criarComentario(user, hglg11, "Meu primeiro FII! Dividendos mensais são ótimos 💰");
                criarComentario(maria, hglg11, "Shopping centers estão se recuperando bem pós-pandemia.");
            }

            System.out.println("✅ " + comentarioRepository.count() + " comentários criados!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar comentários: " + e.getMessage());
        }
    }

    private void criarComentario(Usuario usuario, Investimento investimento, String texto) {
        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setInvestimento(investimento);
        comentario.setConteudo(texto);
        comentario.setDataCriacao(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        comentarioRepository.save(comentario);
    }

    /**
     * � ETAPA 6: Criar Históricos de Investimentos
     */
    private void criarHistoricosIniciais() {
        System.out.println("\n📈 Inicializando históricos de investimentos...");
        
        if (historicoRepository.count() > 0) {
            System.out.println("⏭️  Históricos já existem, pulando criação...");
            return;
        }

        try {
            Usuario user = usuarioDAO.findByEmail("user@user.com").orElse(null);
            Usuario maria = usuarioDAO.findByEmail("maria@investidora.com").orElse(null);
            Usuario admin = usuarioDAO.findByEmail("admin@admin.com").orElse(null);
            List<Investimento> investimentos = investimentoRepository.findAll();

            if (user == null || maria == null || admin == null || investimentos.isEmpty()) {
                System.out.println("⚠️  Dados insuficientes para criar históricos");
                return;
            }

            Random random = new Random();
            YearMonth mesAtual = YearMonth.now();
            
            // Criar históricos para os últimos 12 meses
            for (int mesesAtras = 11; mesesAtras >= 0; mesesAtras--) {
                YearMonth mesReferencia = mesAtual.minusMonths(mesesAtras);
                
                // 📊 Históricos para João Silva (user@user.com)
                criarHistoricosParaUsuario(user, investimentos, mesReferencia, random, "conservador");
                
                // 📊 Históricos para Maria Investidora
                criarHistoricosParaUsuario(maria, investimentos, mesReferencia, random, "moderado");
                
                // 📊 Históricos para Admin (mais diversificado)
                criarHistoricosParaUsuario(admin, investimentos, mesReferencia, random, "agressivo");
            }

            System.out.println("✅ " + historicoRepository.count() + " registros de histórico criados!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar históricos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria múltiplos históricos para um usuário em um mês específico
     */
    private void criarHistoricosParaUsuario(Usuario usuario, List<Investimento> investimentos, 
                                           YearMonth mesReferencia, Random random, String perfil) {
        
        // Determinar quantos investimentos este usuário terá neste mês
        int maxInvestimentos = switch (perfil) {
            case "conservador" -> Math.min(3, investimentos.size()); // João - mais conservador
            case "moderado" -> Math.min(5, investimentos.size());    // Maria - moderado
            case "agressivo" -> Math.min(8, investimentos.size());   // Admin - mais diversificado
            default -> 3;
        };
        
        // Selecionar investimentos aleatórios para este usuário neste mês
        List<Investimento> investimentosEscolhidos = investimentos.stream()
            .limit(maxInvestimentos)
            .toList();
        
        for (int i = 0; i < Math.min(random.nextInt(3) + 2, maxInvestimentos); i++) { // 2 a 4 registros por mês
            Investimento investimento = investimentosEscolhidos.get(random.nextInt(investimentosEscolhidos.size()));
            
            // Calcular valores baseados no perfil e mês
            BigDecimal valorBase = switch (perfil) {
                case "conservador" -> new BigDecimal(random.nextDouble(1000, 5000)); // R$ 1.000 - 5.000
                case "moderado" -> new BigDecimal(random.nextDouble(2000, 8000));    // R$ 2.000 - 8.000  
                case "agressivo" -> new BigDecimal(random.nextDouble(5000, 15000));  // R$ 5.000 - 15.000
                default -> new BigDecimal(1000);
            };
            
            // Simular variação de mercado (alguns meses bons, outros ruins)
            double variacaoMercado = switch (mesReferencia.getMonthValue()) {
                case 1, 2, 11, 12 -> random.nextDouble(-0.15, 0.10); // Inverno: mais volátil
                case 3, 4, 9, 10 -> random.nextDouble(-0.08, 0.15);   // Primavera/Outono: moderado
                case 5, 6, 7, 8 -> random.nextDouble(-0.05, 0.12);    // Verão: mais estável
                default -> random.nextDouble(-0.10, 0.10);
            };
            
            // Ajustar variação baseada no tipo de investimento
            if (investimento.getCategoria() == Categoria.RENDA_FIXA) {
                variacaoMercado = Math.abs(variacaoMercado) * 0.3; // Renda fixa sempre positiva e menor
            } else if (investimento.getRisco() == Risco.ALTO) {
                variacaoMercado *= 1.5; // Alto risco = maior variação
            }
            
            BigDecimal totalInvestido = valorBase.setScale(2, java.math.RoundingMode.HALF_UP);
            BigDecimal totalRetornando = totalInvestido
                .multiply(BigDecimal.valueOf(1 + variacaoMercado))
                .setScale(2, java.math.RoundingMode.HALF_UP);
            
            // Verificar se já existe histórico para este usuário, investimento e mês
            if (historicoRepository.findByUsuarioIdAndInvestimentoIdAndMesAnoRegistro(
                    usuario.getId(), investimento.getId(), mesReferencia).isEmpty()) {
                
                Historico historico = new Historico(investimento, usuario, totalInvestido, 
                                                   totalRetornando, mesReferencia);
                
                // Definir data de registro variada dentro do mês (simular quando foi feito o registro)
                LocalDate dataRegistro = mesReferencia.atDay(random.nextInt(28) + 1);
                historico.setDataRegistro(dataRegistro);
                
                historicoRepository.save(historico);
                
                // Log detalhado para debug
                if (random.nextInt(10) == 0) { // Log apenas 10% para não poluir
                    System.out.printf("📊 %s - %s (%s): R$ %.2f → R$ %.2f (%.1f%%)%n", 
                        usuario.getNomeUsuario(), investimento.getSimbolo(), 
                        mesReferencia, totalInvestido, totalRetornando, 
                        variacaoMercado * 100);
                }
            }
        }
    }

    /**
     * �📊 Imprimir Resumo da Inicialização
     */
    private void imprimirResumoInicializacao() {
        System.out.println("📊 ===== RESUMO DA INICIALIZAÇÃO ===== 📊");
        System.out.println("👥 Usuários: " + ((List<?>) usuarioDAO.findAll()).size());
        System.out.println("💰 Investimentos: " + investimentoRepository.count());
        System.out.println("🎵 Playlists: " + playlistRepository.count());
        System.out.println("💬 Comentários: " + comentarioRepository.count());
        System.out.println("📈 Históricos: " + historicoRepository.count() + " (últimos 12 meses)");
        System.out.println("");
        System.out.println("🔑 CREDENCIAIS DE ACESSO:");
        System.out.println("   👨‍💼 Admin: admin@admin.com / 123456 (R$ 50.000)");
        System.out.println("   👤 User: user@user.com / 123456 (R$ 25.000)");
        System.out.println("   👩‍💼 Maria: maria@investidora.com / 123456 (R$ 35.000)");
        System.out.println("");
        System.out.println("🎯 TESTE OS DIVIDENDOS AUTOMÁTICOS:");
        System.out.println("   📈 Ações com dividendos: PETR4 (8.5%), VALE3 (12.3%), ITUB4 (6.8%), BBAS3 (9.2%), ABEV3 (4.5%)");
        System.out.println("   🏢 FIIs com dividendos: HGLG11 (10.2%), MXRF11 (9.6%), XPLG11 (8.8%)");
        System.out.println("   💡 Compre qualquer ação/FII e receba dividendo IMEDIATO!");
        System.out.println("");
        System.out.println("🌐 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("🎵 Teste as playlists após fazer login!");
        System.out.println("");
        System.out.println("� HISTÓRICOS GERADOS:");
        System.out.println("   📊 12 meses de dados para cada usuário");
        System.out.println("   👤 João Silva: 2-4 investimentos por mês (conservador)");
        System.out.println("   👩‍💼 Maria: 2-5 investimentos por mês (moderado)");  
        System.out.println("   👨‍💼 Admin: 2-8 investimentos por mês (agressivo)");
        System.out.println("   💡 Use /api/historico/usuario/{id} para ver o histórico completo!");
        System.out.println("");
        System.out.println("�🛢️ Console do Banco H2:");
        System.out.println("Acesse o banco de dados em memória para consultas SQL:");
        System.out.println("   📡 URL: http://localhost:8080/h2-console");
        System.out.println("");
        System.out.println("🔐 Credenciais de Acesso H2:");
        System.out.println("   🗄️ JDBC URL: jdbc:h2:mem:fellerdb");
        System.out.println("   👤 Username: Admin");
        System.out.println("   🔑 Password: Fiap123");
        System.out.println("=====================================");
    }
}