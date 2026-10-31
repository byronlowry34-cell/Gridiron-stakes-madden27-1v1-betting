package com.example.data.repository

import com.example.data.local.GridironDatabase
import com.example.data.local.MatchEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.TransactionEntity
import com.example.data.model.MaddenMatch
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerProfile
import com.example.data.model.WagerTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID

class MatchRepository(private val database: GridironDatabase) {
    private val matchDao = database.matchDao()
    private val playerDao = database.playerDao()
    private val transactionDao = database.transactionDao()

    fun getAllMatches(): Flow<List<MaddenMatch>> {
        return matchDao.getAllMatches().map { list -> list.map { it.toDomain() } }
    }

    fun getActiveMatch(): Flow<MaddenMatch?> {
        return matchDao.getActiveMatch().map { it?.toDomain() }
    }

    fun getMatchById(id: String): Flow<MaddenMatch?> {
        return matchDao.getMatchById(id).map { it?.toDomain() }
    }

    fun getProfile(): Flow<PlayerProfile?> {
        return playerDao.getProfile().map { it?.toDomain() }
    }

    fun getTransactions(): Flow<List<WagerTransaction>> {
        return transactionDao.getAllTransactions().map { list -> list.map { it.toDomain() } }
    }

    suspend fun createMatch(match: MaddenMatch) {
        val entity = MatchEntity.fromDomain(match)
        matchDao.insertMatch(entity)

        // Lock stake from profile into escrow
        val currentProfile = playerDao.getProfile().firstOrNull()?.toDomain() ?: PlayerProfile()
        val newBalance = (currentProfile.balance - match.stakeAmount).coerceAtLeast(0.0)
        playerDao.updateProfile(
            PlayerProfileEntity.fromDomain(
                currentProfile.copy(balance = newBalance)
            )
        )

        // Record escrow lock transaction
        transactionDao.insertTransaction(
            TransactionEntity(
                id = UUID.randomUUID().toString(),
                matchId = match.id,
                type = "ESCROW_LOCK",
                amount = -match.stakeAmount,
                description = "Locked $${String.format("%.2f", match.stakeAmount)} in escrow for 1v1 vs ${match.player2Gamertag}",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateMatch(match: MaddenMatch) {
        matchDao.updateMatch(MatchEntity.fromDomain(match))
    }

    suspend fun joinOpenChallenge(matchId: String, player2Gamertag: String, teamAbbr: String) {
        val currentMatch = matchDao.getMatchById(matchId).firstOrNull()?.toDomain() ?: return
        val updated = currentMatch.copy(
            player2Gamertag = player2Gamertag,
            player2TeamAbbr = teamAbbr,
            status = MatchStatus.ESCROW_LOCKED
        )
        matchDao.updateMatch(MatchEntity.fromDomain(updated))
    }

    suspend fun startMatch(matchId: String) {
        val currentMatch = matchDao.getMatchById(matchId).firstOrNull()?.toDomain() ?: return
        val updated = currentMatch.copy(status = MatchStatus.IN_PROGRESS)
        matchDao.updateMatch(MatchEntity.fromDomain(updated))
    }

    suspend fun updateLiveScore(
        matchId: String,
        p1Score: Int,
        p2Score: Int,
        quarter: Int,
        timeRemaining: String,
        p1Yards: Int = 0,
        p2Yards: Int = 0,
        p1Turnovers: Int = 0,
        p2Turnovers: Int = 0
    ) {
        val currentMatch = matchDao.getMatchById(matchId).firstOrNull()?.toDomain() ?: return
        val updated = currentMatch.copy(
            player1Score = p1Score,
            player2Score = p2Score,
            currentQuarter = quarter,
            timeRemaining = timeRemaining,
            p1Yards = p1Yards,
            p2Yards = p2Yards,
            p1Turnovers = p1Turnovers,
            p2Turnovers = p2Turnovers
        )
        matchDao.updateMatch(MatchEntity.fromDomain(updated))
    }

    suspend fun submitFinalResult(
        matchId: String,
        finalP1Score: Int,
        finalP2Score: Int,
        submittingPlayerGamertag: String,
        proofNote: String = ""
    ) {
        val currentMatch = matchDao.getMatchById(matchId).firstOrNull()?.toDomain() ?: return
        val isP1 = submittingPlayerGamertag.equals(currentMatch.player1Gamertag, ignoreCase = true)

        val updatedP1Confirmed = if (isP1) true else currentMatch.p1Confirmed
        val updatedP2Confirmed = if (!isP1) true else currentMatch.p2Confirmed

        val winner = if (finalP1Score > finalP2Score) {
            currentMatch.player1Gamertag
        } else if (finalP2Score > finalP1Score) {
            currentMatch.player2Gamertag
        } else {
            null // Tie / Overtime
        }

        // Check if both confirmed
        val isSettled = updatedP1Confirmed && updatedP2Confirmed && winner != null

        val updatedMatch = currentMatch.copy(
            player1Score = finalP1Score,
            player2Score = finalP2Score,
            p1Confirmed = updatedP1Confirmed,
            p2Confirmed = updatedP2Confirmed,
            winnerGamertag = winner,
            status = if (isSettled) MatchStatus.COMPLETED else MatchStatus.PENDING_CONFIRM,
            completedAt = if (isSettled) System.currentTimeMillis() else currentMatch.completedAt,
            notes = if (proofNote.isNotBlank()) "${currentMatch.notes} | Proof: $proofNote" else currentMatch.notes
        )

        matchDao.updateMatch(MatchEntity.fromDomain(updatedMatch))

        if (isSettled && winner != null) {
            settleMatchPayout(updatedMatch, winner)
        }
    }

    suspend fun resolveDispute(
        matchId: String,
        winnerGamertag: String,
        p1Score: Int,
        p2Score: Int,
        rulingNotes: String
    ) {
        val currentMatch = matchDao.getMatchById(matchId).firstOrNull()?.toDomain() ?: return
        val updated = currentMatch.copy(
            status = MatchStatus.COMPLETED,
            winnerGamertag = winnerGamertag,
            player1Score = p1Score,
            player2Score = p2Score,
            p1Confirmed = true,
            p2Confirmed = true,
            disputeReason = null,
            completedAt = System.currentTimeMillis(),
            notes = "${currentMatch.notes} | Official Admin Ruling: $rulingNotes"
        )
        matchDao.updateMatch(MatchEntity.fromDomain(updated))
        settleMatchPayout(updated, winnerGamertag)
    }

    private suspend fun settleMatchPayout(match: MaddenMatch, winnerGamertag: String) {
        val currentProfile = playerDao.getProfile().firstOrNull()?.toDomain() ?: PlayerProfile()
        val isUserWinner = winnerGamertag.equals(currentProfile.gamertag, ignoreCase = true)

        if (isUserWinner) {
            val totalWon = match.totalPot + match.sideBetAmount
            val newBalance = currentProfile.balance + totalWon
            val newWins = currentProfile.wins + 1
            val newStreak = currentProfile.winStreak + 1
            val newEarnings = currentProfile.totalEarnings + (totalWon - match.stakeAmount)

            playerDao.updateProfile(
                PlayerProfileEntity.fromDomain(
                    currentProfile.copy(
                        balance = newBalance,
                        wins = newWins,
                        winStreak = newStreak,
                        totalEarnings = newEarnings
                    )
                )
            )

            // Payout ledger transaction
            transactionDao.insertTransaction(
                TransactionEntity(
                    id = UUID.randomUUID().toString(),
                    matchId = match.id,
                    type = "MATCH_WIN",
                    amount = totalWon,
                    description = "Claimed $${String.format("%.2f", totalWon)} 1v1 Pot Payout vs ${match.player2Gamertag}",
                    timestamp = System.currentTimeMillis()
                )
            )
        } else {
            // User lost
            val newLosses = currentProfile.losses + 1
            playerDao.updateProfile(
                PlayerProfileEntity.fromDomain(
                    currentProfile.copy(
                        losses = newLosses,
                        winStreak = 0
                    )
                )
            )
        }
    }

    suspend fun depositFunds(amount: Double) {
        val currentProfile = playerDao.getProfile().firstOrNull()?.toDomain() ?: PlayerProfile()
        val newBalance = currentProfile.balance + amount
        playerDao.updateProfile(
            PlayerProfileEntity.fromDomain(
                currentProfile.copy(balance = newBalance)
            )
        )
        transactionDao.insertTransaction(
            TransactionEntity(
                id = UUID.randomUUID().toString(),
                matchId = null,
                type = "DEPOSIT",
                amount = amount,
                description = "Added Gridiron match credits to wallet ($${String.format("%.2f", amount)})",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateProfile(profile: PlayerProfile) {
        playerDao.updateProfile(PlayerProfileEntity.fromDomain(profile))
    }

    suspend fun seedDatabaseIfEmpty() {
        GridironDatabase.populateInitialData(database)
    }
}
