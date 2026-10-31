package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GridironDatabase
import com.example.data.model.MaddenMatch
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerProfile
import com.example.data.model.WagerTransaction
import com.example.data.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppTab {
    ARENA,
    LIVE_GAME,
    RULES,
    RIVALS,
    LEDGER
}

data class ArenaUiState(
    val currentTab: AppTab = AppTab.ARENA,
    val selectedMatchId: String? = null,
    val filterStatus: String = "ALL", // "ALL", "OPEN", "ACTIVE", "COMPLETED"
    val showCreateDialog: Boolean = false,
    val showSubmitScoreDialog: Boolean = false,
    val showDepositDialog: Boolean = false,
    val showJoinCodeDialog: Boolean = false,
    val showDisputeDialog: Boolean = false,
    val activeMatchToResolve: MaddenMatch? = null,
    val userNotification: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = GridironDatabase.getDatabase(application, viewModelScope)
    private val repository = MatchRepository(database)

    private val _uiState = MutableStateFlow(ArenaUiState())
    val uiState: StateFlow<ArenaUiState> = _uiState.asStateFlow()

    val profile: StateFlow<PlayerProfile> = repository.getProfile()
        .map { it ?: PlayerProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerProfile()
        )

    val allMatches: StateFlow<List<MaddenMatch>> = repository.getAllMatches()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeMatch: StateFlow<MaddenMatch?> = repository.getActiveMatch()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val transactions: StateFlow<List<WagerTransaction>> = repository.getTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Seed database if empty initially
            repository.seedDatabaseIfEmpty()
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun selectMatch(matchId: String?) {
        _uiState.value = _uiState.value.copy(selectedMatchId = matchId)
        if (matchId != null) {
            _uiState.value = _uiState.value.copy(currentTab = AppTab.LIVE_GAME)
        }
    }

    fun setFilterStatus(status: String) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
    }

    fun openCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true)
    }

    fun dismissCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = false)
    }

    fun openSubmitScoreDialog(match: MaddenMatch? = null) {
        _uiState.value = _uiState.value.copy(
            showSubmitScoreDialog = true,
            activeMatchToResolve = match ?: activeMatch.value
        )
    }

    fun dismissSubmitScoreDialog() {
        _uiState.value = _uiState.value.copy(showSubmitScoreDialog = false)
    }

    fun openDepositDialog() {
        _uiState.value = _uiState.value.copy(showDepositDialog = true)
    }

    fun dismissDepositDialog() {
        _uiState.value = _uiState.value.copy(showDepositDialog = false)
    }

    fun openJoinCodeDialog() {
        _uiState.value = _uiState.value.copy(showJoinCodeDialog = false)
        _uiState.value = _uiState.value.copy(showJoinCodeDialog = true)
    }

    fun dismissJoinCodeDialog() {
        _uiState.value = _uiState.value.copy(showJoinCodeDialog = false)
    }

    fun openDisputeDialog(match: MaddenMatch) {
        _uiState.value = _uiState.value.copy(
            showDisputeDialog = true,
            activeMatchToResolve = match
        )
    }

    fun dismissDisputeDialog() {
        _uiState.value = _uiState.value.copy(showDisputeDialog = false)
    }

    fun dismissNotification() {
        _uiState.value = _uiState.value.copy(userNotification = null)
    }

    fun showMessage(msg: String) {
        _uiState.value = _uiState.value.copy(userNotification = msg)
    }

    fun createMatch(
        opponentGamertag: String,
        opponentPlatform: String,
        myTeamAbbr: String,
        opponentTeamAbbr: String,
        stakeAmount: Double,
        quarterMinutes: Int,
        difficulty: String,
        gameStyle: String,
        customPlaybooks: Boolean,
        sideBetText: String = "",
        sideBetStake: Double = 0.0
    ) {
        viewModelScope.launch {
            val user = profile.value
            val randomSuffix = (1000..9999).random()
            val newMatch = MaddenMatch(
                id = UUID.randomUUID().toString(),
                matchCode = "MD-$randomSuffix",
                player1Gamertag = user.gamertag,
                player1Platform = user.platform,
                player1TeamAbbr = myTeamAbbr,
                player1Score = 0,
                player2Gamertag = if (opponentGamertag.isBlank()) "Open Challenge (Lobby)" else opponentGamertag,
                player2Platform = opponentPlatform,
                player2TeamAbbr = opponentTeamAbbr,
                player2Score = 0,
                stakeAmount = stakeAmount,
                totalPot = stakeAmount * 2,
                status = if (opponentGamertag.isBlank()) MatchStatus.OPEN else MatchStatus.ESCROW_LOCKED,
                quarterMinutes = quarterMinutes,
                difficulty = difficulty,
                gameStyle = gameStyle,
                customPlaybooksAllowed = customPlaybooks,
                sideBetDescription = sideBetText,
                sideBetAmount = sideBetStake,
                notes = "Challenge created. Stake: $${String.format("%.2f", stakeAmount)} locked in escrow."
            )

            repository.createMatch(newMatch)
            _uiState.value = _uiState.value.copy(
                showCreateDialog = false,
                selectedMatchId = newMatch.id,
                currentTab = AppTab.LIVE_GAME,
                userNotification = "Match challenge created! $${String.format("%.2f", stakeAmount)} locked into escrow."
            )
        }
    }

    fun joinMatch(matchId: String, selectedTeamAbbr: String) {
        viewModelScope.launch {
            val user = profile.value
            repository.joinOpenChallenge(matchId, user.gamertag, selectedTeamAbbr)
            _uiState.value = _uiState.value.copy(
                selectedMatchId = matchId,
                currentTab = AppTab.LIVE_GAME,
                userNotification = "Joined challenge! Escrow locked. Ready to kick off!"
            )
        }
    }

    fun joinByCode(code: String, selectedTeamAbbr: String) {
        viewModelScope.launch {
            val matches = allMatches.value
            val match = matches.find { it.matchCode.equals(code.trim(), ignoreCase = true) }
            if (match != null) {
                val user = profile.value
                repository.joinOpenChallenge(match.id, user.gamertag, selectedTeamAbbr)
                _uiState.value = _uiState.value.copy(
                    showJoinCodeDialog = false,
                    selectedMatchId = match.id,
                    currentTab = AppTab.LIVE_GAME,
                    userNotification = "Found match $code! Stake locked in escrow."
                )
            } else {
                showMessage("No active challenge found for code $code.")
            }
        }
    }

    fun startLiveMatch(matchId: String) {
        viewModelScope.launch {
            repository.startMatch(matchId)
            showMessage("Madden 27 Game is LIVE! Track score quarter by quarter.")
        }
    }

    fun addPointsToP1(match: MaddenMatch, points: Int) {
        viewModelScope.launch {
            val newScore = (match.player1Score + points).coerceAtLeast(0)
            repository.updateLiveScore(
                matchId = match.id,
                p1Score = newScore,
                p2Score = match.player2Score,
                quarter = match.currentQuarter,
                timeRemaining = match.timeRemaining,
                p1Yards = match.p1Yards + if (points == 6) 65 else 25,
                p2Yards = match.p2Yards,
                p1Turnovers = match.p1Turnovers,
                p2Turnovers = match.p2Turnovers
            )
        }
    }

    fun addPointsToP2(match: MaddenMatch, points: Int) {
        viewModelScope.launch {
            val newScore = (match.player2Score + points).coerceAtLeast(0)
            repository.updateLiveScore(
                matchId = match.id,
                p1Score = match.player1Score,
                p2Score = newScore,
                quarter = match.currentQuarter,
                timeRemaining = match.timeRemaining,
                p1Yards = match.p1Yards,
                p2Yards = match.p2Yards + if (points == 6) 65 else 25,
                p1Turnovers = match.p1Turnovers,
                p2Turnovers = match.p2Turnovers
            )
        }
    }

    fun advanceQuarter(match: MaddenMatch) {
        viewModelScope.launch {
            val nextQ = (match.currentQuarter + 1).coerceAtMost(5)
            val time = if (nextQ == 5) "OT 5:00" else "${match.quarterMinutes}:00"
            repository.updateLiveScore(
                matchId = match.id,
                p1Score = match.player1Score,
                p2Score = match.player2Score,
                quarter = nextQ,
                timeRemaining = time,
                p1Yards = match.p1Yards,
                p2Yards = match.p2Yards,
                p1Turnovers = match.p1Turnovers,
                p2Turnovers = match.p2Turnovers
            )
        }
    }

    fun addTurnoverP1(match: MaddenMatch) {
        viewModelScope.launch {
            repository.updateLiveScore(
                matchId = match.id,
                p1Score = match.player1Score,
                p2Score = match.player2Score,
                quarter = match.currentQuarter,
                timeRemaining = match.timeRemaining,
                p1Yards = match.p1Yards,
                p2Yards = match.p2Yards,
                p1Turnovers = match.p1Turnovers + 1,
                p2Turnovers = match.p2Turnovers
            )
            showMessage("Turnover recorded against ${match.player1Gamertag}!")
        }
    }

    fun addTurnoverP2(match: MaddenMatch) {
        viewModelScope.launch {
            repository.updateLiveScore(
                matchId = match.id,
                p1Score = match.player1Score,
                p2Score = match.player2Score,
                quarter = match.currentQuarter,
                timeRemaining = match.timeRemaining,
                p1Yards = match.p1Yards,
                p2Yards = match.p2Yards,
                p1Turnovers = match.p1Turnovers,
                p2Turnovers = match.p2Turnovers + 1
            )
            showMessage("Turnover recorded against ${match.player2Gamertag}!")
        }
    }

    fun submitFinalResult(
        matchId: String,
        finalP1Score: Int,
        finalP2Score: Int,
        proofNote: String = "Screenshot verified"
    ) {
        viewModelScope.launch {
            val myTag = profile.value.gamertag
            repository.submitFinalResult(matchId, finalP1Score, finalP2Score, myTag, proofNote)
            _uiState.value = _uiState.value.copy(
                showSubmitScoreDialog = false,
                userNotification = "Scores submitted! Result verified and pot escrow distributed."
            )
        }
    }

    fun resolveDispute(
        matchId: String,
        winnerGamertag: String,
        p1Score: Int,
        p2Score: Int,
        rulingNotes: String
    ) {
        viewModelScope.launch {
            repository.resolveDispute(matchId, winnerGamertag, p1Score, p2Score, rulingNotes)
            _uiState.value = _uiState.value.copy(
                showDisputeDialog = false,
                userNotification = "Dispute resolved in favor of $winnerGamertag! Payout released."
            )
        }
    }

    fun depositFunds(amount: Double) {
        viewModelScope.launch {
            repository.depositFunds(amount)
            _uiState.value = _uiState.value.copy(
                showDepositDialog = false,
                userNotification = "Added $${String.format("%.2f", amount)} match credits to wallet."
            )
        }
    }

    fun updateGamertag(newGamertag: String, newPlatform: String, newTeam: String) {
        viewModelScope.launch {
            val current = profile.value
            repository.updateProfile(
                current.copy(
                    gamertag = newGamertag.ifBlank { current.gamertag },
                    platform = newPlatform,
                    favoriteTeamAbbr = newTeam
                )
            )
            showMessage("Player profile updated!")
        }
    }
}
