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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.NFLTeams
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TurfGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChallengeDialog(
    userWalletBalance: Double,
    myFavoriteTeam: String,
    onDismiss: () -> Unit,
    onCreateChallenge: (
        opponentGamertag: String,
        opponentPlatform: String,
        myTeamAbbr: String,
        opponentTeamAbbr: String,
        stakeAmount: Double,
        quarterMinutes: Int,
        difficulty: String,
        gameStyle: String,
        customPlaybooks: Boolean,
        sideBetText: String,
        sideBetStake: Double
    ) -> Unit
) {
    var opponentGamertag by remember { mutableStateOf("") }
    var opponentPlatform by remember { mutableStateOf("PlayStation 5") }
    var myTeam by remember { mutableStateOf(myFavoriteTeam) }
    var opponentTeam by remember { mutableStateOf("BAL") }
    var stakeAmount by remember { mutableDoubleStateOf(25.0) }
    var customStakeText by remember { mutableStateOf("") }
    var quarterMinutes by remember { mutableIntStateOf(5) }
    var difficulty by remember { mutableStateOf("All-Madden") }
    var gameStyle by remember { mutableStateOf("Competitive") }
    var customPlaybooks by remember { mutableStateOf(true) }
    var sideBetText by remember { mutableStateOf("") }
    var sideBetStake by remember { mutableDoubleStateOf(0.0) }

    var expandedMyTeam by remember { mutableStateOf(false) }
    var expandedOppTeam by remember { mutableStateOf(false) }

    val stakePresets = listOf(10.0, 25.0, 50.0, 100.0)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CREATE 1v1 CASH DUEL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = ElectricGold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Madden 27 Escrow Wager Setup",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Opponent Gamertag input
                OutlinedTextField(
                    value = opponentGamertag,
                    onValueChange = { opponentGamertag = it },
                    label = { Text("Opponent Gamertag (or leave blank for Open Lobby)") },
                    placeholder = { Text("e.g. LowryElite or BlitzGod99") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricGold,
                        unfocusedBorderColor = BorderLight,
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("opponent_gamertag_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Platform picker
                Text(
                    text = "Platform / Network",
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
                    listOf("PlayStation 5", "Xbox Series X", "PC EA App").forEach { platform ->
                        FilterChip(
                            selected = opponentPlatform == platform,
                            onClick = { opponentPlatform = platform },
                            label = { Text(platform, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                                selectedLabelColor = CyanAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stake Amount Picker
                Text(
                    text = "Stake Amount per Player",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ElectricGold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stakePresets.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (stakeAmount == preset) ElectricGold else SurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (stakeAmount == preset) ElectricGold else BorderLight
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    stakeAmount = preset
                                    customStakeText = ""
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$${preset.toInt()}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (stakeAmount == preset) Color(0xFF1E1100) else TextPrimaryDark
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Escrow pot preview card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceVariantDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Total Winner Pot",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                                )
                                Text(
                                    text = "$${String.format("%.2f", stakeAmount * 2)}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = ElectricGold
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Zero Platform Fee",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TurfGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Teams selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // My Team
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Your Team",
                            style = MaterialTheme.typography.labelMedium.copy(color = TextPrimaryDark)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedMyTeam,
                            onExpandedChange = { expandedMyTeam = it }
                        ) {
                            OutlinedTextField(
                                value = myTeam,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMyTeam) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricGold,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMyTeam,
                                onDismissRequest = { expandedMyTeam = false }
                            ) {
                                NFLTeams.allTeams.forEach { team ->
                                    DropdownMenuItem(
                                        text = { Text("${team.city} ${team.name} (${team.overallRating} OVR)") },
                                        onClick = {
                                            myTeam = team.abbreviation
                                            expandedMyTeam = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Opponent Team
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Opponent Team",
                            style = MaterialTheme.typography.labelMedium.copy(color = TextPrimaryDark)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedOppTeam,
                            onExpandedChange = { expandedOppTeam = it }
                        ) {
                            OutlinedTextField(
                                value = opponentTeam,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOppTeam) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricGold,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedOppTeam,
                                onDismissRequest = { expandedOppTeam = false }
                            ) {
                                NFLTeams.allTeams.forEach { team ->
                                    DropdownMenuItem(
                                        text = { Text("${team.city} ${team.name} (${team.overallRating} OVR)") },
                                        onClick = {
                                            opponentTeam = team.abbreviation
                                            expandedOppTeam = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Game Rules (Quarter length, difficulty, playbook)
                Text(
                    text = "Match Rules & Settings",
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
                    listOf(4, 5, 6).forEach { mins ->
                        FilterChip(
                            selected = quarterMinutes == mins,
                            onClick = { quarterMinutes = mins },
                            label = { Text("${mins}m Quarters") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricGold.copy(alpha = 0.2f),
                                selectedLabelColor = ElectricGold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All-Madden", "All-Pro").forEach { diff ->
                        FilterChip(
                            selected = difficulty == diff,
                            onClick = { difficulty = diff },
                            label = { Text(diff) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                                selectedLabelColor = CyanAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Custom Playbooks",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimaryDark)
                        )
                        Text(
                            text = if (customPlaybooks) "Allowed" else "Official Team Only",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                        )
                    }
                    Switch(
                        checked = customPlaybooks,
                        onCheckedChange = { customPlaybooks = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ElectricGold,
                            checkedTrackColor = ElectricGold.copy(alpha = 0.5f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional Side Bet / Prop
                OutlinedTextField(
                    value = sideBetText,
                    onValueChange = { sideBetText = it },
                    label = { Text("Optional Prop / Side Bet") },
                    placeholder = { Text("e.g. First team to score a TD, or Under 400 yds") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = BorderLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (sideBetText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = if (sideBetStake > 0) sideBetStake.toInt().toString() else "",
                        onValueChange = { sideBetStake = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Side Bet Stake ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = BorderLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Create Action Button
                Button(
                    onClick = {
                        onCreateChallenge(
                            opponentGamertag,
                            opponentPlatform,
                            myTeam,
                            opponentTeam,
                            stakeAmount,
                            quarterMinutes,
                            difficulty,
                            gameStyle,
                            customPlaybooks,
                            sideBetText,
                            sideBetStake
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricGold,
                        contentColor = Color(0xFF1E1100)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_create_challenge_btn")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LOCK $${String.format("%.0f", stakeAmount)} & POST CHALLENGE",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
