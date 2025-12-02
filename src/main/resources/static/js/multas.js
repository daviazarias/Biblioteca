// Gerenciamento de Multas
const MultasApp = {
    init() {
        this.carregarMultas();
    },

    formatarMoeda(valor) {
        return BibliotecaUtils.formatarMoeda(valor);
    },

    processarMultas() {
        fetch('/api/multas/processar', {method: 'POST'})
            .then(resp => {
                if (!resp.ok) throw new Error('Falha ao processar multas');
                this.carregarMultas();
            })
            .catch(err => BibliotecaUtils.mostrarErro(err.message));
    },

    pagarMulta(id) {
        if (!BibliotecaUtils.confirmar('Confirmar pagamento desta multa?')) return;
        fetch(`/api/multas/${id}/pagar`, {method: 'POST'})
            .then(resp => {
                if (!resp.ok) throw new Error('Falha ao pagar multa');
                this.carregarMultas();
            })
            .catch(err => BibliotecaUtils.mostrarErro(err.message));
    },

    pagarTodas(idUsuario) {
        if (!BibliotecaUtils.confirmar('Confirmar pagamento de todas as multas pendentes deste usuário?')) return;
        fetch(`/api/multas/usuario/${idUsuario}/pagar-todas`, {method: 'POST'})
            .then(resp => {
                if (!resp.ok) throw new Error('Falha ao pagar multas');
                this.carregarMultas();
            })
            .catch(err => BibliotecaUtils.mostrarErro(err.message));
    },

    carregarMultas() {
        const filtro = document.getElementById('filtroStatus').value;
        const url = '/api/multas/por-usuario' + (filtro ? `?status=${filtro}` : '');
        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                const container = document.getElementById('listaUsuariosMultas');
                container.innerHTML = '';
                if (!data || data.length === 0) {
                    container.innerHTML = '<div class="alert alert-info">Nenhuma multa encontrada.</div>';
                    return;
                }
                data.forEach((u, idx) => this.renderUsuarioComMultas(u, idx, container));
            })
            .catch(err => BibliotecaUtils.mostrarErro('Erro ao carregar multas: ' + err.message));
    },

    renderUsuarioComMultas(u, idx, container) {
        const headerId = `heading${idx}`;
        const collapseId = `collapse${idx}`;
        const item = document.createElement('div');
        item.className = 'accordion-item';
        item.innerHTML = `
            <h2 class="accordion-header" id="${headerId}">
                <button class="accordion-button ${idx !== 0 ? 'collapsed' : ''}" type="button" data-bs-toggle="collapse" data-bs-target="#${collapseId}" aria-expanded="${idx === 0}" aria-controls="${collapseId}">
                    <div class="d-flex w-100 justify-content-between align-items-center pe-3">
                        <div>
                            <strong>${u.nomeUsuario}</strong> <small class="text-muted">(${u.email})</small>
                        </div>
                        <div class="d-flex align-items-center gap-3">
                            <span class="badge bg-danger">Pendentes: ${this.formatarMoeda(u.valorTotalPendente)}</span>
                            <span class="badge bg-success">Pagas: ${this.formatarMoeda(u.valorTotalPago)}</span>
                            <button class="btn btn-sm btn-success" onclick="event.stopPropagation(); MultasApp.pagarTodas(${u.idUsuario})" title="Pagar todas as multas" aria-label="Pagar todas as multas">
                                <i class="bi bi-cash-coin"></i>
                            </button>
                        </div>
                    </div>
                </button>
            </h2>
            <div id="${collapseId}" class="accordion-collapse collapse ${idx === 0 ? 'show' : ''}" aria-labelledby="${headerId}">
                <div class="accordion-body">
                    <div class="table-responsive">
                        <table class="table table-sm">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Empréstimo</th>
                                <th>Valor</th>
                                <th>Status</th>
                                <th>Gerada em</th>
                                <th>Pago em</th>
                                <th>Ações</th>
                            </tr>
                            </thead>
                            <tbody>
                            ${u.multas.map(m => `
                                <tr>
                                    <td>${m.idMulta}</td>
                                    <td>${m.idEmprestimo ?? '-'}</td>
                                    <td>${this.formatarMoeda(m.valor)}</td>
                                    <td><span class="badge ${m.status === 'PENDENTE' ? 'bg-warning' : (m.status === 'PAGA' ? 'bg-success' : 'bg-secondary')}">${m.status}</span></td>
                                    <td>${m.dataGeracao ? new Date(m.dataGeracao).toLocaleDateString('pt-BR') : '-'}</td>
                                    <td>${m.dataPagamento ? new Date(m.dataPagamento).toLocaleDateString('pt-BR') : '-'}</td>
                                    <td>
                                        ${m.status === 'PENDENTE' ? `
                                            <button class="btn btn-sm btn-success" onclick="MultasApp.pagarMulta(${m.idMulta})" title="Pagar multa" aria-label="Pagar multa">
                                                <i class="bi bi-cash"></i>
                                            </button>` : ''}
                                    </td>
                                </tr>
                            `).join('')}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(item);
    }
};

// Inicializar ao carregar a página
document.addEventListener('DOMContentLoaded', () => MultasApp.init());

