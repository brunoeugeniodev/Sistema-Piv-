// --- Variáveis Globais ---
const BASE_URL_GESTOR = 'http://localhost:8080/api'; // *** ALERTA: Substitua pela URL real do seu backend! ***

let editingHistoricoId = null;
let editingPivoId = null;
let currentUser = null; // Para simular o usuário logado

// --- Funções Auxiliares (Essenciais para o funcionamento) ---

/**
 * Função genérica para fazer requisições à API.
 * @param {string} url - A URL do endpoint da API.
 * @param {string} method - O método HTTP (GET, POST, PUT, DELETE). Padrão: 'GET'.
 * @param {object} data - Os dados a serem enviados no corpo da requisição (para POST/PUT).
 * @returns {Promise<object|null>} - Os dados da resposta JSON ou null em caso de erro.
 */
async function fetchData(url, method = 'GET', data = null) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    // Adiciona o token de autorização se o usuário estiver logado
    const token = localStorage.getItem('userToken');
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            // Tenta ler o erro do corpo da resposta, se disponível
            const errorData = await response.json().catch(() => ({ message: 'Erro desconhecido' }));
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorData.message || response.statusText}`);
        }
        if (response.status === 204) { // No Content
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(`Erro na requisição ${method} ${url}:`, error);
        showMessage(`Erro na operação: ${error.message}`, 'error');
        return null;
    }
}

/**
 * Exibe uma mensagem para o usuário.
 * @param {string} message - A mensagem a ser exibida.
 * @param {'info'|'error'|'confirm'} type - O tipo da mensagem (info, error, confirm).
 * @param {function} callback - Função a ser chamada após a interação do usuário (para 'confirm').
 */
function showMessage(message, type = 'info', callback = null) {
    const messageContainer = document.getElementById('messageContainer') || createMessageContainer();
    messageContainer.innerHTML = ''; // Limpa mensagens anteriores

    const alertDiv = document.createElement('div');
    alertDiv.classList.add('alert', 'alert-dismissible', 'fade', 'show', 'alert-custom');
    alertDiv.setAttribute('role', 'alert');

    switch (type) {
        case 'info':
            alertDiv.classList.add('alert-info-custom');
            break;
        case 'error':
            alertDiv.classList.add('alert-danger-custom');
            break;
        case 'confirm':
            alertDiv.classList.add('alert-warning-custom');
            break;
        default:
            alertDiv.classList.add('alert-info-custom');
            break;
    }

    alertDiv.innerHTML = `
        <span>${message}</span>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

    messageContainer.appendChild(alertDiv);

    if (type === 'confirm' && callback) {
        const confirmButtonsDiv = document.createElement('div');
        confirmButtonsDiv.classList.add('mt-2', 'd-flex', 'justify-content-end');

        const btnYes = document.createElement('button');
        btnYes.classList.add('btn', 'btn-sm', 'btn-success-custom', 'me-2');
        btnYes.textContent = 'Sim';
        btnYes.onclick = () => {
            callback(true);
            alertDiv.remove(); // Remove a mensagem após a ação
        };

        const btnNo = document.createElement('button');
        btnNo.classList.add('btn', 'btn-sm', 'btn-secondary-custom');
        btnNo.textContent = 'Não';
        btnNo.onclick = () => {
            callback(false);
            alertDiv.remove(); // Remove a mensagem após a ação
        };

        confirmButtonsDiv.appendChild(btnYes);
        confirmButtonsDiv.appendChild(btnNo);
        alertDiv.appendChild(confirmButtonsDiv);
    } else {
        // Auto-fechar alertas de info/error após um tempo
        setTimeout(() => {
            alertDiv.classList.remove('show');
            alertDiv.classList.add('hide'); // Adiciona 'hide' para animação de fade-out
            alertDiv.addEventListener('transitionend', () => alertDiv.remove());
        }, 5000);
    }
}

// Cria um container para as mensagens se ele não existir
function createMessageContainer() {
    const container = document.createElement('div');
    container.id = 'messageContainer';
    document.body.prepend(container); // Adiciona no início do body por padrão
    return container;
}


