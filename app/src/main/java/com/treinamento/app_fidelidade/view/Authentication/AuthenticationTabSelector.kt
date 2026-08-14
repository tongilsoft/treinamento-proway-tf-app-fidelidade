package com.treinamento.app_fidelidade.view.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.treinamento.app_fidelidade.R


private val BackgroundColor = Color(0xFF001427)
private val BorderColor = Color(0xFF1D3B57)

@Composable
fun AuthenticationTabSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(13.dp))
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(13.dp)
            )
            .background(BackgroundColor)
    ) {
        AuthenticationTab(
            text = stringResource(id = R.string.login_tab),
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )

        AuthenticationTab(
            text = stringResource(id = R.string.register_tab),
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )
    }
}