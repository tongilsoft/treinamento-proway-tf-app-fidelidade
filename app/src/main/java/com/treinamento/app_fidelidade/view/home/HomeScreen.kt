package com.treinamento.app_fidelidade.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.treinamento.app_fidelidade.R
import com.treinamento.app_fidelidade.viewmodel.FiltroExtrato
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import com.treinamento.app_fidelidade.ui.components.FidelidadeBottomBar
import com.treinamento.app_fidelidade.ui.theme.BackgroundColor
import com.treinamento.app_fidelidade.ui.theme.BorderColor
import com.treinamento.app_fidelidade.ui.theme.FieldColor
import com.treinamento.app_fidelidade.ui.theme.NegativeRed
import com.treinamento.app_fidelidade.ui.theme.PointsYellow
import com.treinamento.app_fidelidade.ui.theme.PositiveGreen
import com.treinamento.app_fidelidade.ui.theme.PrimaryBlue
import com.treinamento.app_fidelidade.ui.theme.SecondaryTextColor
import com.treinamento.app_fidelidade.ui.theme.TextColor
import com.treinamento.app_fidelidade.ui.theme.WarningBlue

data class Transaction(
    val id: String,
    val points: Int,
    val place: String,
    val date: String,
    val isEarning: Boolean
)

/**
 * Converte uma movimentacao da API no item que a Home desenha.
 *
 * Debito vira valor negativo, para o card mostrar "-300 pontos" sem a tela
 * precisar saber o que e "tipo".
 */
private fun MovimentacaoResponse.paraTransaction(): Transaction {
    val ehCredito = tipo.equals("credito", ignoreCase = true)
    val valor = valorPontos.toInt()

    return Transaction(
        id = id.toString(),
        points = if (ehCredito) valor else -valor,
        place = descricao,
        date = formatarData(data),
        isEarning = ehCredito
    )
}

/** A API manda "2026-08-01"; a tela mostra "01/08/2026". */
private fun formatarData(dataApi: String): String {
    val partes = dataApi.take(10).split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else dataApi
}

data class Offer(
    val id: String,
    val title: String,
    val subtitle: String,
    val titleColor: Color
)

data class Partner(
    val id: String,
    val name: String,
    val distance: String,
    val logoUrl: String? = null
)

//@Preview(showBackground = true)
@Composable
fun HomeScreen(
    usuario: Usuario?,
    extrato: List<MovimentacaoResponse>,
    filtro: FiltroExtrato,
    onFiltroSelecionado: (FiltroExtrato) -> Unit,
    onNavigate: (String) -> Unit
) {


    val userName = usuario?.name ?: ""
    val pointsBalance = usuario?.pontosSaldo?.toString() ?: "0"

    // O extrato inteiro entra aqui: credito, debito e transferencia.
    val transactions = extrato.map { it.paraTransaction() }
    
    val offers = listOf(
        Offer("1", "10% OFF", "Em parceiros selecionados", NegativeRed),
        Offer("2", "200 pontos", "Na sua próxima compra", PointsYellow),
        Offer("3", "Frete grátis", "Acima de 500 pontos", Color(0xFF4CAF50))
    )
    
    val partners = listOf(
        Partner("1", "Loja A", "1,2 km")
    )

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            FidelidadeBottomBar(
                selecionado = "home",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            // Header
            item { HomeHeader(userName) }
            
            // Points Card
            item { PointsCard(pointsBalance) }
            
            // Offline Warning
            item { OfflineWarning() }
            
            // Filters
            item { StatementFilters(filtro, onFiltroSelecionado) }
            
            // Statement Section
            item { 
                SectionHeader(
                    title = stringResource(R.string.points_statement),
                    onViewAllClick = {}
                )
            }
            if (transactions.isEmpty()) {
                item { ExtratoVazio(filtro) }
            } else {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
            
            // Offers Section
            item { 
                SectionHeader(
                    title = stringResource(R.string.offers_title),
                    onViewAllClick = {}
                )
            }
            item { OffersRow(offers) }
            
            // Partners Section
            item { 
                SectionHeader(
                    title = stringResource(R.string.nearby_partners),
                    onViewAllClick = {}
                )
            }
            items(partners) { partner ->
                PartnerItem(partner)
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HomeHeader(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.hello_user, userName),
            color = TextColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = TextColor
            )
        }
    }
}