/**
 * Popula um elemento <select> com dados de uma API.
 * @param {string} selectId - O ID do elemento select.
 * @param {string} url - A URL da API para buscar os dados.
 * @param {string} valueKey - A chave do objeto a ser usada como valor da opção (value).
 * @param {string} textKey - A chave do objeto a ser usada como texto visível da opção.
 * @param {string|number} [selectedValue] - O valor da opção que deve ser pré-selecionada.
 */
async function populateSelect(selectId, url, valueKey, textKey, selectedValue = null) {
    const selectElement = document.getElementById(selectId);
    if (!selectElement) {
        console.error(`Elemento select com ID '${selectId}' não encontrado.`);
        return;
    }

    selectElement.innerHTML = '<option value="">Carregando...</option>';

    try {
        const items = await fetchData(url);
        selectElement.innerHTML = '<option value="">Selecione...</option>'; // Limpa e adiciona opção padrão

        if (items && items.length > 0) {
            items.forEach(item => {
                const option = document.createElement('option');
                option.value = item[valueKey];
                option.textContent = item[textKey];
                if (selectedValue !== null && String(item[valueKey]) === String(selectedValue)) {
                    option.selected = true;
                }
                selectElement.appendChild(option);
            });
        } else {
            selectElement.innerHTML = '<option value="">Nenhuma opção disponível</option>';
        }
    } catch (error) {
        console.error(`Erro ao popular o select '${selectId}':`, error);
        selectElement.innerHTML = '<option value="">Erro ao carregar opções</option>';
    }
}


// --- Funções de Navegação e Carregamento de Conteúdo ---
const mainContentDiv = document.getElementById('mainContent');

async function loadContent(pageName, callback = null, requiresAuth = false) {
    if (requiresAuth && !localStorage.getItem('userToken')) {
        showMessage('Você precisa estar logado para acessar esta página.', 'error');
        loadLoginSection(); // Redireciona para login
        return;
    }

    try {
        const response = await fetch(`paginas/${pageName}.html`);
        if (!response.ok) {
            if (response.status === 404) {
                 mainContentDiv.innerHTML = `<div class="container mt-5 text-center"><h1 style="color: var(--primary-dark);">Página Não Encontrada</h1><p>A página ${pageName} não existe ou está indisponível.</p></div>`;
                 return;
            }
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.text();
        mainContentDiv.innerHTML = data;

        if (callback && typeof callback === 'function') {
            await callback();
        }
    } catch (error) {
        console.error(`Erro ao carregar a página ${pageName}:`, error);
        showMessage(`Não foi possível carregar a seção ${pageName}.`, 'error');
    }
}

// --- Funções de Autenticação ---

async function loadLoginSection() {
    await loadContent('login', () => {
        document.getElementById('loginForm').addEventListener('submit', (event) => {
            event.preventDefault();
            handleLogin();
        });
        document.getElementById('linkCadastreAqui').addEventListener('click', (event) => {
            event.preventDefault();
            loadRegisterSection();
        });
    });
}

async function handleLogin() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginSenha').value;

    if (!email || !password) {
        showMessage('Por favor, preencha E-mail e Senha.', 'error');
        return;
    }

    try {
        const userData = await fetchData(`${BASE_URL_GESTOR}/auth/login`, 'POST', { email, password });
        if (userData && userData.token) {
            localStorage.setItem('userToken', userData.token);
            // Salvar mais dados do usuário se sua API retornar (e.g., id, nome, role)
            currentUser = userData.user || { id: 'someId', name: 'User' }; // Placeholder se API não retornar user object
            showMessage('Login realizado com sucesso!', 'info');
            loadHomeSection(); // Redireciona para a home
            updateNavigationForLoggedInUser(); // Atualiza os botões de navegação
        } else {
            showMessage('Credenciais inválidas. Tente novamente.', 'error');
        }
    } catch (error) {
        console.error('Erro no login:', error);
        showMessage('Erro ao tentar fazer login. Verifique suas credenciais e tente novamente.', 'error');
    }
}

