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

            Investimento i1 = new Investimento();
            i1.setNome("Tesouro Direto");
            i1.setCategoria(Categoria.RENDA_FIXA);
            i1.setValor(new BigDecimal("1000.00"));
            i1.setDescricao("Investimento seguro em títulos do governo");
            i1.setData(LocalDate.of(2025, 6, 10));
            i1.setLiquidez("Diária");
            i1.setTaxaRetorno(new BigDecimal("5.5"));
            i1.setAtivo(true);
            i1.setRisco(Risco.BAIXO);
            i1.setCreatedAt(now);
            i1.setUpdatedAt(now);

            Investimento i2 = new Investimento();
            i2.setNome("Ações Vale");
            i2.setCategoria(Categoria.RENDA_VARIAVEL);
            i2.setValor(new BigDecimal("5000.00"));
            i2.setDescricao("Investimento em ações da Vale");
            i2.setData(LocalDate.of(2025, 5, 20));
            i2.setLiquidez("Baixa");
            i2.setTaxaRetorno(new BigDecimal("12.0"));
            i2.setAtivo(true);
            i2.setRisco(Risco.ALTO);
            i2.setCreatedAt(now);
            i2.setUpdatedAt(now);

            Investimento i3 = new Investimento();
            i3.setNome("Fundo Imobiliário");
            i3.setCategoria(Categoria.FUNDO);
            i3.setValor(new BigDecimal("3000.00"));
            i3.setDescricao("Investimento em fundos imobiliários");
            i3.setData(LocalDate.of(2025, 4, 15));
            i3.setLiquidez("Média");
            i3.setTaxaRetorno(new BigDecimal("8.0"));
            i3.setAtivo(true);
            i3.setRisco(Risco.MEDIO);
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
