package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.MatchStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MatchEntity::class, PlayerProfileEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GridironDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun playerDao(): PlayerDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: GridironDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GridironDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GridironDatabase::class.java,
                    "gridiron_stakes_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: GridironDatabase) {
            val now = System.currentTimeMillis()

            // Seed user profile
            val initialProfile = PlayerProfileEntity(
                id = "current_user",
                gamertag = "GridironKing27",
                platform = "PlayStation 5",
                favoriteTeamAbbr = "KC",
                balance = 250.00,
                wins = 18,
                losses = 6,
                winStreak = 4,
                totalEarnings = 740.00,
                rankTier = "All-Madden Legend"
            )
            db.playerDao().setProfile(initialProfile)

            // Seed matches
            val sampleMatches = listOf(
                MatchEntity(
                    id = "match_active_1",
                    matchCode = "MD-9942",
                    player1Gamertag = "GridironKing27",
                    player1Platform = "PlayStation 5",
                    player1TeamAbbr = "KC",
                    player1Score = 24,
                    player2Gamertag = "ViperClutch",
                    player2Platform = "PlayStation 5",
                    player2TeamAbbr = "BAL",
                    player2Score = 21,
                    stakeAmount = 50.00,
                    totalPot = 100.00,
                    status = MatchStatus.IN_PROGRESS.name,
                    quarterMinutes = 5,
                    difficulty = "All-Madden",
                    gameStyle = "Competitive",
                    customPlaybooksAllowed = true,
                    winnerGamertag = null,
                    p1Confirmed = false,
                    p2Confirmed = false,
                    disputeReason = null,
                    isOvertime = false,
                    currentQuarter = 4,
                    timeRemaining = "1:45",
                    p1Yards = 340,
                    p2Yards = 310,
                    p1Turnovers = 1,
                    p2Turnovers = 2,
                    createdAt = now - (25 * 60 * 1000),
                    completedAt = null,
                    notes = "High-roller rivalry rematch. Both players verified in party chat.",
                    sideBetDescription = "First to 200 passing yards",
                    sideBetAmount = 15.00,
                    sideBetWinner = "GridironKing27"
                ),
                MatchEntity(
                    id = "match_open_2",
                    matchCode = "MD-3301",
                    player1Gamertag = "DetroitDimeDropper",
                    player1Platform = "Xbox Series X",
                    player1TeamAbbr = "DET",
                    player1Score = 0,
                    player2Gamertag = "Waiting for Opponent...",
                    player2Platform = "Crossplay Enabled",
                    player2TeamAbbr = "SF",
                    player2Score = 0,
                    stakeAmount = 25.00,
                    totalPot = 50.00,
                    status = MatchStatus.OPEN.name,
                    quarterMinutes = 5,
                    difficulty = "All-Madden",
                    gameStyle = "Competitive",
                    customPlaybooksAllowed = false,
                    winnerGamertag = null,
                    p1Confirmed = false,
                    p2Confirmed = false,
                    disputeReason = null,
                    isOvertime = false,
                    currentQuarter = 1,
                    timeRemaining = "5:00",
                    p1Yards = 0,
                    p2Yards = 0,
                    p1Turnovers = 0,
                    p2Turnovers = 0,
                    createdAt = now - (5 * 60 * 1000),
                    completedAt = null,
                    notes = "Team playbooks only! No cheese plays allowed.",
                    sideBetDescription = "",
                    sideBetAmount = 0.0,
                    sideBetWinner = null
                ),
                MatchEntity(
                    id = "match_completed_3",
                    matchCode = "MD-1188",
                    player1Gamertag = "GridironKing27",
                    player1Platform = "PlayStation 5",
                    player1TeamAbbr = "KC",
                    player1Score = 31,
                    player2Gamertag = "BlitzGod_TX",
                    player2Platform = "PC EA App",
                    player2TeamAbbr = "DAL",
                    player2Score = 28,
                    stakeAmount = 40.00,
                    totalPot = 80.00,
                    status = MatchStatus.COMPLETED.name,
                    quarterMinutes = 5,
                    difficulty = "All-Madden",
                    gameStyle = "Competitive",
                    customPlaybooksAllowed = true,
                    winnerGamertag = "GridironKing27",
                    p1Confirmed = true,
                    p2Confirmed = true,
                    disputeReason = null,
                    isOvertime = true,
                    currentQuarter = 5,
                    timeRemaining = "0:00",
                    p1Yards = 412,
                    p2Yards = 388,
                    p1Turnovers = 0,
                    p2Turnovers = 1,
                    createdAt = now - (2 * 3600 * 1000),
                    completedAt = now - (90 * 60 * 1000),
                    notes = "Walk-off field goal in OT! Clean match with box score verified.",
                    sideBetDescription = "Over 50 total game points",
                    sideBetAmount = 10.00,
                    sideBetWinner = "GridironKing27"
                ),
                MatchEntity(
                    id = "match_completed_4",
                    matchCode = "MD-0941",
                    player1Gamertag = "GridironKing27",
                    player1Platform = "PlayStation 5",
                    player1TeamAbbr = "PHI",
                    player1Score = 35,
                    player2Gamertag = "CincySlinger",
                    player2Platform = "Xbox Series X",
                    player2TeamAbbr = "CIN",
                    player2Score = 17,
                    stakeAmount = 20.00,
                    totalPot = 40.00,
                    status = MatchStatus.COMPLETED.name,
                    quarterMinutes = 4,
                    difficulty = "All-Pro",
                    gameStyle = "Competitive",
                    customPlaybooksAllowed = true,
                    winnerGamertag = "GridironKing27",
                    p1Confirmed = true,
                    p2Confirmed = true,
                    disputeReason = null,
                    isOvertime = false,
                    currentQuarter = 4,
                    timeRemaining = "0:00",
                    p1Yards = 390,
                    p2Yards = 220,
                    p1Turnovers = 1,
                    p2Turnovers = 3,
                    createdAt = now - (8 * 3600 * 1000),
                    completedAt = now - (7 * 3600 * 1000),
                    notes = "Dominant rushing attack. 2 pick-sixes sealed the bag.",
                    sideBetDescription = "",
                    sideBetAmount = 0.0,
                    sideBetWinner = null
                )
            )
            db.matchDao().insertMatches(sampleMatches)

            // Seed initial transactions
            val sampleTransactions = listOf(
                TransactionEntity(
                    id = "tx_1",
                    matchId = "match_completed_3",
                    type = "MATCH_WIN",
                    amount = 80.00,
                    description = "Madden 27 Match Win vs BlitzGod_TX (Pot: $80.00)",
                    timestamp = now - (90 * 60 * 1000)
                ),
                TransactionEntity(
                    id = "tx_2",
                    matchId = "match_completed_3",
                    type = "SIDE_BET_WIN",
                    amount = 20.00,
                    description = "Prop Bet: Over 50 game points won vs BlitzGod_TX",
                    timestamp = now - (90 * 60 * 1000)
                ),
                TransactionEntity(
                    id = "tx_3",
                    matchId = "match_active_1",
                    type = "ESCROW_LOCK",
                    amount = -50.00,
                    description = "Escrow locked for 1v1 Match vs ViperClutch",
                    timestamp = now - (25 * 60 * 1000)
                ),
                TransactionEntity(
                    id = "tx_4",
                    matchId = "match_completed_4",
                    type = "MATCH_WIN",
                    amount = 40.00,
                    description = "Madden 27 Match Win vs CincySlinger (Pot: $40.00)",
                    timestamp = now - (7 * 3600 * 1000)
                )
            )
            db.transactionDao().insertTransactions(sampleTransactions)
        }
    }
}