async function loadRegisterSection() {
    await loadContent('cadastro', () => {
        document.getElementById('cadastroForm').addEventListener('submit', (event) => {
            event.preventDefault();
            handleRegister();
        });
    });
}

async function handleRegister() {
    const nome = document.getElementById('cadastroNome').value;
    const email = document.getElementById('cadastroEmail').value;
    const senha = document.getElementById('cadastroSenha').value;
    const telefone = document.getElementById('cadastroTelefone').value;

    if (!nome || !email || !senha) {
        showMessage('Por favor, preencha Nome, E-mail e Senha para o cadastro!', 'error');
        return;
    }

    const userData = {
        nome_usuario: nome,
        email: email,
        senha: senha,
        telefone: telefone
    };

    try {
        const response = await fetchData(`${BASE_URL_GESTOR}/auth/register`, 'POST', userData);
        if (response) {
            showMessage('Cadastro realizado com sucesso! Faça login para continuar.', 'info', () => {
                loadLoginSection(); // Redireciona para a tela de login
            });
        } else {
             showMessage('Erro ao cadastrar usuário. Tente novamente.', 'error');
        }
    } catch (error) {
        console.error('Erro no cadastro:', error);
        showMessage('Erro ao tentar cadastrar. Verifique os dados e tente novamente.', 'error');
    }
}

function handleLogout() {
    localStorage.removeItem('userToken');
    currentUser = null;
    showMessage('Você foi desconectado.', 'info', () => {
        loadLoginSection(); // Redireciona para a tela de login
        updateNavigationForLoggedInUser();
    });
}

// Função para atualizar a UI de navegação após login/logout
function updateNavigationForLoggedInUser() {
    const navBarNav = document.getElementById('navbarNav'); // O container dos links do navbar
    const authButtonsContainer = document.getElementById('authButtons'); // O container dos botões Entrar/Cadastrar

    if (!navBarNav || !authButtonsContainer) return;

    if (localStorage.getItem('userToken')) {
        // Usuário Logado: Mostrar menu completo e botão Sair
        authButtonsContainer.innerHTML = `
            <li class="nav-item ms-2">
                <button class="btn btn-primary-custom" id="btnLogout">Sair</button>
            </li>
        `;
        document.getElementById('btnLogout')?.addEventListener('click', handleLogout);

        navBarNav.innerHTML = `
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="#" id="btnPivos">Pivôs</a></li>
                <li class="nav-item"><a class="nav-link" href="#" id="btnHistorico">Históricos</a></li>
                <li class="nav-item"><a class="nav-link" href="#" id="btnGuiasNav">Guias</a></li>
                <li class="nav-item"><a class="nav-link" href="#" id="linkSobreNos">Sobre nós</a></li>
            </ul>
        ` + authButtonsContainer.outerHTML; // Reinsere o container de botões

    } else {
        // Usuário NÃO Logado: Mostrar menu simplificado e botões de Autenticação
        authButtonsContainer.innerHTML = `
            <li class="nav-item">
                <button class="btn-outline-custom me-2" id="btnCadastro">Cadastre-se</button>
            </li>
            <li class="nav-item">
                <button class="btn-custom" id="btnEntrar">Entrar</button>
            </li>
        `;
        document.getElementById('btnCadastro')?.addEventListener('click', loadRegisterSection);
        document.getElementById('btnEntrar')?.addEventListener('click', loadLoginSection);

        navBarNav.innerHTML = `
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="#" id="btnPivos">Pivôs</a></li>
            </ul>
        ` + authButtonsContainer.outerHTML; // Reinsere o container de botões
    }

    // Re-atribui event listeners para os links principais após a atualização do HTML
    document.getElementById('btnPivos')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadPivoSection();
    });
    document.getElementById('btnHistorico')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadHistoricoSection();
    });
    document.getElementById('btnGuiasNav')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadGuiaSection(); // Agora carrega a página informativa
    });
    document.getElementById('linkSobreNos')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadAboutUsSection();
    });

    // Event listener para o link do logo "Moiô"
    document.getElementById('btnHome')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadHomeSection();
    });

     // Event listeners para os links do rodapé (sempre visíveis, mas alguns podem exigir login para conteúdo)
    document.getElementById('footerLinkSobreNos')?.addEventListener('click', (e) => { // ID ajustado para evitar conflito com navbar
        e.preventDefault();
        loadAboutUsSection();
    });
    document.getElementById('footerLinkGuia')?.addEventListener('click', (e) => { // ID ajustado
        e.preventDefault();
        loadGuiaSection();
    });
}


