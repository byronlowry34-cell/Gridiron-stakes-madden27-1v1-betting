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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MaddenMatch
import com.example.data.model.MatchStatus
import com.example.data.model.NFLTeams
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.FieryRed
import com.example.ui.theme.GoldDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TurfGreen

@Composable
fun MatchCard(
    match: MaddenMatch,
    currentUserGamertag: String,
    onSelectMatch: () -> Unit,
    onJoinMatch: () -> Unit,
    onSubmitResult: () -> Unit,
    onDisputeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrentUserP1 = match.player1Gamertag.equals(currentUserGamertag, ignoreCase = true)
    val isCurrentUserP2 = match.player2Gamertag.equals(currentUserGamertag, ignoreCase = true)
    val isUserInMatch = isCurrentUserP1 || isCurrentUserP2

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (match.status == MatchStatus.IN_PROGRESS) BorderHighlight else BorderLight
        ),
        tonalElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectMatch)
            .testTag("match_card_${match.matchCode}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Code + Stake & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceVariantDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                    ) {
                        Text(
                            text = match.matchCode,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${match.quarterMinutes}m • ${match.difficulty}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                    )
                }

                // Status pill
                StatusBadge(status = match.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Duel Arena Section: Player 1 VS Player 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariantDark)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player 1
                PlayerDuelColumn(
                    gamertag = match.player1Gamertag,
                    teamAbbr = match.player1TeamAbbr,
                    score = match.player1Score,
                    isCurrentUser = isCurrentUserP1,
                    isWinner = match.winnerGamertag.equals(match.player1Gamertag, ignoreCase = true)
                )

                // VS & Live Clock Indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    if (match.status == MatchStatus.IN_PROGRESS) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = FieryRed.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(FieryRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Q${match.currentQuarter} ${match.timeRemaining}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = FieryRed,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "VS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = ElectricGold,
                                letterSpacing = 2.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Pot Cash Chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x33FFB703),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricGold)
                    ) {
                        Text(
                            text = "POT $${String.format("%.0f", match.totalPot)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                // Player 2
                PlayerDuelColumn(
                    gamertag = match.player2Gamertag,
                    teamAbbr = match.player2TeamAbbr,
                    score = match.player2Score,
                    isCurrentUser = isCurrentUserP2,
                    isWinner = match.winnerGamertag.equals(match.player2Gamertag, ignoreCase = true)
                )
            }

            // Side Bet / Prop note if present
            if (match.sideBetDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Side Bet",
                            tint = CyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Prop Bet: ${match.sideBetDescription} (+$${String.format("%.0f", match.sideBetAmount)})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextPrimaryDark,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (match.status) {
                    MatchStatus.OPEN -> {
                        if (!isCurrentUserP1) {
                            Button(
                                onClick = onJoinMatch,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricGold,
                                    contentColor = Color(0xFF1E1100)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("join_match_btn_${match.matchCode}")
                            ) {
                                Icon(Icons.Default.SportsFootball, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Accept & Stake $${String.format("%.0f", match.stakeAmount)}",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            OutlinedButton(
                                onClick = onSelectMatch,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Waiting for Opponent...", color = TextSecondaryDark)
                            }
                        }
                    }

                    MatchStatus.ESCROW_LOCKED, MatchStatus.IN_PROGRESS -> {
                        Button(
                            onClick = onSelectMatch,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TurfGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("open_scorekeeper_btn_${match.matchCode}")
                        ) {
                            Icon(Icons.Default.SportsScore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (match.status == MatchStatus.IN_PROGRESS) "Live Game Tracker" else "Start Match & Lock Escrow",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    MatchStatus.PENDING_CONFIRM -> {
                        Button(
                            onClick = onSubmitResult,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanAccent,
                                contentColor = Color(0xFF00363D)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Confirm Score & Release Pot", fontWeight = FontWeight.Bold)
                        }
                    }

                    MatchStatus.DISPUTED -> {
                        Button(
                            onClick = onDisputeClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FieryRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Admin Proof Dispute", fontWeight = FontWeight.Bold)
                        }
                    }

                    MatchStatus.COMPLETED -> {
                        OutlinedButton(
                            onClick = onSelectMatch,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TurfGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "View Box Score & Payout",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerDuelColumn(
    gamertag: String,
    teamAbbr: String,
    score: Int,
    isCurrentUser: Boolean,
    isWinner: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(105.dp)
    ) {
        TeamBadge(teamAbbreviation = teamAbbr)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = gamertag,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                color = if (isCurrentUser) ElectricGold else TextPrimaryDark,
                fontSize = 11.sp
            ),
            maxLines = 1
        )
        if (isWinner) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = TurfGreen.copy(alpha = 0.2f),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = "WINNER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TurfGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 8.sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                color = if (isWinner) TurfGreen else Color.White
            )
        )
    }
}

@Composable
fun StatusBadge(status: MatchStatus) {
    val (label, bg, fg, border) = when (status) {
        MatchStatus.OPEN -> Quad("OPEN", Color(0x2200E5FF), CyanAccent, CyanAccent)
        MatchStatus.ESCROW_LOCKED -> Quad("LOCKED", Color(0x33FFB703), ElectricGold, ElectricGold)
        MatchStatus.IN_PROGRESS -> Quad("LIVE", Color(0x33EF4444), FieryRed, FieryRed)
        MatchStatus.PENDING_CONFIRM -> Quad("CONFIRMING", Color(0x33FCA311), Color(0xFFFCA311), Color(0xFFFCA311))
        MatchStatus.COMPLETED -> Quad("SETTLED", Color(0x3310B981), TurfGreen, TurfGreen)
        MatchStatus.DISPUTED -> Quad("DISPUTED", Color(0x33EF4444), FieryRed, FieryRed)
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                color = fg,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