@Composable
fun PointsCard(balance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FieldColor),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.points_balance),
                    color = SecondaryTextColor,
                    fontSize = 14.sp
                )
                Text(
                    text = balance,
                    color = PointsYellow,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.points_label),
                    color = SecondaryTextColor,
                    fontSize = 14.sp
                )
            }
            
            // Coin Stack Icon Replacement
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = PointsYellow,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun OfflineWarning() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(WarningBlue)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.offline_warning),
            color = PrimaryBlue,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun StatementFilters(
    filtro: FiltroExtrato,
    onFiltroSelecionado: (FiltroExtrato) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(R.string.statement_filter),
            color = TextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                text = stringResource(R.string.filter_all),
                isSelected = filtro == FiltroExtrato.TODOS,
                onClick = { onFiltroSelecionado(FiltroExtrato.TODOS) }
            )
            FilterChip(
                text = stringResource(R.string.filter_earnings),
                isSelected = filtro == FiltroExtrato.GANHOS,
                onClick = { onFiltroSelecionado(FiltroExtrato.GANHOS) }
            )
            FilterChip(
                text = stringResource(R.string.filter_spendings),
                isSelected = filtro == FiltroExtrato.GASTOS,
                onClick = { onFiltroSelecionado(FiltroExtrato.GASTOS) }
            )
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendário",
                    tint = SecondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) PrimaryBlue else FieldColor
    val textColor = if (isSelected) Color.White else SecondaryTextColor
    val borderStroke = if (isSelected) null else BorderStroke(1.dp, BorderColor)
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        border = borderStroke,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp
        )
    }
}

/** Sem esta mensagem, filtrar uma aba sem movimentacao deixaria a secao muda. */
@Composable
fun ExtratoVazio(filtro: FiltroExtrato) {
    Text(
        text = when (filtro) {
            FiltroExtrato.TODOS -> "Voce ainda nao tem movimentacoes."
            FiltroExtrato.GANHOS -> "Nenhum ganho de pontos ate agora."
            FiltroExtrato.GASTOS -> "Nenhum gasto de pontos ate agora."
        },
        color = SecondaryTextColor,
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SectionHeader(title: String, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.view_all),
            color = PrimaryBlue,
            fontSize = 14.sp,
            modifier = Modifier.clickable(onClick = onViewAllClick)
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = if (transaction.isEarning) PositiveGreen else PointsYellow,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${if (transaction.points > 0) "+" else ""}${transaction.points} pontos",
                color = TextColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = transaction.place,
                color = SecondaryTextColor,
                fontSize = 13.sp
            )
        }
        Text(
            text = transaction.date,
            color = SecondaryTextColor,
            fontSize = 13.sp
        )
    }
}

@Composable
fun OffersRow(offers: List<Offer>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(offers) { offer ->
            OfferCard(offer)
        }
    }
}

@Composable
fun OfferCard(offer: Offer) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FieldColor),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = offer.title,
                color = offer.titleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = offer.subtitle,
                color = SecondaryTextColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PartnerItem(partner: Partner) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FieldColor),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "LOGO", color = SecondaryTextColor, fontSize = 8.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = partner.name,
                    color = TextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = partner.distance,
                    color = SecondaryTextColor,
                    fontSize = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SecondaryTextColor
            )
        }
    }
}

//@Composable
//fun HomeBottomNavigation() {
//    NavigationBar(
//        containerColor = BackgroundColor,
//        contentColor = SecondaryTextColor,
//        tonalElevation = 8.dp
//    ) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
//            label = { Text("Home") },
//            selected = true,
//            onClick = { },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryBlue,
//                selectedTextColor = PrimaryBlue,
//                unselectedIconColor = SecondaryTextColor,
//                unselectedTextColor = SecondaryTextColor,
//                indicatorColor = BackgroundColor
//            )
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Catálogo") },
//            label = { Text("Catálogo") },
//            selected = false,
//            onClick = { },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryBlue,
//                selectedTextColor = PrimaryBlue,
//                unselectedIconColor = SecondaryTextColor,
//                unselectedTextColor = SecondaryTextColor,
//                indicatorColor = BackgroundColor
//            )
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "QR Code") },
//            label = { Text("QR Code") },
//            selected = false,
//            onClick = { },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryBlue,
//                selectedTextColor = PrimaryBlue,
//                unselectedIconColor = SecondaryTextColor,
//                unselectedTextColor = SecondaryTextColor,
//                indicatorColor = BackgroundColor
//            )
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
//            label = { Text("Perfil") },
//            selected = false,
//            onClick = { },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryBlue,
//                selectedTextColor = PrimaryBlue,
//                unselectedIconColor = SecondaryTextColor,
//                unselectedTextColor = SecondaryTextColor,
//                indicatorColor = BackgroundColor
//            )
//        )
//    }
//}