// --- Funções da Seção Principal/Home ---
async function loadHomeSection() {
    await loadContent('home');
}

// --- Funções da Seção Sobre Nós ---
async function loadAboutUsSection() {
    await loadContent('sobre-nos');
}

// --- Funções da Seção de Guias (APENAS INFORMATIVA AGORA) ---
async function loadGuiaSection() {
    await loadContent('guia'); // Carrega o HTML estático de informações
    // Não há mais CRUD para guias aqui
}


// --- Funções Específicas para a Seção de Históricos ---
async function loadHistoricoSection() {
    await loadContent('historico', async () => {
        // Popula o combobox de pivôs para o Histórico
        await populateSelect('pivoIdHistorico', `${BASE_URL_GESTOR}/pivos`, 'id', 'nome_pivo');

        document.getElementById('historicoForm').addEventListener('submit', (event) => {
            event.preventDefault();
            saveHistorico();
        });
        document.getElementById('btnListarHistoricos').addEventListener('click', displayHistoricoIrrigacao);
        document.getElementById('btnCancelarHistorico').addEventListener('click', () => {
            editingHistoricoId = null;
            document.getElementById('historicoForm').reset();
            document.getElementById('btnSalvarHistorico').textContent = 'Salvar Histórico';
        });
        displayHistoricoIrrigacao(); // Carrega a lista de históricos ao iniciar
    }, true); // Requer autenticação
}

async function saveHistorico() {
    const nomeHistorico = document.getElementById('nomeHistorico').value;
    const statusOperacao = document.getElementById('statusOperacaoRegistrado').value;
    const observacoes = document.getElementById('observacoesHistorico').value;
    const pivoId = document.getElementById('pivoIdHistorico').value;

    if (!nomeHistorico || !statusOperacao || !pivoId) {
        showMessage('Por favor, preencha nome, status e selecione o pivô para o histórico!', 'error');
        return;
    }

    const historicoData = {
        nome_historico: nomeHistorico,
        status_operacao_registrado: statusOperacao,
        observacoes: observacoes,
        pivos: [pivoId] // Sua API espera um array de IDs de pivôs
    };

    let response;
    try {
        if (editingHistoricoId) {
            response = await fetchData(`${BASE_URL_GESTOR}/historicos/${editingHistoricoId}`, 'PUT', historicoData);
            if (response) {
                showMessage('Histórico atualizado com sucesso!', 'info', () => {
                    editingHistoricoId = null;
                    document.getElementById('btnSalvarHistorico').textContent = 'Salvar Histórico';
                    document.getElementById('historicoForm').reset();
                    displayHistoricoIrrigacao();
                });
            }
        } else {
            response = await fetchData(`${BASE_URL_GESTOR}/historicos`, 'POST', historicoData);
            if (response) {
                showMessage('Histórico cadastrado com sucesso!', 'info', () => {
                    document.getElementById('historicoForm').reset();
                    displayHistoricoIrrigacao();
                });
            }
        }
    } catch (error) {
        console.error('Erro ao salvar histórico:', error);
        showMessage('Erro ao salvar histórico. Verifique os dados e tente novamente.', 'error');
    }
}

