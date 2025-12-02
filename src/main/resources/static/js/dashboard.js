// Dashboard Statistics
const DashboardApp = {
    init() {
        this.carregarEstatisticas();
    },

    carregarEstatisticas() {
        fetch('/api/dashboard/stats')
            .then(response => response.json())
            .then(data => {
                document.getElementById('totalUsuarios').textContent = data.totalUsuarios;
                document.getElementById('totalLivros').textContent = data.totalLivros;
                document.getElementById('totalEmprestimosAtivos').textContent = data.totalEmprestimosAtivos;
                document.getElementById('totalEmprestimosAtrasados').textContent = data.totalEmprestimosAtrasados;
            })
            .catch(error => console.error('Erro ao carregar estatísticas:', error));
    }
};

document.addEventListener('DOMContentLoaded', () => DashboardApp.init());

