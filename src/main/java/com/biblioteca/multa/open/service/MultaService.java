package com.biblioteca.multa.open.service;

import com.biblioteca.emprestimo.internal.repository.EmprestimoRepository;
import com.biblioteca.multa.internal.entity.Multa;
import com.biblioteca.multa.internal.mapper.MultaMapper;
import com.biblioteca.multa.internal.repository.MultaRepository;
import com.biblioteca.multa.open.dto.MultaCreateDTO;
import com.biblioteca.multa.open.dto.MultaDTO;
import com.biblioteca.multa.open.dto.MultaUpdateDTO;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MultaService {

    public static final String MULTA_NAO_ENCONTRADA = "Multa não encontrada";
    private final MultaRepository multaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final MultaMapper multaMapper;

    public MultaDTO criarMulta(MultaCreateDTO dto) {
        var usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Multa multa = new Multa();
        multa.setUsuario(usuario);
        multa.setValor(dto.valor());
        multa.setObservacao(dto.observacao());
        multa.setDataGeracao(LocalDate.now());
        multa.setStatus(Multa.StatusMulta.PENDENTE);

        if (dto.idEmprestimo() != null) {
            var emprestimo = emprestimoRepository.findById(dto.idEmprestimo())
                    .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));
            multa.setEmprestimo(emprestimo);
        }

        return multaMapper.toDTO(multaRepository.save(multa));
    }

    public MultaDTO atualizarMulta(MultaUpdateDTO dto) {
        Multa multa = multaRepository.findById(dto.idMulta())
                .orElseThrow(() -> new IllegalArgumentException(MULTA_NAO_ENCONTRADA));

        if (dto.valor() != null) multa.setValor(dto.valor());
        if (dto.observacao() != null) multa.setObservacao(dto.observacao());

        if (dto.status() != null) {
            multa.setStatus(dto.status());
            if (dto.status() == Multa.StatusMulta.PAGA && multa.getDataPagamento() == null) {
                multa.setDataPagamento(LocalDate.now());
            }
        }

        return multaMapper.toDTO(multaRepository.save(multa));
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> listarTodas() {
        return getMultasPrivate();
    }

    private List<MultaDTO> getMultasPrivate() {
        List<MultaDTO> list = new ArrayList<>();
        for (Multa multa : multaRepository.findAll()) {
            MultaDTO dto = multaMapper.toDTO(multa);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> listarPendentes() {
        List<MultaDTO> list = new ArrayList<>();
        for (Multa multa : multaRepository.findByStatus(Multa.StatusMulta.PENDENTE)) {
            MultaDTO dto = multaMapper.toDTO(multa);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> listarPorStatus(String statusStr) {
        try {
            Multa.StatusMulta status = Multa.StatusMulta.valueOf(statusStr.toUpperCase());
            List<MultaDTO> list = new ArrayList<>();
            for (Multa multa : multaRepository.findByStatus(status)) {
                MultaDTO dto = multaMapper.toDTO(multa);
                list.add(dto);
            }
            return list;
        } catch (IllegalArgumentException e) {
            return getMultasPrivate();
        }
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> listarPorUsuario(Long idUsuario) {
        List<MultaDTO> list = new ArrayList<>();
        for (Multa multa : multaRepository.findByUsuarioIdUsuario(idUsuario)) {
            MultaDTO dto = multaMapper.toDTO(multa);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public MultaDTO buscarPorId(Long id) {
        return multaMapper.toDTO(multaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MULTA_NAO_ENCONTRADA)));
    }

    public void deletarMulta(Long id) {
        if (!multaRepository.existsById(id)) {
            throw new IllegalArgumentException(MULTA_NAO_ENCONTRADA);
        }
        multaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<com.biblioteca.multa.open.dto.MultasPorUsuarioDTO> listarMultasPorUsuario() {
        List<com.biblioteca.multa.open.dto.MultasPorUsuarioDTO> resultado = new ArrayList<>();

        // Buscar todas as multas e agrupar por usuário
        java.util.Map<Long, List<Multa>> multasPorUsuario = new java.util.HashMap<>();

        for (Multa multa : multaRepository.findAll()) {
            Long idUsuario = multa.getUsuario().getIdUsuario();
            multasPorUsuario.computeIfAbsent(idUsuario, k -> new ArrayList<>()).add(multa);
        }

        // Criar DTO para cada usuário
        for (java.util.Map.Entry<Long, List<Multa>> entry : multasPorUsuario.entrySet()) {
            List<Multa> multas = entry.getValue();
            if (multas.isEmpty()) continue;

            var usuario = multas.get(0).getUsuario();

            java.math.BigDecimal totalPendente = multas.stream()
                    .filter(m -> m.getStatus() == Multa.StatusMulta.PENDENTE)
                    .map(Multa::getValor)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            java.math.BigDecimal totalPago = multas.stream()
                    .filter(m -> m.getStatus() == Multa.StatusMulta.PAGA)
                    .map(Multa::getValor)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            List<MultaDTO> multasDTO = multas.stream()
                    .map(multaMapper::toDTO)
                    .toList();

            resultado.add(new com.biblioteca.multa.open.dto.MultasPorUsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    (long) multas.size(),
                    totalPendente,
                    totalPago,
                    multasDTO
            ));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public List<com.biblioteca.multa.open.dto.MultasPorUsuarioDTO> listarMultasPorUsuario(String status) {
        List<com.biblioteca.multa.open.dto.MultasPorUsuarioDTO> resultado = new ArrayList<>();

        Multa.StatusMulta filtroStatus = null;
        try {
            if (status != null && !status.isBlank()) {
                filtroStatus = Multa.StatusMulta.valueOf(status);
            }
        } catch (IllegalArgumentException ignored) {
        }

        java.util.Map<Long, List<com.biblioteca.multa.internal.entity.Multa>> multasPorUsuario = new java.util.HashMap<>();
        for (com.biblioteca.multa.internal.entity.Multa m : multaRepository.findAll()) {
            if (filtroStatus != null && m.getStatus() != filtroStatus) continue;
            multasPorUsuario.computeIfAbsent(m.getUsuario().getIdUsuario(), k -> new ArrayList<>()).add(m);
        }

        for (var entry : multasPorUsuario.entrySet()) {
            var multas = entry.getValue();
            if (multas.isEmpty()) continue;
            var usuario = multas.get(0).getUsuario();

            java.math.BigDecimal totalPendente = multas.stream()
                    .filter(mm -> mm.getStatus() == Multa.StatusMulta.PENDENTE)
                    .map(com.biblioteca.multa.internal.entity.Multa::getValor)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            java.math.BigDecimal totalPago = multas.stream()
                    .filter(mm -> mm.getStatus() == Multa.StatusMulta.PAGA)
                    .map(com.biblioteca.multa.internal.entity.Multa::getValor)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            List<MultaDTO> multasDTO = multas.stream().map(multaMapper::toDTO).toList();

            resultado.add(new com.biblioteca.multa.open.dto.MultasPorUsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    (long) multas.size(),
                    totalPendente,
                    totalPago,
                    multasDTO
            ));
        }
        return resultado;
    }

    @Transactional
    public void pagarMulta(Long idMulta) {
        Multa multa = multaRepository.findById(idMulta)
                .orElseThrow(() -> new IllegalArgumentException(MULTA_NAO_ENCONTRADA));
        multa.setStatus(Multa.StatusMulta.PAGA);
        multa.setDataPagamento(java.time.LocalDate.now());
        multaRepository.save(multa);
    }

    @Transactional
    public void pagarTodasDoUsuario(Long idUsuario) {
        List<Multa> pendentes = multaRepository.findMultasPendentesByUsuario(idUsuario);
        for (Multa m : pendentes) {
            m.setStatus(Multa.StatusMulta.PAGA);
            m.setDataPagamento(java.time.LocalDate.now());
            multaRepository.save(m);
        }
    }
}
