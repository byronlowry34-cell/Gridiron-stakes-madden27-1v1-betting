package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MaddenMatch
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerProfile
import com.example.data.model.WagerTransaction

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    val matchCode: String,
    val player1Gamertag: String,
    val player1Platform: String,
    val player1TeamAbbr: String,
    val player1Score: Int,
    val player2Gamertag: String,
    val player2Platform: String,
    val player2TeamAbbr: String,
    val player2Score: Int,
    val stakeAmount: Double,
    val totalPot: Double,
    val status: String,
    val quarterMinutes: Int,
    val difficulty: String,
    val gameStyle: String,
    val customPlaybooksAllowed: Boolean,
    val winnerGamertag: String?,
    val p1Confirmed: Boolean,
    val p2Confirmed: Boolean,
    val disputeReason: String?,
    val isOvertime: Boolean,
    val currentQuarter: Int,
    val timeRemaining: String,
    val p1Yards: Int,
    val p2Yards: Int,
    val p1Turnovers: Int,
    val p2Turnovers: Int,
    val createdAt: Long,
    val completedAt: Long?,
    val notes: String,
    val sideBetDescription: String,
    val sideBetAmount: Double,
    val sideBetWinner: String?
) {
    fun toDomain(): MaddenMatch = MaddenMatch(
        id = id,
        matchCode = matchCode,
        player1Gamertag = player1Gamertag,
        player1Platform = player1Platform,
        player1TeamAbbr = player1TeamAbbr,
        player1Score = player1Score,
        player2Gamertag = player2Gamertag,
        player2Platform = player2Platform,
        player2TeamAbbr = player2TeamAbbr,
        player2Score = player2Score,
        stakeAmount = stakeAmount,
        totalPot = totalPot,
        status = runCatching { MatchStatus.valueOf(status) }.getOrDefault(MatchStatus.OPEN),
        quarterMinutes = quarterMinutes,
        difficulty = difficulty,
        gameStyle = gameStyle,
        customPlaybooksAllowed = customPlaybooksAllowed,
        winnerGamertag = winnerGamertag,
        p1Confirmed = p1Confirmed,
        p2Confirmed = p2Confirmed,
        disputeReason = disputeReason,
        isOvertime = isOvertime,
        currentQuarter = currentQuarter,
        timeRemaining = timeRemaining,
        p1Yards = p1Yards,
        p2Yards = p2Yards,
        p1Turnovers = p1Turnovers,
        p2Turnovers = p2Turnovers,
        createdAt = createdAt,
        completedAt = completedAt,
        notes = notes,
        sideBetDescription = sideBetDescription,
        sideBetAmount = sideBetAmount,
        sideBetWinner = sideBetWinner
    )

    companion object {
        fun fromDomain(match: MaddenMatch): MatchEntity = MatchEntity(
            id = match.id,
            matchCode = match.matchCode,
            player1Gamertag = match.player1Gamertag,
            player1Platform = match.player1Platform,
            player1TeamAbbr = match.player1TeamAbbr,
            player1Score = match.player1Score,
            player2Gamertag = match.player2Gamertag,
            player2Platform = match.player2Platform,
            player2TeamAbbr = match.player2TeamAbbr,
            player2Score = match.player2Score,
            stakeAmount = match.stakeAmount,
            totalPot = match.totalPot,
            status = match.status.name,
            quarterMinutes = match.quarterMinutes,
            difficulty = match.difficulty,
            gameStyle = match.gameStyle,
            customPlaybooksAllowed = match.customPlaybooksAllowed,
            winnerGamertag = match.winnerGamertag,
            p1Confirmed = match.p1Confirmed,
            p2Confirmed = match.p2Confirmed,
            disputeReason = match.disputeReason,
            isOvertime = match.isOvertime,
            currentQuarter = match.currentQuarter,
            timeRemaining = match.timeRemaining,
            p1Yards = match.p1Yards,
            p2Yards = match.p2Yards,
            p1Turnovers = match.p1Turnovers,
            p2Turnovers = match.p2Turnovers,
            createdAt = match.createdAt,
            completedAt = match.completedAt,
            notes = match.notes,
            sideBetDescription = match.sideBetDescription,
            sideBetAmount = match.sideBetAmount,
            sideBetWinner = match.sideBetWinner
        )
    }
}

@Entity(tableName = "profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: String,
    val gamertag: String,
    val platform: String,
    val favoriteTeamAbbr: String,
    val balance: Double,
    val wins: Int,
    val losses: Int,
    val winStreak: Int,
    val totalEarnings: Double,
    val rankTier: String
) {
    fun toDomain(): PlayerProfile = PlayerProfile(
        id = id,
        gamertag = gamertag,
        platform = platform,
        favoriteTeamAbbr = favoriteTeamAbbr,
        balance = balance,
        wins = wins,
        losses = losses,
        winStreak = winStreak,
        totalEarnings = totalEarnings,
        rankTier = rankTier
    )

    companion object {
        fun fromDomain(p: PlayerProfile): PlayerProfileEntity = PlayerProfileEntity(
            id = p.id,
            gamertag = p.gamertag,
            platform = p.platform,
            favoriteTeamAbbr = p.favoriteTeamAbbr,
            balance = p.balance,
            wins = p.wins,
            losses = p.losses,
            winStreak = p.winStreak,
            totalEarnings = p.totalEarnings,
            rankTier = p.rankTier
        )
    }
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val matchId: String?,
    val type: String,
    val amount: Double,
    val description: String,
    val timestamp: Long
) {
    fun toDomain(): WagerTransaction = WagerTransaction(
        id = id,
        matchId = matchId,
        type = type,
        amount = amount,
        description = description,
        timestamp = timestamp
    )

    companion object {
        fun fromDomain(t: WagerTransaction): TransactionEntity = TransactionEntity(
            id = t.id,
            matchId = t.matchId,
            type = t.type,
            amount = t.amount,
            description = t.description,
            timestamp = t.timestamp
        )
    }
}