async function displayHistoricoIrrigacao() {
    const tabelaHistoricoBody = document.querySelector('#tabelaHistorico tbody');
    if (!tabelaHistoricoBody) return;

    tabelaHistoricoBody.innerHTML = '<tr><td colspan="6">Carregando históricos...</td></tr>';

    try {
        const historicos = await fetchData(`${BASE_URL_GESTOR}/historicos`);
        tabelaHistoricoBody.innerHTML = '';

        if (!historicos || historicos.length === 0) {
            tabelaHistoricoBody.innerHTML = '<tr><td colspan="6">Nenhum histórico de irrigação cadastrado ainda.</td></tr>';
            return;
        }

        for (const historico of historicos) {
            const pivoNomes = [];
            if (historico.pivos && historico.pivos.length > 0) {
                // Para simplificar, pegaremos apenas o primeiro pivô se for um array
                const pivoId = historico.pivos[0];
                try {
                    const pivo = await fetchData(`${BASE_URL_GESTOR}/pivos/${pivoId}`);
                    if (pivo) {
                        pivoNomes.push(pivo.nome_pivo);
                    } else {
                        pivoNomes.push(`${pivoId.substring(0, 8)}... (Não encontrado)`);
                    }
                } catch (error) {
                    console.warn(`Não foi possível carregar o pivô para o ID ${pivoId}:`, error);
                    pivoNomes.push(`${pivoId.substring(0, 8)}... (Erro)`);
                }
            }

            const row = tabelaHistoricoBody.insertRow();
            row.innerHTML = `
                <td>${historico.id ? historico.id.substring(0, 8) + '...' : 'N/A'}</td>
                <td>${historico.nome_historico || 'N/A'}</td>
                <td>${historico.status_operacao_registrado || 'N/A'}</td>
                <td>${historico.observacoes || 'N/A'}</td>
                <td>${pivoNomes.length > 0 ? pivoNomes.join(', ') : 'N/A'}</td>
                <td class="action-buttons">
                    <button class="btn btn-warning-custom btn-sm edit-btn" data-id="${historico.id}">Editar</button>
                    <button class="btn btn-danger-custom btn-sm delete-btn" data-id="${historico.id}">Excluir</button>
                </td>
            `;
        }

        document.querySelectorAll('#tabelaHistorico .edit-btn').forEach(button => {
            button.addEventListener('click', (event) => editHistorico(event.target.dataset.id));
        });
        document.querySelectorAll('#tabelaHistorico .delete-btn').forEach(button => {
            button.addEventListener('click', (event) => deleteHistorico(event.target.dataset.id));
        });
    } catch (error) {
        console.error('Erro ao exibir históricos:', error);
        tabelaHistoricoBody.innerHTML = '<tr><td colspan="6" style="color:red;">Erro ao carregar históricos.</td></tr>';
    }
}

async function editHistorico(id) {
    try {
        const historicoToEdit = await fetchData(`${BASE_URL_GESTOR}/historicos/${id}`);
        if (historicoToEdit) {
            document.getElementById('nomeHistorico').value = historicoToEdit.nome_historico || '';
            document.getElementById('statusOperacaoRegistrado').value = historicoToEdit.status_operacao_registrado || '';
            document.getElementById('observacoesHistorico').value = historicoToEdit.observacoes || '';

            const pivoIdAssociado = historicoToEdit.pivos && historicoToEdit.pivos.length > 0 ? historicoToEdit.pivos[0] : '';
            await populateSelect('pivoIdHistorico', `${BASE_URL_GESTOR}/pivos`, 'id', 'nome_pivo', pivoIdAssociado);

            editingHistoricoId = id;
            document.getElementById('btnSalvarHistorico').textContent = 'Atualizar Histórico';
            document.getElementById('historicoFormContainer').scrollIntoView({ behavior: 'smooth' });
        } else {
            showMessage('Histórico não encontrado para edição.', 'error');
        }
    } catch (error) {
        console.error('Erro ao carregar histórico para edição:', error);
    }
}

function deleteHistorico(id) {
    showMessage('Tem certeza que deseja excluir este histórico?', 'confirm', async (result) => {
        if (result) {
            try {
                await fetchData(`${BASE_URL_GESTOR}/historicos/${id}`, 'DELETE');
                showMessage('Histórico excluído com sucesso!', 'info', () => {
                    displayHistoricoIrrigacao();
                });
            } catch (error) {
                console.error('Erro ao excluir histórico:', error);
                showMessage('Erro ao excluir histórico.', 'error');
            }
        }
    });
}


