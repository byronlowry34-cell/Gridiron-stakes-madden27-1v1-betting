package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerProfile
import com.example.data.model.WagerTransaction
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.FieryRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TurfGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LedgerScreen(
    profile: PlayerProfile,
    transactions: List<WagerTransaction>,
    onAddFundsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())

    val totalDepositedOrWon = transactions.filter { it.amount > 0 }.sumOf { it.amount }
    val totalStaked = transactions.filter { it.amount < 0 }.sumOf { -it.amount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("ledger_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vault Overview Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderHighlight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ESCROW & VAULT BALANCE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = ElectricGold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "$${String.format("%.2f", profile.balance)}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                        }

                        Button(
                            onClick = onAddFundsClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricGold,
                                contentColor = Color(0xFF1E1100)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Top Up", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Summary Stats Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Lifetime Payouts", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "+$${String.format("%.2f", totalDepositedOrWon)}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TurfGreen
                                )
                            )
                        }
                        Column {
                            Text("Stakes Locked", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "-$${String.format("%.2f", totalStaked)}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FieryRed
                                )
                            )
                        }
                        Column {
                            Text("Net P/L", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            val net = totalDepositedOrWon - totalStaked
                            Text(
                                "${if (net >= 0) "+" else ""}$${String.format("%.2f", net)}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (net >= 0) ElectricGold else FieryRed
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRANSACTION & PAYOUT LEDGER",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "${transactions.size} records",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                )
            }
        }

        // Transaction items
        if (transactions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceVariantDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = TextMutedDark, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No Transactions Yet", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark))
                    }
                }
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
                val isCredit = tx.amount >= 0

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCredit) TurfGreen.copy(alpha = 0.2f) else FieryRed.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (isCredit) TurfGreen else FieryRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = tx.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = dateFormat.format(Date(tx.timestamp)),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondaryDark,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "${if (isCredit) "+" else ""}$${String.format("%.2f", tx.amount)}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isCredit) TurfGreen else FieryRed
                            )
                        )
                    }
                }
            }
        }
    }
}
