package com.example.data.model

enum class MatchStatus {
    OPEN,               // Waiting for Player 2 to accept and match stake
    ESCROW_LOCKED,      // Both players locked stake into escrow, ready to play
    IN_PROGRESS,        // Game underway in Madden 27
    PENDING_CONFIRM,    // Scores submitted, awaiting confirmation / proof check
    COMPLETED,          // Winner confirmed, payout settled
    DISPUTED            // Scores mismatch, flagged for dispute review
}

data class MaddenMatch(
    val id: String,
    val matchCode: String,
    val player1Gamertag: String,
    val player1Platform: String,
    val player1TeamAbbr: String,
    val player1Score: Int = 0,
    val player2Gamertag: String,
    val player2Platform: String,
    val player2TeamAbbr: String,
    val player2Score: Int = 0,
    val stakeAmount: Double,
    val totalPot: Double = stakeAmount * 2,
    val status: MatchStatus = MatchStatus.OPEN,
    val quarterMinutes: Int = 5,
    val difficulty: String = "All-Madden",
    val gameStyle: String = "Competitive",
    val customPlaybooksAllowed: Boolean = true,
    val winnerGamertag: String? = null,
    val p1Confirmed: Boolean = false,
    val p2Confirmed: Boolean = false,
    val disputeReason: String? = null,
    val isOvertime: Boolean = false,
    val currentQuarter: Int = 1, // 1 to 4, 5 for OT
    val timeRemaining: String = "5:00",
    val p1Yards: Int = 0,
    val p2Yards: Int = 0,
    val p1Turnovers: Int = 0,
    val p2Turnovers: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val notes: String = "",
    val sideBetDescription: String = "",
    val sideBetAmount: Double = 0.0,
    val sideBetWinner: String? = null
)

data class PlayerProfile(
    val id: String = "current_user",
    val gamertag: String = "GridironKing27",
    val platform: String = "PlayStation 5",
    val favoriteTeamAbbr: String = "KC",
    val balance: Double = 250.00,
    val wins: Int = 18,
    val losses: Int = 6,
    val winStreak: Int = 4,
    val totalEarnings: Double = 680.00,
    val rankTier: String = "All-Madden Legend"
)

data class WagerTransaction(
    val id: String,
    val matchId: String?,
    val type: String, // "MATCH_WIN", "ESCROW_LOCK", "ESCROW_REFUND", "SIDE_BET_WIN", "DEPOSIT"
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CompetitiveRule(
    val title: String,
    val category: String, // "General", "Fourth Down", "Special Teams", "Disconnect"
    val ruleText: String,
    val penalty: String
)