// --- Funções Específicas para a Seção de Pivôs ---
async function loadPivoSection() {
    await loadContent('pivo', async () => {
        // Popula o combobox de fazendas para o Pivô
        await populateSelect('idFazendaPivo', `${BASE_URL_GESTOR}/fazendas`, 'id_fazenda', 'nome_fazenda');

        document.getElementById('pivoForm').addEventListener('submit', (event) => {
            event.preventDefault();
            savePivo();
        });
        document.getElementById('btnAddPivoForm').addEventListener('click', () => {
            document.getElementById('pivoFormContainer').style.display = 'block'; // Mostra o formulário
            document.getElementById('pivoForm').reset();
            editingPivoId = null;
            document.getElementById('btnSalvarPivo').textContent = 'Salvar Pivô';
            document.getElementById('pivoFormContainer').scrollIntoView({ behavior: 'smooth' });
        });
        document.getElementById('btnCancelarPivo').addEventListener('click', () => {
            editingPivoId = null;
            document.getElementById('pivoForm').reset();
            document.getElementById('btnSalvarPivo').textContent = 'Salvar Pivô';
            document.getElementById('pivoFormContainer').style.display = 'none'; // Esconde o formulário
        });
        displayPivos(); // Carrega a lista de pivôs ao iniciar
    }, true); // Requer autenticação
}

async function savePivo() {
    const nomePivo = document.getElementById('nomePivo').value;
    const idFazendaPivo = document.getElementById('idFazendaPivo').value;

    if (!nomePivo || !idFazendaPivo) {
        showMessage('Por favor, preencha o nome do pivô e selecione a fazenda!', 'error');
        return;
    }

    const pivoData = {
        nome_pivo: nomePivo,
        id_fazenda: parseInt(idFazendaPivo)
    };

    let response;
    try {
        if (editingPivoId) {
            response = await fetchData(`${BASE_URL_GESTOR}/pivos/${editingPivoId}`, 'PUT', pivoData);
            if (response) {
                showMessage('Pivô atualizado com sucesso!', 'info', () => {
                    editingPivoId = null;
                    document.getElementById('btnSalvarPivo').textContent = 'Salvar Pivô';
                    document.getElementById('pivoForm').reset();
                    document.getElementById('pivoFormContainer').style.display = 'none';
                    displayPivos();
                });
            }
        } else {
            response = await fetchData(`${BASE_URL_GESTOR}/pivos`, 'POST', pivoData);
            if (response) {
                showMessage('Pivô cadastrado com sucesso!', 'info', () => {
                    document.getElementById('pivoForm').reset();
                    document.getElementById('pivoFormContainer').style.display = 'none';
                    displayPivos();
                });
            }
        }
    } catch (error) {
        console.error('Erro ao salvar pivô:', error);
        showMessage('Erro ao salvar pivô. Verifique os dados e tente novamente.', 'error');
    }
}

