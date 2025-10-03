package com.example.demo.investimento.init;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.model.Categoria;
import com.example.demo.investimento.model.Risco;
import com.example.demo.investimento.repository.InvestimentoRepository;
import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InvestimentoInitializer implements CommandLineRunner {

    private final InvestimentoRepository investimentoRepository;
    private final UsuarioDAO usuarioDAO;

    public InvestimentoInitializer(InvestimentoRepository investimentoRepository, UsuarioDAO usuarioDAO) {
        this.investimentoRepository = investimentoRepository;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (investimentoRepository.count() == 0) { // evita duplicar na reinicialização

            LocalDateTime now = LocalDateTime.now();

            // Tesouro Direto - Renda Fixa
            Investimento i1 = new Investimento();
            i1.setNome("Tesouro Direto");
            i1.setSimbolo("TD");
            i1.setCategoria(Categoria.RENDA_FIXA);
            i1.setPrecoBase(new BigDecimal("100.00"));
            i1.setPrecoAtual(new BigDecimal("102.50"));
            i1.setVariacaoPercentual(new BigDecimal("2.50"));
            i1.setDescricao("Investimento seguro em títulos do governo");
            i1.setData(LocalDate.of(2025, 6, 10));
            i1.setLiquidez("Diária");
            i1.setDividendYield(new BigDecimal("0.00")); // Tesouro não paga dividendo
            i1.setFrequenciaDividendo(0);
            i1.setAtivo(true);
            i1.setRisco(Risco.BAIXO);
            i1.setQuantidadeTotal(1000000L); // 1 milhão de títulos
            i1.setQuantidadeDisponivel(850000L); // 850 mil disponíveis
            i1.setUltimaAtualizacaoPreco(now);
            i1.setCreatedAt(now);
            i1.setUpdatedAt(now);

            // Ações Vale - Renda Variável
            Investimento i2 = new Investimento();
            i2.setNome("Ações Vale");
            i2.setSimbolo("VALE3");
            i2.setCategoria(Categoria.RENDA_VARIAVEL);
            i2.setPrecoBase(new BigDecimal("65.00"));
            i2.setPrecoAtual(new BigDecimal("68.75"));
            i2.setVariacaoPercentual(new BigDecimal("5.77"));
            i2.setDescricao("Investimento em ações da Vale do Rio Doce");
            i2.setData(LocalDate.of(2025, 5, 20));
            i2.setLiquidez("Alta");
            i2.setDividendYield(new BigDecimal("8.50")); // 8.5% ao ano
            i2.setFrequenciaDividendo(4); // Trimestral
            i2.setAtivo(true);
            i2.setRisco(Risco.ALTO);
            i2.setQuantidadeTotal(500000L); // 500 mil ações
            i2.setQuantidadeDisponivel(275000L); // 275 mil disponíveis
            i2.setUltimaAtualizacaoPreco(now);
            i2.setCreatedAt(now);
            i2.setUpdatedAt(now);

            // Fundo Imobiliário
            Investimento i3 = new Investimento();
            i3.setNome("Fundo Imobiliário HGLG11");
            i3.setSimbolo("HGLG11");
            i3.setCategoria(Categoria.FUNDO);
            i3.setPrecoBase(new BigDecimal("160.00"));
            i3.setPrecoAtual(new BigDecimal("165.20"));
            i3.setVariacaoPercentual(new BigDecimal("3.25"));
            i3.setDescricao("Fundo de investimento imobiliário com foco em logística");
            i3.setData(LocalDate.of(2025, 4, 15));
            i3.setLiquidez("Média");
            i3.setDividendYield(new BigDecimal("6.20")); // 6.2% ao ano
            i3.setFrequenciaDividendo(12); // Mensal
            i3.setAtivo(true);
            i3.setRisco(Risco.MEDIO);
            i3.setQuantidadeTotal(100000L); // 100 mil cotas
            i3.setQuantidadeDisponivel(45000L); // 45 mil disponíveis
            i3.setUltimaAtualizacaoPreco(now);
            i3.setCreatedAt(now);
            i3.setUpdatedAt(now);

            investimentoRepository.save(i1);
            investimentoRepository.save(i2);
            investimentoRepository.save(i3);

            System.out.println("Investimentos padrão criados com sucesso.");
            
            // Vincular investimentos ao usuário comum
            vincularInvestimentosAoUsuarioComum(i1, i2);
        }
    }
    
    private void vincularInvestimentosAoUsuarioComum(Investimento inv1, Investimento inv2) {
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail("usuario@teste.com");
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Adicionar investimentos ao usuário
                usuario.getInvestimentos().add(inv1);
                usuario.getInvestimentos().add(inv2);
                
                // Salvar o usuário com os investimentos vinculados
                usuarioDAO.save(usuario);
                
                System.out.println("✅ " + inv1.getNome() + " vinculado ao usuário comum");
                System.out.println("✅ " + inv2.getNome() + " vinculado ao usuário comum");
                System.out.println("🎯 Investimentos vinculados com sucesso ao usuário comum!");
            } else {
                System.out.println("⚠️ Usuário comum não encontrado para vincular investimentos");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao vincular investimentos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
