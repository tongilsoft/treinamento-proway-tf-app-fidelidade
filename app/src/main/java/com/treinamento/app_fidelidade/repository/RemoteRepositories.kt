package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import com.treinamento.app_fidelidade.model.Produto
import com.treinamento.app_fidelidade.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RemoteProdutoRepository(private val api: FidelidadeApi) : ProdutoRepository {
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    override fun observarProdutos(): Flow<List<Produto>> = _produtos.asStateFlow()

    override suspend fun atualizarProdutos(): Result<Unit> = try {
        val response = api.getProdutos()
        if (response.success) {
            _produtos.value = response.data.map { dto ->
                Produto(
                    id = dto.id.toLong(),
                    nome = dto.name,
                    descricao = dto.descricao,
                    valorPontos = dto.valorPontos.toLong(),
                    categoria = when(dto.idCategoria.toInt()) {
                        1 -> "Medicamentos"
                        2 -> "Bem-estar"
                        3 -> "Higiene"
                        else -> "Geral"
                    }
                )
            }
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message ?: "Erro ao buscar produtos"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class RemoteUsuarioRepository(private val api: FidelidadeApi) : UsuarioRepository {
    private val _usuario = MutableStateFlow(
        Usuario(0, "", "", null, 0, "")
    )
    override fun observarUsuario(): Flow<Usuario> = _usuario.asStateFlow()

    override suspend fun atualizarUsuario(): Result<Unit> = try {
        val response = api.getMeusDados()
        if (response.success) {
            val dto = response.data
            _usuario.value = Usuario(
                id = dto.id.toLong(),
                nome = dto.name,
                email = dto.email,
                pontosSaldo = dto.pontosSaldo.toLong(),
                qrCode = dto.qrCode
            )
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message ?: "Erro ao buscar dados do usuário"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun atualizarDados(nome: String, email: String): Result<Unit> {
        // Implementar se houver endpoint no backend
        return Result.success(Unit)
    }

    override suspend fun alterarSenha(senhaAtual: String, novaSenha: String): Result<Unit> {
        // Implementar se houver endpoint no backend
        return Result.success(Unit)
    }
}