async function displayPivos() {
    const pivosContainer = document.getElementById('pivosContainer');
    const loadingPivos = document.getElementById('loadingPivos');
    if (!pivosContainer || !loadingPivos) return;

    loadingPivos.style.display = 'block'; // Mostra o spinner
    pivosContainer.innerHTML = ''; // Limpa o conteúdo anterior

    try {
        const pivos = await fetchData(`${BASE_URL_GESTOR}/pivos`);
        loadingPivos.style.display = 'none'; // Esconde o spinner

        if (!pivos || pivos.length === 0) {
            pivosContainer.innerHTML = '<p class="text-center mt-4" style="color: var(--text-dark);">Nenhum pivô cadastrado ainda.</p>';
            return;
        }

        for (const pivo of pivos) {
            let nomeFazenda = 'N/A';
            if (pivo.id_fazenda) {
                try {
                    const fazenda = await fetchData(`${BASE_URL_GESTOR}/fazendas/${pivo.id_fazenda}`);
                    if (fazenda) {
                        nomeFazenda = fazenda.nome_fazenda;
                    }
                } catch (error) {
                    console.warn(`Não foi possível carregar o nome da fazenda para o ID ${pivo.id_fazenda}:`, error);
                }
            }

            const pivoCard = document.createElement('div');
            pivoCard.classList.add('pivo-card');
            pivoCard.innerHTML = `
                <div class="pivo-actions">
                    <div class="pivo-refresh-group">
                        <button class="pivo-refresh-btn"><i class="fas fa-sync-alt"></i></button>
                        <button class="pivo-refresh-btn edit-btn" data-id="${pivo.id}"><i class="fas fa-edit"></i></button>
                    </div>
                    <button class="pivo-control-btn btn-ligar" data-id="${pivo.id}">Liga</button>
                    <button class="pivo-control-btn btn-desligar" data-id="${pivo.id}">Desliga</button>
                </div>
                <div class="pivo-circle">
                    <i class="fas fa-water"></i> 
                </div>
                <div class="pivo-info">
                    <h4 style="color: var(--primary-dark);">${pivo.nome_pivo || 'Pivô Sem Nome'}</h4>
                    <p>Cultura: Soja (Exemplo)</p>
                    <p>Status: Parado (Exemplo)</p>
                    <p>Fazenda: ${nomeFazenda}</p>
                    <p>Histórico: 13/04 (Exemplo)</p>
                </div>
            `;
            pivosContainer.appendChild(pivoCard);

            // Adiciona listeners para os botões de ação e controle
            pivoCard.querySelector('.edit-btn')?.addEventListener('click', () => editPivo(pivo.id));
            pivoCard.querySelector('.btn-ligar')?.addEventListener('click', () => showMessage(`Comando 'Ligar' enviado para ${pivo.nome_pivo}`, 'info'));
            pivoCard.querySelector('.btn-desligar')?.addEventListener('click', () => showMessage(`Comando 'Desligar' enviado para ${pivo.nome_pivo}`, 'info'));
            // Adicione aqui a lógica real para ligar/desligar pivôs, se tiver rotas de API para isso
        }
    } catch (error) {
        console.error('Erro ao exibir pivôs:', error);
        loadingPivos.style.display = 'none';
        pivosContainer.innerHTML = '<p class="text-center mt-4" style="color:red;">Erro ao carregar pivôs.</p>';
    }
}

async function editPivo(id) {
    try {
        const pivoToEdit = await fetchData(`${BASE_URL_GESTOR}/pivos/${id}`);
        if (pivoToEdit) {
            document.getElementById('nomePivo').value = pivoToEdit.nome_pivo || '';
            await populateSelect('idFazendaPivo', `${BASE_URL_GESTOR}/fazendas`, 'id_fazenda', 'nome_fazenda', pivoToEdit.id_fazenda);

            editingPivoId = id;
            document.getElementById('btnSalvarPivo').textContent = 'Atualizar Pivô';
            document.getElementById('pivoFormContainer').style.display = 'block'; // Mostra o formulário
            document.getElementById('pivoFormContainer').scrollIntoView({ behavior: 'smooth' });
        } else {
            showMessage('Pivô não encontrado para edição.', 'error');
        }
    } catch (error) {
        console.error('Erro ao carregar pivô para edição:', error);
    }
}

// Não há função deletePivo na UI da imagem, mas se precisar de uma, pode ser adicionada aqui
// function deletePivo(id) { ... }


// --- Inicialização (Chamada no carregamento da página) ---
document.addEventListener('DOMContentLoaded', () => {
    createMessageContainer(); // Garante que o container de mensagens existe

    // Links fixos do navbar
    document.getElementById('btnHome')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadHomeSection();
    });

    // Event listeners para os links do rodapé (sempre visíveis)
    document.getElementById('footerLinkSobreNos')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadAboutUsSection();
    });
    document.getElementById('footerLinkGuia')?.addEventListener('click', (e) => {
        e.preventDefault();
        loadGuiaSection();
    });

    // Verifica o status de login e carrega a navegação e a tela inicial adequada
    if (localStorage.getItem('userToken')) {
        loadHomeSection(); // Ou loadPivoSection() se essa for a tela inicial pós-login
    } else {
        loadLoginSection();
    }
    updateNavigationForLoggedInUser(); // Configura os botões de navegação inicial
});