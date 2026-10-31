package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MaddenMatch
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

@Composable
fun SubmitScoreDialog(
    match: MaddenMatch,
    currentUserGamertag: String,
    onDismiss: () -> Unit,
    onSubmitScore: (matchId: String, p1Score: Int, p2Score: Int, proofNote: String) -> Unit
) {
    var p1Score by remember { mutableIntStateOf(match.player1Score) }
    var p2Score by remember { mutableIntStateOf(match.player2Score) }
    var proofNote by remember { mutableStateOf("Madden 27 final box score screenshot uploaded & verified") }
    var hasSimulatedScreenshot by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SUBMIT FINAL SCORE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = ElectricGold
                            )
                        )
                        Text(
                            text = "Match Code: ${match.matchCode} • Pot: $${String.format("%.0f", match.totalPot)}",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Score Inputs Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // P1
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        TeamBadge(teamAbbreviation = match.player1TeamAbbr)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = match.player1Gamertag,
                            style = MaterialTheme.typography.labelSmall.copy(color = TextPrimaryDark),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = p1Score.toString(),
                            onValueChange = { p1Score = it.toIntOrNull() ?: 0 },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricGold,
                                unfocusedBorderColor = BorderLight
                            ),
                            textStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .width(80.dp)
                                .testTag("submit_p1_score_input")
                        )
                    }

                    Text(
                        text = "FINAL",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextMutedDark
                        )
                    )

                    // P2
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        TeamBadge(teamAbbreviation = match.player2TeamAbbr)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = match.player2Gamertag,
                            style = MaterialTheme.typography.labelSmall.copy(color = TextPrimaryDark),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = p2Score.toString(),
                            onValueChange = { p2Score = it.toIntOrNull() ?: 0 },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricGold,
                                unfocusedBorderColor = BorderLight
                            ),
                            textStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .width(80.dp)
                                .testTag("submit_p2_score_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Screenshot / Proof Attachment Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceVariantDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { hasSimulatedScreenshot = !hasSimulatedScreenshot }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (hasSimulatedScreenshot) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = if (hasSimulatedScreenshot) TurfGreen else CyanAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (hasSimulatedScreenshot) "Madden Box Score Attached" else "Attach Post-Game Screenshot",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasSimulatedScreenshot) TurfGreen else TextPrimaryDark
                                )
                            )
                            Text(
                                text = "Required to verify winner & protect against disputes",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMutedDark)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Winner summary
                val winnerName = when {
                    p1Score > p2Score -> match.player1Gamertag
                    p2Score > p1Score -> match.player2Gamertag
                    else -> "TIE (Requires Overtime)"
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x3310B981),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Winner: $winnerName will receive $${String.format("%.2f", match.totalPot)}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TurfGreen,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        onSubmitScore(match.id, p1Score, p2Score, proofNote)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurfGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_submit_score_btn")
                ) {
                    Text(
                        text = "CONFIRM & RELEASE POT PAYOUT",
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun DepositDialog(
    currentBalance: Double,
    onDismiss: () -> Unit,
    onDeposit: (amount: Double) -> Unit
) {
    val presets = listOf(20.0, 50.0, 100.0, 250.0)
    var selectedAmount by remember { mutableStateOf(50.0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GRIDIRON WALLET",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = ElectricGold
                            )
                        )
                        Text(
                            text = "Add Match Credits / Stakes",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceVariantDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Available Balance", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "$${String.format("%.2f", currentBalance)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = ElectricGold
                                )
                            )
                        }
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = ElectricGold, modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Credit Package",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presets.forEach { amt ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedAmount == amt) ElectricGold else SurfaceElevated,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedAmount = amt }
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "+$${amt.toInt()}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (selectedAmount == amt) Color(0xFF1E1100) else Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onDeposit(selectedAmount) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricGold,
                        contentColor = Color(0xFF1E1100)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_deposit_btn")
                ) {
                    Text(
                        text = "ADD +$${selectedAmount.toInt()} CREDITS",
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun JoinCodeDialog(
    onDismiss: () -> Unit,
    onJoin: (code: String, teamAbbr: String) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf("KC") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "JOIN 1v1 DUEL BY CODE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = CyanAccent
                            )
                        )
                        Text(
                            text = "Enter match code from opponent",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text("Match Code (e.g. MD-3301)") },
                    placeholder = { Text("MD-XXXX") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = BorderLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("join_code_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Your Team",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("KC", "SF", "BAL", "DET", "PHI").forEach { team ->
                        FilterChip(
                            selected = selectedTeam == team,
                            onClick = { selectedTeam = team },
                            label = { Text(team) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                                selectedLabelColor = CyanAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onJoin(code, selectedTeam) },
                    enabled = code.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanAccent,
                        contentColor = Color(0xFF00363D)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_join_by_code_btn")
                ) {
                    Text(
                        text = "JOIN MATCH & LOCK ESCROW",
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun DisputeDialog(
    match: MaddenMatch,
    onDismiss: () -> Unit,
    onResolve: (winner: String, p1Score: Int, p2Score: Int, notes: String) -> Unit
) {
    var winnerGamertag by remember { mutableStateOf(match.player1Gamertag) }
    var notes by remember { mutableStateOf("Verified from official Madden 27 screenshot box score.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = FieryRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DISPUTE TRIBUNAL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = FieryRed
                            )
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Both players submitted conflicting scores or claimed disconnect violations. Select the rightful winner according to Madden 27 rules:",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { winnerGamertag = match.player1Gamertag },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (winnerGamertag == match.player1Gamertag) TurfGreen else SurfaceElevated
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(match.player1Gamertag, maxLines = 1)
                    }

                    Button(
                        onClick = { winnerGamertag = match.player2Gamertag },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (winnerGamertag == match.player2Gamertag) TurfGreen else SurfaceElevated
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(match.player2Gamertag, maxLines = 1)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Resolution Reason / Proof Audit") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TurfGreen,
                        unfocusedBorderColor = BorderLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val p1Score = if (winnerGamertag == match.player1Gamertag) 28 else 21
                        val p2Score = if (winnerGamertag == match.player2Gamertag) 28 else 21
                        onResolve(winnerGamertag, p1Score, p2Score, notes)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TurfGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("APPLY RULING & RELEASE POT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
