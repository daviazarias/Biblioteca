// Utilities - Funções comuns do sistema
const BibliotecaUtils = {
    formatarData(dataStr) {
        if (!dataStr) return '-';
        return new Date(dataStr).toLocaleDateString('pt-BR');
    },

    formatarDataHora(dataStr) {
        if (!dataStr) return '-';
        return new Date(dataStr).toLocaleString('pt-BR');
    },

    formatarMoeda(valor) {
        return (valor ?? 0).toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'});
    },

    mostrarErro(mensagem) {
        alert('Erro: ' + mensagem);
    },

    mostrarSucesso(mensagem) {
        alert(mensagem);
    },

    confirmar(mensagem) {
        return confirm(mensagem);
    }
};

