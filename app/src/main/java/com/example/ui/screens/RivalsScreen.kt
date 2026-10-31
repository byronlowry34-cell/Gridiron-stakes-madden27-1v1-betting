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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideogameAsset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerProfile
import com.example.ui.components.TeamBadge
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.GoldDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TurfGreen

data class RivalRecord(
    val gamertag: String,
    val platform: String,
    val favoriteTeamAbbr: String,
    val winsAgainst: Int,
    val lossesAgainst: Int,
    val netCashWon: Double
)

data class LeaderboardPlayer(
    val rank: Int,
    val gamertag: String,
    val tier: String,
    val totalEarnings: Double,
    val winRate: String
)

@Composable
fun RivalsScreen(
    profile: PlayerProfile,
    onChallengeRival: (gamertag: String, platform: String, teamAbbr: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rivals = listOf(
        RivalRecord("ViperClutch", "PlayStation 5", "BAL", 4, 2, 85.00),
        RivalRecord("BlitzGod_TX", "PC EA App", "DAL", 3, 1, 60.00),
        RivalRecord("CincySlinger", "Xbox Series X", "CIN", 2, 0, 40.00),
        RivalRecord("DetroitDimeDropper", "Xbox Series X", "DET", 1, 1, 0.00),
        RivalRecord("BayArea49", "PlayStation 5", "SF", 0, 2, -50.00)
    )

    val leaderboard = listOf(
        LeaderboardPlayer(1, "LowryElite", "All-Madden Legend", 1420.00, "88%"),
        LeaderboardPlayer(2, "GridironKing27 (You)", "All-Madden Legend", 740.00, "75%"),
        LeaderboardPlayer(3, "ViperClutch", "Veteran", 620.00, "68%"),
        LeaderboardPlayer(4, "RedZoneReaper", "Veteran", 580.00, "64%"),
        LeaderboardPlayer(5, "BlitzGod_TX", "Pro", 490.00, "60%")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("rivals_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Player Profile Esports Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ElectricGold, GoldDark))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsFootball,
                                    contentDescription = null,
                                    tint = Color(0xFF1E1100),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = profile.gamertag,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "${profile.platform} • Favorite: ${profile.favoriteTeamAbbr}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x33FFB703),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricGold)
                        ) {
                            Text(
                                text = profile.rankTier,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricGold,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Win / Loss", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "${profile.wins}W - ${profile.losses}L",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TurfGreen
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Win Rate", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            val total = profile.wins + profile.losses
                            val winRate = if (total > 0) (profile.wins * 100 / total) else 0
                            Text(
                                "$winRate%",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricGold
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Won", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "$${String.format("%.0f", profile.totalEarnings)}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Head-to-Head Rivals
        item {
            Text(
                text = "HEAD-TO-HEAD RIVALRIES",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
        }

        // Rivals cards
        items(rivals) { rival ->
            Surface(
                shape = RoundedCornerShape(14.dp),
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
                        TeamBadge(teamAbbreviation = rival.favoriteTeamAbbr)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = rival.gamertag,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${rival.winsAgainst}W - ${rival.lossesAgainst}L vs You • ${rival.platform}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                            )
                            Text(
                                text = if (rival.netCashWon >= 0) "+$${String.format("%.2f", rival.netCashWon)} net won" else "-$${String.format("%.2f", -rival.netCashWon)} net lost",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (rival.netCashWon >= 0) TurfGreen else Color(0xFFEF4444),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Button(
                        onClick = { onChallengeRival(rival.gamertag, rival.platform, rival.favoriteTeamAbbr) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricGold,
                            contentColor = Color(0xFF1E1100)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Rematch ($25)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        // Section Title: Madden 27 Cash Leaderboard
        item {
            Text(
                text = "GLOBAL MONEY LEADERBOARD",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
        }

        // Leaderboard items
        items(leaderboard) { player ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (player.rank == 2) Color(0x22FFB703) else SurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (player.rank == 2) ElectricGold else BorderLight
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when (player.rank) {
                                        1 -> ElectricGold
                                        2 -> Color(0xFFC0C0C0)
                                        3 -> Color(0xFFCD7F32)
                                        else -> SurfaceVariantDark
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${player.rank}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (player.rank <= 3) Color(0xFF1E1100) else Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = player.gamertag,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (player.rank == 2) ElectricGold else Color.White
                                )
                            )
                            Text(
                                text = "${player.tier} • ${player.winRate} Win Rate",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                            )
                        }
                    }

                    Text(
                        text = "$${String.format("%.0f", player.totalEarnings)}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TurfGreen
                        )
                    )
                }
            }
        }
    }
}
