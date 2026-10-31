package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY createdAt DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    fun getMatchById(id: String): Flow<MatchEntity?>

    @Query("SELECT * FROM matches WHERE status IN ('IN_PROGRESS', 'ESCROW_LOCKED') ORDER BY createdAt DESC LIMIT 1")
    fun getActiveMatch(): Flow<MatchEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun deleteMatch(id: String)
}

@Dao
interface PlayerDao {
    @Query("SELECT * FROM profile WHERE id = :id")
    fun getProfile(id: String = "current_user"): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setProfile(profile: PlayerProfileEntity)

    @Update
    suspend fun updateProfile(profile: PlayerProfileEntity)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)
}
