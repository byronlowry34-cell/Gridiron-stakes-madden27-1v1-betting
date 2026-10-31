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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsFootball
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
import com.example.ui.components.StatusBadge
import com.example.ui.components.TeamBadge
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
fun LiveGameScreen(
    match: MaddenMatch?,
    currentUserGamertag: String,
    onStartMatch: (String) -> Unit,
    onAddPointsP1: (MaddenMatch, Int) -> Unit,
    onAddPointsP2: (MaddenMatch, Int) -> Unit,
    onAdvanceQuarter: (MaddenMatch) -> Unit,
    onTurnoverP1: (MaddenMatch) -> Unit,
    onTurnoverP2: (MaddenMatch) -> Unit,
    onSubmitFinalScore: (MaddenMatch) -> Unit,
    onOpenDispute: (MaddenMatch) -> Unit,
    onCreateChallengeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (match == null) {
        EmptyLiveGameView(onCreateChallengeClick = onCreateChallengeClick, modifier = modifier)
        return
    }

    val team1 = NFLTeams.getTeam(match.player1TeamAbbr)
    val team2 = NFLTeams.getTeam(match.player2TeamAbbr)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("live_game_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Broadcast Jumbotron Header
        item {
            JumbotronScoreboard(
                match = match,
                team1 = team1,
                team2 = team2,
                currentUserGamertag = currentUserGamertag
            )
        }

        // Match status & Escrow banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariantDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "STAKE ESCROW VAULT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricGold,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "$${String.format("%.2f", match.totalPot)} ($${String.format("%.2f", match.stakeAmount)} each)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    StatusBadge(status = match.status)
                }
            }
        }

        // Start Match trigger if still in ESCROW_LOCKED or OPEN
        if (match.status == MatchStatus.ESCROW_LOCKED || match.status == MatchStatus.OPEN) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x3310B981),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TurfGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "READY FOR KICKOFF",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TurfGreen
                            )
                        )
                        Text(
                            text = "Both players ready. Launch Madden 27 and start the official game clock.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onStartMatch(match.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = TurfGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_live_game_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("START LIVE SCORE TRACKING", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Live Scoring Pad (if IN_PROGRESS or ESCROW_LOCKED)
        if (match.status == MatchStatus.IN_PROGRESS || match.status == MatchStatus.ESCROW_LOCKED) {
            item {
                Text(
                    text = "REAL-TIME SCOREKEEPER",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = ElectricGold,
                        letterSpacing = 1.sp
                    )
                )
            }

            // P1 & P2 Score Control Pads
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // P1 Pad
                    ScorePadColumn(
                        gamertag = match.player1Gamertag,
                        teamAbbr = match.player1TeamAbbr,
                        score = match.player1Score,
                        isHome = true,
                        onAddPoints = { pts -> onAddPointsP1(match, pts) },
                        onTurnover = { onTurnoverP1(match) },
                        modifier = Modifier.weight(1f)
                    )

                    // P2 Pad
                    ScorePadColumn(
                        gamertag = match.player2Gamertag,
                        teamAbbr = match.player2TeamAbbr,
                        score = match.player2Score,
                        isHome = false,
                        onAddPoints = { pts -> onAddPointsP2(match, pts) },
                        onTurnover = { onTurnoverP2(match) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quarter & Game Flow Controllers
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Quarter: ${if (match.currentQuarter > 4) "OVERTIME" else "Q${match.currentQuarter}"}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${match.quarterMinutes} min quarters • ${match.difficulty}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                            )
                        }

                        Button(
                            onClick = { onAdvanceQuarter(match) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceElevated,
                                contentColor = CyanAccent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (match.currentQuarter >= 4) "Enter OT" else "Next Quarter",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Side Bet / Prop Status Card
        if (match.sideBetDescription.isNotBlank()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = CyanAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "ACTIVE PROP / SIDE BET",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanAccent,
                                        fontWeight = FontWeight.Black
                                    )
                                )
                                Text(
                                    text = match.sideBetDescription,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                        Text(
                            text = "+$${String.format("%.0f", match.sideBetAmount)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = CyanAccent
                            )
                        )
                    }
                }
            }
        }

        // Complete & Submit Result Action
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { onSubmitFinalScore(match) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurfGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_final_score_cta")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SUBMIT FINAL SCORE & RELEASE POT",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onOpenDispute(match) },
                    border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FieryRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Report Dispute / EA Server Crash", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun JumbotronScoreboard(
    match: MaddenMatch,
    team1: com.example.data.model.NFLTeam,
    team2: com.example.data.model.NFLTeam,
    currentUserGamertag: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderHighlight),
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Bar: Match Code & Game Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MADDEN 27 CASH SHOWDOWN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = ElectricGold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = match.matchCode,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Jumbotron Center Board
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(SurfaceElevated, SurfaceVariantDark)
                        )
                    )
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team 1
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamBadge(teamAbbreviation = match.player1TeamAbbr)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = match.player1Gamertag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (match.player1Gamertag.equals(currentUserGamertag, ignoreCase = true)) ElectricGold else TextPrimaryDark
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "${match.p1Yards} yds • ${match.p1Turnovers} TO",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMutedDark, fontSize = 9.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = match.player1Score.toString(),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                }

                // Middle Clock & Quarter
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0x33EF4444),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed)
                    ) {
                        Text(
                            text = if (match.currentQuarter > 4) "OT" else "Q${match.currentQuarter}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = FieryRed
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = match.timeRemaining,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = ElectricGold
                        )
                    )
                    Text(
                        text = "LIVE CLOCK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMutedDark,
                            fontSize = 8.sp,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // Team 2
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamBadge(teamAbbreviation = match.player2TeamAbbr)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = match.player2Gamertag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (match.player2Gamertag.equals(currentUserGamertag, ignoreCase = true)) ElectricGold else TextPrimaryDark
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "${match.p2Yards} yds • ${match.p2Turnovers} TO",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMutedDark, fontSize = 9.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = match.player2Score.toString(),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ScorePadColumn(
    gamertag: String,
    teamAbbr: String,
    score: Int,
    isHome: Boolean,
    onAddPoints: (Int) -> Unit,
    onTurnover: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isHome) "HOME" else "AWAY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = TextMutedDark,
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = gamertag,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Touchdown +6
            Button(
                onClick = { onAddPoints(6) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricGold,
                    contentColor = Color(0xFF1E1100)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Text("+6 TOUCHDOWN", fontWeight = FontWeight.Black, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Extra point row: +1 XP / +2 2PT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = { onAddPoints(1) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1 XP", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { onAddPoints(2) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+2 2PT", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Field goal (+3) & Safety (+2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = { onAddPoints(3) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TurfGreen),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+3 FG", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { onTurnover() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FieryRed),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1 TO", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Undo -1 pt
            OutlinedButton(
                onClick = { onAddPoints(-1) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMutedDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                Text("-1 Undo Point", fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun EmptyLiveGameView(
    onCreateChallengeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceDark,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.SportsFootball,
                contentDescription = null,
                tint = ElectricGold,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Live Match Selected",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            )
            Text(
                text = "Select an active duel from the Arena or create a new Madden 27 cash match to start tracking live scores.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCreateChallengeClick,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricGold)
            ) {
                Text("Create Cash Match ($25)", color = Color(0xFF1E1100), fontWeight = FontWeight.Bold)
            }
        }
    }
}
