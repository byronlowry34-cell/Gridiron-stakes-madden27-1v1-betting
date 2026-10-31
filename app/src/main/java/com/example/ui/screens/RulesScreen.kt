package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompetitiveRule
import com.example.data.model.MaddenRulesData
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
fun RulesScreen(
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("ALL") }
    var calculatorStake by remember { mutableFloatStateOf(50f) }

    val categories = listOf("ALL", "Fourth Down", "Special Teams", "Disconnect", "General", "Verification")

    val filteredRules = remember(selectedCategory) {
        if (selectedCategory == "ALL") {
            MaddenRulesData.standardRules
        } else {
            MaddenRulesData.standardRules.filter { it.category == selectedCategory }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("rules_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stake & Payout Calculator
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
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
                            Icon(Icons.Default.Calculate, contentDescription = null, tint = ElectricGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "STAKE & POT CALCULATOR",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = ElectricGold
                                )
                            )
                        }
                        Text(
                            text = "0% Platform Rake",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TurfGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your Wager: $${calculatorStake.toInt()}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )

                    Slider(
                        value = calculatorStake,
                        onValueChange = { calculatorStake = it },
                        valueRange = 10f..250f,
                        steps = 23,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricGold,
                            activeTrackColor = ElectricGold,
                            inactiveTrackColor = SurfaceElevated
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Escrow Pot", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "$${(calculatorStake * 2).toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricGold
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Winner Net Profit", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark))
                            Text(
                                "+$${calculatorStake.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TurfGreen
                                )
                            )
                        }
                    }
                }
            }
        }

        // Rules Header & Filter
        item {
            Column {
                Text(
                    text = "MADDEN 27 FAIR PLAY RULES",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Official guidelines enforced for all 1v1 cash matches.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                                selectedLabelColor = CyanAccent
                            )
                        )
                    }
                }
            }
        }

        // Rule cards
        items(filteredRules) { rule ->
            RuleCard(rule = rule)
        }
    }
}

@Composable
fun RuleCard(rule: CompetitiveRule) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
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
                Text(
                    text = rule.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SurfaceVariantDark
                ) {
                    Text(
                        text = rule.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyanAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rule.ruleText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = FieryRed.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = FieryRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Violation: ${rule.penalty}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FieryRed,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
