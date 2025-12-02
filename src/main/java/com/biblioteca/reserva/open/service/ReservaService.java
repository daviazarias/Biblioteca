package com.biblioteca.reserva.open.service;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import com.biblioteca.emprestimo.internal.repository.EmprestimoRepository;
import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.livro.internal.repository.LivroRepository;
import com.biblioteca.reserva.internal.entity.Reserva;
import com.biblioteca.reserva.internal.mapper.ReservaMapper;
import com.biblioteca.reserva.internal.repository.ReservaRepository;
import com.biblioteca.reserva.open.dto.ReservaCreateDTO;
import com.biblioteca.reserva.open.dto.ReservaDTO;
import com.biblioteca.reserva.open.dto.ReservaUpdateDTO;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    public static final String RESERVA_NAO_ENCONTRADA = "Reserva não encontrada";
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final ReservaMapper reservaMapper;

    public ReservaDTO criarReserva(ReservaCreateDTO dto) {
        var usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo não pode fazer reservas");
        }

        var livro = livroRepository.findById(dto.idLivro())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        var reservasAtivas = reservaRepository.findReservasAtivasByLivro(livro.getIdLivro());
        boolean usuarioJaReservou = reservasAtivas.stream()
                .anyMatch(r -> r.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()));

        if (usuarioJaReservou) {
            throw new IllegalStateException("Usuário já possui reserva ativa para este livro");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setLivro(livro);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setDataValidade(LocalDate.now().plusDays(3));
        reserva.setStatus(Reserva.StatusReserva.ATIVA);

        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public ReservaDTO atualizarReserva(ReservaUpdateDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.idReserva())
                .orElseThrow(() -> new IllegalArgumentException(RESERVA_NAO_ENCONTRADA));

        reserva.setStatus(dto.status());

        if (dto.status() == Reserva.StatusReserva.CONCLUIDA && reserva.getDataRetirada() == null) {
            reserva.setDataRetirada(LocalDateTime.now());
        }

        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> listarTodas() {
        List<ReservaDTO> list = new ArrayList<>();
        for (Reserva reserva : reservaRepository.findAll()) {
            ReservaDTO dto = reservaMapper.toDTO(reserva);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> listarAtivas() {
        List<ReservaDTO> list = new ArrayList<>();
        for (Reserva reserva : reservaRepository.findByStatus(Reserva.StatusReserva.ATIVA)) {
            ReservaDTO dto = reservaMapper.toDTO(reserva);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> listarPorUsuario(Long idUsuario) {
        List<ReservaDTO> list = new ArrayList<>();
        for (Reserva reserva : reservaRepository.findByUsuarioIdUsuario(idUsuario)) {
            ReservaDTO dto = reservaMapper.toDTO(reserva);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public ReservaDTO buscarPorId(Long id) {
        return reservaMapper.toDTO(reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(RESERVA_NAO_ENCONTRADA)));
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(RESERVA_NAO_ENCONTRADA));

        if (reserva.getStatus() != Reserva.StatusReserva.ATIVA) {
            throw new IllegalStateException("Apenas reservas ativas podem ser canceladas");
        }

        reserva.setStatus(Reserva.StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public ReservaDTO concluirReservaECriarEmprestimo(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(RESERVA_NAO_ENCONTRADA));

        if (reserva.getStatus() != Reserva.StatusReserva.ATIVA) {
            throw new IllegalStateException("Apenas reservas ativas podem ser concluídas");
        }

        // Verificar se o livro está disponível
        Livro livro = reserva.getLivro();
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalStateException("Livro não disponível para empréstimo");
        }

        // Concluir a reserva
        reserva.setStatus(Reserva.StatusReserva.CONCLUIDA);
        reserva.setDataRetirada(LocalDateTime.now());
        reservaRepository.save(reserva);

        // Criar empréstimo automaticamente
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(reserva.getUsuario());
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14)); // 14 dias de prazo
        emprestimo.setStatus(Emprestimo.StatusEmprestimo.ATIVO);
        emprestimoRepository.save(emprestimo);

        // Decrementar quantidade disponível do livro
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        return reservaMapper.toDTO(reserva);
    }

    public void expirarReservas() {
        var expiradas = reservaRepository.findReservasExpiradas(LocalDate.now());
        expiradas.forEach(r -> {
            r.setStatus(Reserva.StatusReserva.EXPIRADA);
            reservaRepository.save(r);
        });
    }
}
