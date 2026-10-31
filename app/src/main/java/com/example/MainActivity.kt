package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.components.AppHeader
import com.example.ui.components.CreateChallengeDialog
import com.example.ui.components.DepositDialog
import com.example.ui.components.DisputeDialog
import com.example.ui.components.JoinCodeDialog
import com.example.ui.components.NotificationBanner
import com.example.ui.components.SubmitScoreDialog
import com.example.ui.screens.ArenaScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.screens.LiveGameScreen
import com.example.ui.screens.RivalsScreen
import com.example.ui.screens.RulesScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextPrimaryDark

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val allMatches by viewModel.allMatches.collectAsStateWithLifecycle()
    val activeMatch by viewModel.activeMatch.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    // Handle Android system back gesture to return to Arena if not already on Arena
    BackHandler(enabled = uiState.currentTab != AppTab.ARENA) {
        viewModel.selectTab(AppTab.ARENA)
    }

    val selectedMatch = remember(uiState.selectedMatchId, allMatches, activeMatch) {
        if (uiState.selectedMatchId != null) {
            allMatches.find { it.id == uiState.selectedMatchId } ?: activeMatch
        } else {
            activeMatch ?: allMatches.firstOrNull()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        containerColor = BackgroundDark,
        topBar = {
            Column(
                modifier = Modifier
                    .background(SurfaceDark)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                AppHeader(
                    profile = profile,
                    onAddFundsClick = { viewModel.openDepositDialog() }
                )
                NotificationBanner(
                    message = uiState.userNotification,
                    onDismiss = { viewModel.dismissNotification() }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.ARENA,
                    onClick = { viewModel.selectTab(AppTab.ARENA) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsFootball,
                            contentDescription = "Arena"
                        )
                    },
                    label = { Text("Arena", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E1100),
                        selectedTextColor = ElectricGold,
                        indicatorColor = ElectricGold,
                        unselectedIconColor = TextMutedDark,
                        unselectedTextColor = TextMutedDark
                    ),
                    modifier = Modifier.testTag("nav_item_arena")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.LIVE_GAME,
                    onClick = { viewModel.selectTab(AppTab.LIVE_GAME) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsScore,
                            contentDescription = "Live Game"
                        )
                    },
                    label = { Text("Scorekeeper", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E1100),
                        selectedTextColor = ElectricGold,
                        indicatorColor = ElectricGold,
                        unselectedIconColor = TextMutedDark,
                        unselectedTextColor = TextMutedDark
                    ),
                    modifier = Modifier.testTag("nav_item_live_game")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.RULES,
                    onClick = { viewModel.selectTab(AppTab.RULES) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Rules"
                        )
                    },
                    label = { Text("Rules", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E1100),
                        selectedTextColor = ElectricGold,
                        indicatorColor = ElectricGold,
                        unselectedIconColor = TextMutedDark,
                        unselectedTextColor = TextMutedDark
                    ),
                    modifier = Modifier.testTag("nav_item_rules")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.RIVALS,
                    onClick = { viewModel.selectTab(AppTab.RIVALS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Rivals"
                        )
                    },
                    label = { Text("Rivals", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E1100),
                        selectedTextColor = ElectricGold,
                        indicatorColor = ElectricGold,
                        unselectedIconColor = TextMutedDark,
                        unselectedTextColor = TextMutedDark
                    ),
                    modifier = Modifier.testTag("nav_item_rivals")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.LEDGER,
                    onClick = { viewModel.selectTab(AppTab.LEDGER) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "Ledger"
                        )
                    },
                    label = { Text("Vault", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E1100),
                        selectedTextColor = ElectricGold,
                        indicatorColor = ElectricGold,
                        unselectedIconColor = TextMutedDark,
                        unselectedTextColor = TextMutedDark
                    ),
                    modifier = Modifier.testTag("nav_item_ledger")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppTab.ARENA -> {
                    ArenaScreen(
                        matches = allMatches,
                        currentUserGamertag = profile.gamertag,
                        filterStatus = uiState.filterStatus,
                        onFilterChange = { viewModel.setFilterStatus(it) },
                        onCreateChallengeClick = { viewModel.openCreateDialog() },
                        onJoinByCodeClick = { viewModel.openJoinCodeDialog() },
                        onSelectMatch = { matchId -> viewModel.selectMatch(matchId) },
                        onJoinMatch = { matchId -> viewModel.joinMatch(matchId, profile.favoriteTeamAbbr) },
                        onSubmitResult = { match -> viewModel.openSubmitScoreDialog(match) },
                        onDisputeClick = { match -> viewModel.openDisputeDialog(match) }
                    )
                }

                AppTab.LIVE_GAME -> {
                    LiveGameScreen(
                        match = selectedMatch,
                        currentUserGamertag = profile.gamertag,
                        onStartMatch = { matchId -> viewModel.startLiveMatch(matchId) },
                        onAddPointsP1 = { match, pts -> viewModel.addPointsToP1(match, pts) },
                        onAddPointsP2 = { match, pts -> viewModel.addPointsToP2(match, pts) },
                        onAdvanceQuarter = { match -> viewModel.advanceQuarter(match) },
                        onTurnoverP1 = { match -> viewModel.addTurnoverP1(match) },
                        onTurnoverP2 = { match -> viewModel.addTurnoverP2(match) },
                        onSubmitFinalScore = { match -> viewModel.openSubmitScoreDialog(match) },
                        onOpenDispute = { match -> viewModel.openDisputeDialog(match) },
                        onCreateChallengeClick = { viewModel.openCreateDialog() }
                    )
                }

                AppTab.RULES -> {
                    RulesScreen()
                }

                AppTab.RIVALS -> {
                    RivalsScreen(
                        profile = profile,
                        onChallengeRival = { rivalTag, platform, oppTeam ->
                            viewModel.createMatch(
                                opponentGamertag = rivalTag,
                                opponentPlatform = platform,
                                myTeamAbbr = profile.favoriteTeamAbbr,
                                opponentTeamAbbr = oppTeam,
                                stakeAmount = 25.0,
                                quarterMinutes = 5,
                                difficulty = "All-Madden",
                                gameStyle = "Competitive",
                                customPlaybooks = true
                            )
                        }
                    )
                }

                AppTab.LEDGER -> {
                    LedgerScreen(
                        profile = profile,
                        transactions = transactions,
                        onAddFundsClick = { viewModel.openDepositDialog() }
                    )
                }
            }
        }
    }

    // Modal Dialogs
    if (uiState.showCreateDialog) {
        CreateChallengeDialog(
            userWalletBalance = profile.balance,
            myFavoriteTeam = profile.favoriteTeamAbbr,
            onDismiss = { viewModel.dismissCreateDialog() },
            onCreateChallenge = { oppTag, oppPlat, myT, oppT, stake, qMins, diff, style, customP, sideText, sideStake ->
                viewModel.createMatch(
                    opponentGamertag = oppTag,
                    opponentPlatform = oppPlat,
                    myTeamAbbr = myT,
                    opponentTeamAbbr = oppT,
                    stakeAmount = stake,
                    quarterMinutes = qMins,
                    difficulty = diff,
                    gameStyle = style,
                    customPlaybooks = customP,
                    sideBetText = sideText,
                    sideBetStake = sideStake
                )
            }
        )
    }

    if (uiState.showSubmitScoreDialog && uiState.activeMatchToResolve != null) {
        SubmitScoreDialog(
            match = uiState.activeMatchToResolve!!,
            currentUserGamertag = profile.gamertag,
            onDismiss = { viewModel.dismissSubmitScoreDialog() },
            onSubmitScore = { matchId, p1Score, p2Score, proofNote ->
                viewModel.submitFinalResult(matchId, p1Score, p2Score, proofNote)
            }
        )
    }

    if (uiState.showDepositDialog) {
        DepositDialog(
            currentBalance = profile.balance,
            onDismiss = { viewModel.dismissDepositDialog() },
            onDeposit = { amount -> viewModel.depositFunds(amount) }
        )
    }

    if (uiState.showJoinCodeDialog) {
        JoinCodeDialog(
            onDismiss = { viewModel.dismissJoinCodeDialog() },
            onJoin = { code, team -> viewModel.joinByCode(code, team) }
        )
    }

    if (uiState.showDisputeDialog && uiState.activeMatchToResolve != null) {
        DisputeDialog(
            match = uiState.activeMatchToResolve!!,
            onDismiss = { viewModel.dismissDisputeDialog() },
            onResolve = { winner, p1Score, p2Score, notes ->
                viewModel.resolveDispute(uiState.activeMatchToResolve!!.id, winner, p1Score, p2Score, notes)
            }
        )
    }
}
