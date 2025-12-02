package com.biblioteca.multa.open.service;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import com.biblioteca.emprestimo.internal.repository.EmprestimoRepository;
import com.biblioteca.multa.internal.entity.Multa;
import com.biblioteca.multa.internal.repository.MultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MultaCalculoService {

    private static final BigDecimal VALOR_POR_DIA = new BigDecimal("1.00");
    private static final BigDecimal VALOR_ADICIONAL_10_DIAS = new BigDecimal("3.00");
    private static final int DIAS_PARA_ADICIONAL = 10;
    private final EmprestimoRepository emprestimoRepository;
    private final MultaRepository multaRepository;

    public BigDecimal calcularValorMulta(long diasAtraso) {
        if (diasAtraso <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal valorBase = VALOR_POR_DIA.multiply(new BigDecimal(diasAtraso));
        long periodos10Dias = diasAtraso / DIAS_PARA_ADICIONAL;
        BigDecimal valorAdicional = VALOR_ADICIONAL_10_DIAS.multiply(new BigDecimal(periodos10Dias));
        return valorBase.add(valorAdicional);
    }

    public void gerarOuAtualizarMulta(Emprestimo emprestimo) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataDevolucaoPrevista = emprestimo.getDataDevolucaoPrevista();
        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, hoje);
        if (diasAtraso <= 0) {
            return;
        }
        BigDecimal valorMulta = calcularValorMulta(diasAtraso);
        var optExistente = multaRepository.findByEmprestimoIdEmprestimo(emprestimo.getIdEmprestimo());
        if (optExistente.isPresent()) {
            Multa multaExistente = optExistente.get();
            if (multaExistente.getStatus() == Multa.StatusMulta.PENDENTE) {
                multaExistente.setValor(valorMulta);
                multaExistente.setObservacao(String.format("Multa atualizada: %d dias de atraso", diasAtraso));
                multaRepository.save(multaExistente);
                log.info("Multa atualizada para empréstimo {}: R$ {}", emprestimo.getIdEmprestimo(), valorMulta);
            }
        } else {
            Multa novaMulta = new Multa();
            novaMulta.setUsuario(emprestimo.getUsuario());
            novaMulta.setEmprestimo(emprestimo);
            novaMulta.setValor(valorMulta);
            novaMulta.setDataGeracao(hoje);
            novaMulta.setStatus(Multa.StatusMulta.PENDENTE);
            novaMulta.setObservacao(String.format("Atraso de %d dias. Livro: %s", diasAtraso, emprestimo.getLivro().getTitulo()));
            multaRepository.save(novaMulta);
            log.info("Nova multa gerada para empréstimo {}: R$ {}", emprestimo.getIdEmprestimo(), valorMulta);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void processarMultasAutomaticamente() {
        log.info("Iniciando processamento automático de multas...");
        LocalDate hoje = LocalDate.now();
        List<Emprestimo> emprestimosAtivos = emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.ATIVO);
        int multasGeradas = 0;
        int multasAtualizadas = 0;
        for (Emprestimo emprestimo : emprestimosAtivos) {
            if (emprestimo.getDataDevolucaoPrevista().isBefore(hoje)) {
                emprestimo.setStatus(Emprestimo.StatusEmprestimo.ATRASADO);
                emprestimoRepository.save(emprestimo);
                boolean jaTemMulta = multaRepository.findByEmprestimoIdEmprestimo(emprestimo.getIdEmprestimo()).isPresent();
                gerarOuAtualizarMulta(emprestimo);
                if (jaTemMulta) {
                    multasAtualizadas++;
                } else {
                    multasGeradas++;
                }
            }
        }
        log.info("Processamento concluído. Multas geradas: {}, atualizadas: {}", multasGeradas, multasAtualizadas);
    }

    public boolean usuarioTemMultasPendentes(Long idUsuario) {
        List<Multa> multasPendentes = multaRepository.findMultasPendentesByUsuario(idUsuario);
        return !multasPendentes.isEmpty();
    }

    public BigDecimal calcularTotalMultasPendentes(Long idUsuario) {
        Double soma = multaRepository.calcularTotalMultasPendentes(idUsuario);
        return soma == null ? BigDecimal.ZERO : BigDecimal.valueOf(soma);
    }
}
