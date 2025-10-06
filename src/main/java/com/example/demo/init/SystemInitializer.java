package com.example.demo.init;

import com.example.demo.comentarios.model.Comentario;
import com.example.demo.comentarios.repository.ComentarioRepository;
import com.example.demo.extrato.model.Extrato;
import com.example.demo.extrato.model.TipoTransacao;
import com.example.demo.extrato.repository.ExtratoRepository;
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
    private ExtratoRepository extratoRepository;

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
        
        // 6. Criar extratos de investimentos para os últimos 12 meses (precisa de usuários e investimentos)
        criarExtratosIniciais();
        
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
     * 📊 ETAPA 6: Criar Extratos de Investimentos para os Últimos 12 Meses
     */
    private void criarExtratosIniciais() {
        System.out.println("\n� Inicializando extratos de investimentos para 12 meses...");
        
        // Verificar se já existem extratos de investimento
        long extratosInvestimento = extratoRepository.count();
        if (extratosInvestimento > 20) { // Se já tem mais de 20 registros, provavelmente já foi inicializado
            System.out.println("⏭️  Extratos de investimento já existem, pulando criação...");
            return;
        }

        try {
            Usuario user = usuarioDAO.findByEmail("user@user.com").orElse(null);
            Usuario maria = usuarioDAO.findByEmail("maria@investidora.com").orElse(null);
            Usuario admin = usuarioDAO.findByEmail("admin@admin.com").orElse(null);
            List<Investimento> investimentos = investimentoRepository.findAll();

            if (user == null || maria == null || admin == null || investimentos.isEmpty()) {
                System.out.println("⚠️  Dados insuficientes para criar extratos de investimentos");
                return;
            }

            Random random = new Random();
            YearMonth mesAtual = YearMonth.now();
            
            // Criar extratos para os últimos 12 meses (até o mês passado)
            for (int mesesAtras = 12; mesesAtras >= 1; mesesAtras--) {
                YearMonth mesReferencia = mesAtual.minusMonths(mesesAtras);
                
                // 📊 Extratos para João Silva (user@user.com) - Perfil Conservador
                criarExtratosParaUsuario(user, investimentos, mesReferencia, random, "conservador");
                
                // 📊 Extratos para Maria Investidora - Perfil Moderado
                criarExtratosParaUsuario(maria, investimentos, mesReferencia, random, "moderado");
                
                // 📊 Extratos para Admin - Perfil Agressivo
                criarExtratosParaUsuario(admin, investimentos, mesReferencia, random, "agressivo");
            }

            System.out.println("✅ " + extratoRepository.count() + " registros de extrato criados para 12 meses!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar extratos de investimentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria múltiplos extratos de investimento para um usuário em um mês específico
     */
    private void criarExtratosParaUsuario(Usuario usuario, List<Investimento> investimentos, 
                                         YearMonth mesReferencia, Random random, String perfil) {
        
        // Determinar quantos investimentos este usuário fará neste mês baseado no perfil
        int maxOperacoesMes = switch (perfil) {
            case "conservador" -> random.nextInt(2) + 2; // 2-3 operações por mês
            case "moderado" -> random.nextInt(3) + 3;    // 3-5 operações por mês
            case "agressivo" -> random.nextInt(4) + 4;   // 4-7 operações por mês
            default -> 2;
        };
        
        // Selecionar investimentos baseado no perfil
        List<Investimento> investimentosEscolhidos = switch (perfil) {
            case "conservador" -> investimentos.stream()
                .filter(inv -> inv.getRisco() != Risco.ALTO)
                .limit(4).toList(); // Evita alto risco
            case "moderado" -> investimentos.stream()
                .limit(6).toList(); // Mix equilibrado
            case "agressivo" -> investimentos; // Todos os investimentos
            default -> investimentos.subList(0, Math.min(3, investimentos.size()));
        };
        
        BigDecimal saldoAtual = usuario.getSaldoCarteira();
        
        for (int i = 0; i < maxOperacoesMes && !investimentosEscolhidos.isEmpty(); i++) {
            Investimento investimento = investimentosEscolhidos.get(random.nextInt(investimentosEscolhidos.size()));
            
            // Calcular valores baseados no perfil e investimento
            BigDecimal valorOperacao = calcularValorOperacao(perfil, investimento, random);
            BigDecimal quantidade = valorOperacao.divide(investimento.getPrecoAtual(), 6, java.math.RoundingMode.HALF_UP);
            
            // Simular compra do investimento
            BigDecimal saldoAnterior = saldoAtual;
            saldoAtual = saldoAtual.subtract(valorOperacao);
            
            // Criar extrato de compra
            Extrato extratoCompra = new Extrato();
            extratoCompra.setUsuario(usuario);
            extratoCompra.setInvestimento(investimento);
            extratoCompra.setTipoTransacao(TipoTransacao.COMPRA_ACAO);
            extratoCompra.setQuantidade(quantidade);
            extratoCompra.setPrecoUnitario(investimento.getPrecoAtual());
            extratoCompra.setValorTotal(valorOperacao.negate()); // Negativo pois é saída
            extratoCompra.setSaldoAnterior(saldoAnterior);
            extratoCompra.setSaldoAtual(saldoAtual);
            extratoCompra.setDescricao(String.format("Compra de %s cotas de %s", 
                quantidade.setScale(2, java.math.RoundingMode.HALF_UP), investimento.getSimbolo()));
            
            // Data aleatória dentro do mês
            LocalDate dataOperacao = mesReferencia.atDay(random.nextInt(28) + 1);
            extratoCompra.setDataTransacao(dataOperacao.atTime(9 + random.nextInt(8), random.nextInt(60)));
            
            extratoRepository.save(extratoCompra);
            
            // Simular dividendo se aplicável (apenas para ações e FIIs)
            if (investimento.getDividendYield() != null && investimento.getDividendYield().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal dividendo = valorOperacao
                    .multiply(investimento.getDividendYield())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP); // Mensal
                
                if (dividendo.compareTo(BigDecimal.valueOf(0.01)) > 0) { // Só se for maior que 1 centavo
                    BigDecimal saldoAntesDividendo = saldoAtual;
                    saldoAtual = saldoAtual.add(dividendo);
                    
                    Extrato extratoDividendo = new Extrato();
                    extratoDividendo.setUsuario(usuario);
                    extratoDividendo.setInvestimento(investimento);
                    extratoDividendo.setTipoTransacao(TipoTransacao.DIVIDENDO_RECEBIDO);
                    extratoDividendo.setQuantidade(quantidade);
                    extratoDividendo.setPrecoUnitario(BigDecimal.ZERO);
                    extratoDividendo.setValorTotal(dividendo); // Positivo pois é entrada
                    extratoDividendo.setSaldoAnterior(saldoAntesDividendo);
                    extratoDividendo.setSaldoAtual(saldoAtual);
                    extratoDividendo.setDescricao(String.format("Dividendo de %s (%.2f%% yield)", 
                        investimento.getSimbolo(), investimento.getDividendYield()));
                    
                    // Dividendo alguns dias após a compra
                    extratoDividendo.setDataTransacao(dataOperacao.plusDays(random.nextInt(15) + 5)
                        .atTime(10 + random.nextInt(6), random.nextInt(60)));
                    
                    extratoRepository.save(extratoDividendo);
                }
            }
            
            // Algumas vezes simular venda (20% de chance)
            if (random.nextInt(5) == 0) {
                // Simular variação de preço para a venda
                double variacaoPreco = switch (investimento.getRisco()) {
                    case BAIXO -> random.nextDouble(-0.05, 0.08);   // -5% a +8%
                    case MEDIO -> random.nextDouble(-0.12, 0.15);   // -12% a +15%
                    case ALTO -> random.nextDouble(-0.25, 0.30);    // -25% a +30%
                };
                
                BigDecimal precoVenda = investimento.getPrecoAtual()
                    .multiply(BigDecimal.valueOf(1 + variacaoPreco))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
                
                BigDecimal valorVenda = quantidade.multiply(precoVenda);
                BigDecimal saldoAntesVenda = saldoAtual;
                saldoAtual = saldoAtual.add(valorVenda);
                
                Extrato extratoVenda = new Extrato();
                extratoVenda.setUsuario(usuario);
                extratoVenda.setInvestimento(investimento);
                extratoVenda.setTipoTransacao(TipoTransacao.VENDA_ACAO);
                extratoVenda.setQuantidade(quantidade);
                extratoVenda.setPrecoUnitario(precoVenda);
                extratoVenda.setValorTotal(valorVenda); // Positivo pois é entrada
                extratoVenda.setSaldoAnterior(saldoAntesVenda);
                extratoVenda.setSaldoAtual(saldoAtual);
                extratoVenda.setDescricao(String.format("Venda de %s cotas de %s (%.1f%%)", 
                    quantidade.setScale(2, java.math.RoundingMode.HALF_UP), 
                    investimento.getSimbolo(), variacaoPreco * 100));
                
                // Venda alguns dias/semanas após a compra
                extratoVenda.setDataTransacao(dataOperacao.plusDays(random.nextInt(20) + 5)
                    .atTime(10 + random.nextInt(6), random.nextInt(60)));
                
                extratoRepository.save(extratoVenda);
            }
        }
        
        // Log ocasional para acompanhar o progresso
        if (random.nextInt(8) == 0) {
            System.out.printf("📊 %s - %s: %d operações criadas%n", 
                usuario.getNomeUsuario(), mesReferencia, maxOperacoesMes);
        }
    }

    /**
     * Calcula valor da operação baseado no perfil do usuário e tipo de investimento
     */
    private BigDecimal calcularValorOperacao(String perfil, Investimento investimento, Random random) {
        // Valor base conforme perfil
        double valorBase = switch (perfil) {
            case "conservador" -> random.nextDouble(500, 2000);   // R$ 500 - 2.000
            case "moderado" -> random.nextDouble(1000, 4000);     // R$ 1.000 - 4.000
            case "agressivo" -> random.nextDouble(2000, 8000);    // R$ 2.000 - 8.000
            default -> 1000;
        };
        
        // Ajustar baseado no tipo de investimento
        double multiplicador = switch (investimento.getCategoria()) {
            case RENDA_FIXA -> random.nextDouble(1.2, 2.0);       // Renda fixa: valores maiores
            case FUNDO_IMOBILIARIO -> random.nextDouble(0.8, 1.5); // FIIs: valores médios
            case RENDA_VARIAVEL -> random.nextDouble(0.6, 1.3);    // Ações: mais variado
            default -> 1.0; // Default multiplicador
        };
        
        return BigDecimal.valueOf(valorBase * multiplicador)
            .setScale(2, java.math.RoundingMode.HALF_UP);
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
        System.out.println("� Extratos: " + extratoRepository.count() + " (últimos 12 meses com compras, vendas e dividendos)");
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
        System.out.println("📊 EXTRATOS GERADOS (12 MESES):");
        System.out.println("   � Compras, vendas e dividendos simulados realisticamente");
        System.out.println("   👤 João Silva: 2-3 operações/mês (conservador - evita alto risco)");
        System.out.println("   👩‍💼 Maria: 3-5 operações/mês (moderado - mix equilibrado)");  
        System.out.println("   👨‍💼 Admin: 4-7 operações/mês (agressivo - todos investimentos)");
        System.out.println("   💡 Use /api/extrato/resumo para ver análise de lucro/prejuízo!");
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