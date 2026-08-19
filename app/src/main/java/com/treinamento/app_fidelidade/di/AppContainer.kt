package com.treinamento.app_fidelidade.di

import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.service.ResgateService
import com.treinamento.app_fidelidade.repository.CarrinhoRepository
import com.treinamento.app_fidelidade.repository.CarrinhoRepositoryEmMemoria
import com.treinamento.app_fidelidade.repository.Conexao
import com.treinamento.app_fidelidade.repository.ConexaoMock
import com.treinamento.app_fidelidade.repository.ResgateRepository
import com.treinamento.app_fidelidade.repository.ResgateRepositoryPadrao
import com.treinamento.app_fidelidade.repository.SaldoPontosRepositorio
import com.treinamento.app_fidelidade.repository.SaldoRepository

/**
 * Ponto unico onde as dependencias sao montadas.
 *
 * Isto e o "grafo" do app feito na mao, sem Hilt nem Koin: quem cria o service, quem
 * cria os repositorios e quem os entrega para os ViewModels.
 *
 * Regra: **ninguem de dentro do ViewModel olha para ca**. O container so aparece nas
 * Factory dos ViewModels, que sao a fronteira entre o Android e o nosso codigo. E o
 * que garante que o ViewModel receba tudo pronto pelo construtor e possa ser criado
 * com dublês em um teste.
 *
 * TODO: quando entrar o Room, isto vira um DefaultAppContainer instanciado por uma
 * subclasse de Application, que e quem tem o Context necessario para abrir o banco.
 */
object AppContainer {

    private val resgateService = ResgateService(RetrofitInstance.api)

    val carrinhoRepository: CarrinhoRepository = CarrinhoRepositoryEmMemoria()

    val resgateRepository: ResgateRepository = ResgateRepositoryPadrao(resgateService)

    val saldoRepository: SaldoRepository = SaldoPontosRepositorio

    val conexao: Conexao = ConexaoMock
}
