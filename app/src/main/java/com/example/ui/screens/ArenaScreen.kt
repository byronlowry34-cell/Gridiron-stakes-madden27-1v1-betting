package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MaddenMatch
import com.example.data.model.MatchStatus
import com.example.ui.components.MatchCard
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

@Composable
fun ArenaScreen(
    matches: List<MaddenMatch>,
    currentUserGamertag: String,
    filterStatus: String,
    onFilterChange: (String) -> Unit,
    onCreateChallengeClick: () -> Unit,
    onJoinByCodeClick: () -> Unit,
    onSelectMatch: (String) -> Unit,
    onJoinMatch: (String) -> Unit,
    onSubmitResult: (MaddenMatch) -> Unit,
    onDisputeClick: (MaddenMatch) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredMatches = remember(matches, filterStatus) {
        when (filterStatus) {
            "OPEN" -> matches.filter { it.status == MatchStatus.OPEN }
            "ACTIVE" -> matches.filter { it.status == MatchStatus.IN_PROGRESS || it.status == MatchStatus.ESCROW_LOCKED }
            "COMPLETED" -> matches.filter { it.status == MatchStatus.COMPLETED }
            else -> matches
        }
    }

    val totalEscrowActive = remember(matches) {
        matches.filter { it.status == MatchStatus.IN_PROGRESS || it.status == MatchStatus.ESCROW_LOCKED }
            .sumOf { it.totalPot }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("arena_matches_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Stadium Card
        item {
            StadiumHeroBanner(
                totalEscrowActive = totalEscrowActive,
                onCreateChallengeClick = onCreateChallengeClick,
                onJoinByCodeClick = onJoinByCodeClick
            )
        }

        // Filter Chips Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    "ALL" to "All Duels",
                    "ACTIVE" to "Live in Play",
                    "OPEN" to "Open Lobbies",
                    "COMPLETED" to "History"
                ).forEach { (key, label) ->
                    FilterChip(
                        selected = filterStatus == key,
                        onClick = { onFilterChange(key) },
                        label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricGold.copy(alpha = 0.2f),
                            selectedLabelColor = ElectricGold,
                            containerColor = SurfaceVariantDark,
                            labelColor = TextSecondaryDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterStatus == key,
                            selectedBorderColor = ElectricGold,
                            borderColor = BorderLight
                        )
                    )
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
                    text = if (filterStatus == "ALL") "CURRENT DUELS & LOBBIES" else "$filterStatus DUELS",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "${filteredMatches.size} Matches",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                )
            }
        }

        // Matches list
        if (filteredMatches.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceVariantDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsFootball,
                            contentDescription = null,
                            tint = TextMutedDark,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Matches Found",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                        Text(
                            text = "Be the first to post a Madden 27 cash challenge!",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCreateChallengeClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricGold)
                        ) {
                            Text("Create Challenge Now", color = Color(0xFF1E1100), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredMatches, key = { it.id }) { match ->
                MatchCard(
                    match = match,
                    currentUserGamertag = currentUserGamertag,
                    onSelectMatch = { onSelectMatch(match.id) },
                    onJoinMatch = { onJoinMatch(match.id) },
                    onSubmitResult = { onSubmitResult(match) },
                    onDisputeClick = { onDisputeClick(match) }
                )
            }
        }
    }
}

@Composable
fun StadiumHeroBanner(
    totalEscrowActive: Double,
    onCreateChallengeClick: () -> Unit,
    onJoinByCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Stadium Image background
            Image(
                painter = painterResource(id = R.drawable.madden_stadium_banner),
                contentDescription = "Stadium Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Gradient Scrim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0x660B101D),
                                Color(0xCC0B101D),
                                Color(0xFF0B101D)
                            )
                        )
                    )
            )

            // Content inside hero
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
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x6600E5FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyanAccent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MADDEN 27 PRO LEAGUE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = CyanAccent,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    // Active Escrow Ticker
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x6610B981),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TurfGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TurfGreen, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$${String.format("%.0f", totalEscrowActive)} In Escrow",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TurfGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "PLAY 1v1 FOR CASH & REPUTATION",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                )

                Text(
                    text = "Lock stakes in escrow, dominate on the virtual gridiron, verify final scores, and claim the winner's pot instantly.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        lineHeight = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons inside banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onCreateChallengeClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricGold,
                            contentColor = Color(0xFF1E1100)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .testTag("hero_create_match_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create 1v1 Challenge", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = onJoinByCodeClick,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("hero_join_code_btn")
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Enter Code", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
