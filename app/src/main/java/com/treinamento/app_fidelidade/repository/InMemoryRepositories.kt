package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.model.Produto
import com.treinamento.app_fidelidade.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class InMemoryProdutoRepository : ProdutoRepository {
    private val produtos = MutableStateFlow(
        listOf(
            Produto(1, "Dipirona 1g 10 comprimidos", "Analgesico generico - parceiro Drogasil", 120, "Medicamentos", 100),
            Produto(2, "Paracetamol 750mg 20 comprimidos", "Analgesico e antitermico - parceiro Drogasil", 180, "Medicamentos", 90),
            Produto(3, "Ibuprofeno 400mg 20 comprimidos", "Anti-inflamatorio nao esteroidal - parceiro Ultrafarma", 210, "Bem-estar", 80),
            Produto(4, "Loratadina 10mg 12 comprimidos", "Antialergico de uso diario - parceiro Farmacia do Povo", 240, "Higiene", 70),
            Produto(5, "Vitamina C efervescente 10 comprimidos", "Suplemento diario - parceiro Ultrafarma", 220, "Bem-estar", 60),
            Produto(6, "Polivitaminico adulto 60 capsulas", "Suplementacao mensal - parceiro Ultrafarma", 460, "Bem-estar", 50),
            Produto(7, "Fralda geriatrica M pacote 8 unidades", "Cuidado diario - parceiro Drogasil", 390, "Medicamentos", 40),
            Produto(8, "Sabonete liquido antisseptico 250ml", "Higiene e protecao - parceiro Farmacia do Povo", 170, "Higiene", 30),
            Produto(9, "Fio dental 50m", "Higiene bucal diaria - parceiro Farmacia do Povo", 320, "Higiene", 20),
            Produto(10, "Enxaguante bucal sem alcool 500ml", "Cuidado bucal completo - parceiro Drogasil", 290, "Medicamentos", 10),
            Produto(11, "Shampoo anticaspa 200ml", "Cuidado capilar - parceiro Farmacia do Povo", 280, "Higiene", 5),
            Produto(12, "Condicionador hidratante 200ml", "Cuidado capilar diario - parceiro Ultrafarma", 270, "Bem-estar", 0),
            Produto(13, "Desodorante aerosol 150ml", "Higiene pessoal - parceiro Drogasil", 260, "Medicamentos", 0),
            Produto(14, "Protetor solar FPS 50 120ml", "Protecao diaria UVA/UVB - parceiro Drogasil", 620, "Medicamentos", 0),
            Produto(15, "Repelente spray 200ml", "Protecao contra insetos - parceiro Ultrafarma", 300, "Bem-estar", 0),
            Produto(16, "Termometro digital", "Monitoramento de temperatura corporal - parceiro Farmacia do Povo", 520, "Higiene", 0),
            Produto(17, "Aparelho de pressao digital de braco", "Monitoramento domestico - parceiro Ultrafarma", 1450, "Bem-estar", 0),
            Produto(18, "Nebulizador inalador", "Suporte respiratorio residencial - parceiro Farmacia do Povo", 1690, "Higiene", 0),
            Produto(19, "Kit curativo 35 itens", "Primeiros socorros para casa - parceiro Drogasil", 680, "Medicamentos", 0),
            Produto(20, "Mascara cirurgica caixa 50 unidades", "Protecao respiratoria - parceiro Ultrafarma", 340, "Bem-estar", 0)
        )
    )
    override fun observarProdutos(): Flow<List<Produto>> = produtos
    override suspend fun atualizarProdutos(): Result<Unit> = Result.success(Unit)
}

class InMemoryUsuarioRepository : UsuarioRepository {
    private val usuario = MutableStateFlow(
        Usuario(
            id = 123456789, 
            nome = "Natalie R. Rowan",
            email = "natalierrowan@gmail.com",
            endereco = "Rua das Flores, 123",
            saldoPontos = 13_123_678, 
            qrCode = "FIDELIDADE-123456789"
        )
    )
    override fun observarUsuario(): Flow<Usuario> = usuario
    override suspend fun atualizarUsuario(): Result<Unit> = Result.success(Unit)
    
    override suspend fun atualizarDados(nome: String, email: String, endereco: String): Result<Unit> {
        usuario.update { it.copy(nome = nome, email = email, endereco = endereco) }
        return Result.success(Unit)
    }

    override suspend fun alterarSenha(senhaAtual: String, novaSenha: String): Result<Unit> =
        if (senhaAtual.isBlank() || novaSenha.length < 6) {
            Result.failure(IllegalArgumentException("Verifique as senhas informadas."))
        } else Result.success(Unit)
}
