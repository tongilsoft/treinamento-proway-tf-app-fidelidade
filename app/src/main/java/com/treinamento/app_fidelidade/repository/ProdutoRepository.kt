package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    fun observarProdutos(): Flow<List<Produto>>
    suspend fun atualizarProdutos(): Result<Unit>
}
