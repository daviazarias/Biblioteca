// Gerenciamento de Usuários
const UsuariosApp = {
    init() {
        this.carregarUsuarios();
    },

    carregarUsuarios() {
        fetch('/api/usuarios')
            .then(response => response.json())
            .then(usuarios => {
                const tbody = document.getElementById('tabelaUsuarios');
                tbody.innerHTML = '';

                usuarios.forEach(usuario => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${usuario.idUsuario}</td>
                        <td>${usuario.nome}</td>
                        <td>${usuario.email}</td>
                        <td><span class="badge bg-info">${usuario.tipoUsuario}</span></td>
                        <td>${BibliotecaUtils.formatarData(usuario.dataCadastro)}</td>
                        <td>
                            ${usuario.ativo ? '<span class="badge bg-success me-1">Ativo</span>' : '<span class="badge bg-danger me-1">Inativo</span>'}
                            ${usuario.bloqueadoPorMultas ? '<span class="badge bg-warning text-dark"><i class="bi bi-exclamation-triangle me-1"></i> Multas</span>' : ''}
                        </td>
                        <td>
                            <button class="btn btn-sm btn-warning me-1" onclick="UsuariosApp.editarUsuario(${usuario.idUsuario})" title="Editar" aria-label="Editar">
                                <i class="bi bi-pencil"></i>
                            </button>
                            ${usuario.tipoUsuario !== 'ADMIN' && usuario.ativo ?
                        `<button class="btn btn-sm btn-danger" onclick="UsuariosApp.inativarUsuario(${usuario.idUsuario})" title="Inativar" aria-label="Inativar">
                                <i class="bi bi-trash"></i>
                            </button>` : ''}
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            })
            .catch(error => BibliotecaUtils.mostrarErro('Erro ao carregar usuários'));
    },

    abrirModalNovo() {
        document.getElementById('modalUsuarioTitulo').textContent = 'Novo Usuário';
        document.getElementById('formNovoUsuario').reset();
        document.getElementById('usuarioId').value = '';
        document.getElementById('senha').required = true;
        document.getElementById('senhaHint').style.display = 'none';
        document.getElementById('campoStatus').style.display = 'none';
        document.getElementById('btnSalvarUsuario').textContent = 'Salvar';
        new bootstrap.Modal(document.getElementById('modalNovoUsuario')).show();
    },

    editarUsuario(id) {
        fetch(`/api/usuarios/${id}`)
            .then(response => response.json())
            .then(usuario => {
                document.getElementById('modalUsuarioTitulo').textContent = 'Editar Usuário';
                document.getElementById('usuarioId').value = usuario.idUsuario;
                document.getElementById('nome').value = usuario.nome;
                document.getElementById('email').value = usuario.email;
                document.getElementById('senha').value = '';
                document.getElementById('senha').required = false;
                document.getElementById('senhaHint').style.display = 'block';
                document.getElementById('tipoUsuario').value = usuario.tipoUsuario;
                document.getElementById('ativo').value = usuario.ativo.toString();
                document.getElementById('campoStatus').style.display = 'block';
                document.getElementById('btnSalvarUsuario').textContent = 'Atualizar';
                new bootstrap.Modal(document.getElementById('modalNovoUsuario')).show();
            })
            .catch(error => BibliotecaUtils.mostrarErro('Erro ao carregar usuário'));
    },

    salvarUsuario() {
        const form = document.getElementById('formNovoUsuario');
        const formData = new FormData(form);
        const id = document.getElementById('usuarioId').value;
        const isEdicao = id !== '';

        const usuario = {
            nome: formData.get('nome'),
            email: formData.get('email'),
            tipoUsuario: formData.get('tipoUsuario')
        };

        const senha = formData.get('senha');
        if (senha) usuario.senha = senha;
        if (isEdicao) usuario.ativo = formData.get('ativo') === 'true';

        const url = isEdicao ? `/api/usuarios/${id}` : '/api/usuarios';
        const _method = isEdicao ? 'PUT' : 'POST';

        fetch(url, {method: _method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(usuario)})
            .then(response => {
                if (response.ok) {
                    BibliotecaUtils.mostrarSucesso(isEdicao ? 'Usuário atualizado com sucesso!' : 'Usuário criado com sucesso!');
                    bootstrap.Modal.getInstance(document.getElementById('modalNovoUsuario')).hide();
                    form.reset();
                    this.carregarUsuarios();
                } else {
                    return response.text().then(text => {
                        throw new Error(text);
                    });
                }
            })
            .catch(error => BibliotecaUtils.mostrarErro('Erro ao salvar usuário: ' + error.message));
    },

    inativarUsuario(id) {
        if (!BibliotecaUtils.confirmar('Deseja realmente inativar este usuário?')) return;
        fetch(`/api/usuarios/${id}`, {method: 'DELETE'})
            .then(response => {
                if (response.ok) {
                    BibliotecaUtils.mostrarSucesso('Usuário inativado!');
                    this.carregarUsuarios();
                }
            })
            .catch(error => BibliotecaUtils.mostrarErro('Erro ao inativar usuário'));
    }
};

document.addEventListener('DOMContentLoaded', () => UsuariosApp.init());

