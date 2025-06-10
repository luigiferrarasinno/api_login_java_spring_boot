package com.example.demo.investimento.init;

import com.example.demo.investimento.model.Investimento;
import com.example.demo.investimento.repository.InvestimentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class InvestimentoInitializer implements CommandLineRunner {

    private final InvestimentoRepository investimentoRepository;

    public InvestimentoInitializer(InvestimentoRepository investimentoRepository) {
        this.investimentoRepository = investimentoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (investimentoRepository.count() == 0) { // evita duplicar na reinicialização
            Investimento i1 = new Investimento();
            i1.setNome("Tesouro Direto");
            i1.setCategoria("Renda Fixa");
            i1.setValor(new BigDecimal("1000.00"));
            i1.setDescricao("Investimento seguro em títulos do governo");
            // i1.setUsuario(null);  --> removido
            i1.setData(LocalDate.of(2025, 6, 10));
            i1.setRisco("Baixo");

            Investimento i2 = new Investimento();
            i2.setNome("Ações Vale");
            i2.setCategoria("Renda Variável");
            i2.setValor(new BigDecimal("5000.00"));
            i2.setDescricao("Investimento em ações da Vale");
            // i2.setUsuario(null);  --> removido
            i2.setData(LocalDate.of(2025, 5, 20));
            i2.setRisco("Alto");

            Investimento i3 = new Investimento();
            i3.setNome("Fundo Imobiliário");
            i3.setCategoria("Fundo");
            i3.setValor(new BigDecimal("3000.00"));
            i3.setDescricao("Investimento em fundos imobiliários");
            // i3.setUsuario(null);  --> removido
            i3.setData(LocalDate.of(2025, 4, 15));
            i3.setRisco("Médio");

            investimentoRepository.save(i1);
            investimentoRepository.save(i2);
            investimentoRepository.save(i3);

            System.out.println("Investimentos padrão criados com sucesso.");
        }
    }
}
