package com.treinamento.app_fidelidade.feature.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.model.Produto
import com.treinamento.app_fidelidade.repository.CarrinhoRepository
import com.treinamento.app_fidelidade.repository.ProdutoRepository
import com.treinamento.app_fidelidade.repository.SaldoRepository
import com.treinamento.app_fidelidade.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private data class Controls(
    val busca: String = "",
    val categoriaSelecionada: String? = null,
    val produtoSelecionadoId: Long? = null,
    val quantidadeSelecionada: Int = 1,
    val atualizando: Boolean = false,
    val offline: Boolean = false,
    val mensagem: String? = null
)

class CatalogoViewModel(
    private val produtoRepository: ProdutoRepository,
    usuarioRepository: UsuarioRepository,
    private val carrinhoRepository: CarrinhoRepository,
    private val saldoRepository: SaldoRepository
) : ViewModel() {
    private val controls = MutableStateFlow(Controls())

    /** Catalogo sempre quente, para achar o produto na hora de jogar no carrinho. */
    private val produtos = produtoRepository.observarProdutos()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<CatalogoUiState> = combine(
        produtos,
        usuarioRepository.observarUsuario(),
        controls,
        carrinhoRepository.itens
    ) { produtos, usuario, c, itensCarrinho ->
        val filtrados = produtos
            .filter { c.busca.isBlank() || it.nome.contains(c.busca, true) || it.descricao.contains(c.busca, true) }
            .filter { c.categoriaSelecionada == null || it.categoria == c.categoriaSelecionada }
//            .ordenar(c.ordenacao)

        val categorias = produtos.map { it.categoria }.distinct().sorted()

        CatalogoUiState(
            carregando = false,
            atualizando = c.atualizando,
            produtos = filtrados,
            categorias = categorias,
            pontosAtuais = usuario.pontosSaldo,
            busca = c.busca,
//            ordenacao = c.ordenacao,
            categoriaSelecionada = c.categoriaSelecionada,
            quantidadeCarrinho = itensCarrinho.sumOf { item -> item.quantidade },
            produtoSelecionado = produtos.find { it.id == c.produtoSelecionadoId },
            quantidadeSelecionada = c.quantidadeSelecionada,
            offline = c.offline,
            mensagem = c.mensagem
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CatalogoUiState())

    fun onEvent(event: CatalogoEvent) {
        when (event) {
            is CatalogoEvent.Buscar -> controls.update { it.copy(busca = event.texto) }
            is CatalogoEvent.SelecionarProduto -> controls.update { it.copy(produtoSelecionadoId = event.id, quantidadeSelecionada = 1) }
            is CatalogoEvent.AjustarQuantidade -> controls.update { it.copy(quantidadeSelecionada = event.novaQuantidade.coerceAtLeast(1)) }
            is CatalogoEvent.AdicionarAoCarrinho -> {
                val produto = produtos.value.find { it.id == event.id }
                if (produto == null) {
                    controls.update { it.copy(mensagem = "Produto nao encontrado no catalogo.") }
                } else {
                    carrinhoRepository.adicionar(produto, event.quantidade)
                    controls.update {
                        it.copy(mensagem = "${event.quantidade} produto(s) adicionado(s) ao carrinho!")
                    }
                }
            }
            CatalogoEvent.LimparMensagem -> controls.update { it.copy(mensagem = null) }
            CatalogoEvent.Atualizar -> atualizar()
        }
    }

    private fun atualizar() = viewModelScope.launch {
        controls.update { it.copy(atualizando = true, mensagem = null) }
        // O saldo tambem se recupera aqui: "Seus pontos: 0" nao pode ficar preso
        // so porque a primeira carga pegou o servidor fora do ar.
        saldoRepository.atualizar()
        produtoRepository.atualizarProdutos()
            .onSuccess { controls.update { it.copy(atualizando = false, offline = false) } }
            .onFailure {
                controls.update {
                    it.copy(atualizando = false, offline = true, mensagem = "Exibindo dados offline")
                }
            }
    }
}

