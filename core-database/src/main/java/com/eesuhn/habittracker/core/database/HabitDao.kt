package com.eesuhn.habittracker.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eesuhn.habittracker.core.database.entity.Action
import com.eesuhn.habittracker.core.database.entity.ActionCompletionRate
import com.eesuhn.habittracker.core.database.entity.ActionCountByMonth
import com.eesuhn.habittracker.core.database.entity.ActionCountByWeek
import com.eesuhn.habittracker.core.database.entity.Habit
import com.eesuhn.habittracker.core.database.entity.HabitActionCount
import com.eesuhn.habittracker.core.database.entity.HabitById
import com.eesuhn.habittracker.core.database.entity.HabitId
import com.eesuhn.habittracker.core.database.entity.HabitTopDay
import com.eesuhn.habittracker.core.database.entity.HabitWithActions
import com.eesuhn.habittracker.core.database.entity.SumActionCountByDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit ORDER BY `order` ASC")
    suspend fun getHabits(): List<Habit>

    @Insert
    suspend fun insertHabits(habits: List<Habit>)

    @Delete(entity = Habit::class)
    suspend fun deleteHabit(habitById: HabitById)

    @Query("DELETE FROM habit")
    suspend fun deleteAllHabits()

    @Query("DELETE FROM `action`")
    suspend fun deleteAllActions()

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("SELECT count(*) FROM habit")
    fun getTotalHabitCount(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM habit WHERE archived == 0 ORDER BY `order` ASC")
    fun getActiveHabitsWithActions(): Flow<List<HabitWithActions>>

    @Transaction
    @Query("SELECT * FROM habit WHERE archived == 1 ORDER BY `id` ASC")
    fun getArchivedHabitsWithActions(): Flow<List<HabitWithActions>>

    @Query("SELECT * FROM `action` ORDER BY timestamp ASC")
    fun getAllActions(): Flow<List<Action>>

    @Query("SELECT * FROM habit WHERE id IN (:id1, :id2)")
    suspend fun getHabitPair(id1: HabitId, id2: HabitId): List<Habit>

    @Query(
        """UPDATE habit
            SET `order` = CASE id WHEN :id1 THEN :order1 WHEN :id2 THEN :order2 END
            WHERE id IN (:id1, :id2)
        """
    )
    suspend fun updateHabitOrders(id1: HabitId, order1: Int, id2: HabitId, order2: Int)

    @Query("UPDATE habit SET archived = 0 WHERE id == :id")
    suspend fun unarchiveHabit(id: HabitId)

    @Transaction
    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitWithActions(habitId: Int): HabitWithActions

    @Insert
    suspend fun insertActions(actions: List<Action>)

    @Query("DELETE FROM `action` WHERE id = :id")
    suspend fun deleteAction(id: Int)

    @Transaction
    suspend fun restoreBackup(habitsToRestore: List<Habit>, actionsToRestore: List<Action>) {
        deleteAllHabits()
        deleteAllActions()
        insertHabits(habitsToRestore)
        insertActions(actionsToRestore)
    }

    /**
     * When there are no actions returns UNIX epoch time as first_day and action_count of 0
     */
    @Query(
        """SELECT
                min(timestamp) as first_day,
                count(*) as action_count
            FROM `action`
            WHERE habit_id = :habitId
    """
    )
    suspend fun getCompletionRate(habitId: Int): ActionCompletionRate

    @Query(
        """SELECT 
                strftime('%Y', timestamp / 1000, 'unixepoch', 'localtime') as year,
                strftime('%m', timestamp / 1000, 'unixepoch', 'localtime') as month,
                count(*) as action_count
            FROM `action`
            WHERE habit_id = :habitId
            GROUP BY year, month"""
    )
    suspend fun getActionCountByMonth(habitId: Int): List<ActionCountByMonth>

    // Week of year calculation explanation: https://stackoverflow.com/a/15511864/745637
    @Query(
        """SELECT
                strftime('%Y', date(timestamp / 1000, 'unixepoch', 'localtime', '-3 days', 'weekday 4')) as year,
                (strftime('%j', date(timestamp / 1000, 'unixepoch', 'localtime', '-3 days', 'weekday 4')) - 1) / 7 + 1 as week,
                count(*) as action_count
            FROM `action`
            WHERE habit_id = :habitId
            GROUP BY year, week
            ORDER BY year ASC, week ASC"""
    )
    suspend fun getActionCountByWeek(habitId: Int): List<ActionCountByWeek>

    @Query(
        """SELECT
                date(timestamp / 1000, 'unixepoch', 'localtime') as date,
                count(*) AS action_count
            FROM `action`
            WHERE date >= date(:from) AND date <= date(:to)
            GROUP BY date
        """
    )
    suspend fun getSumActionCountByDay(from: LocalDate, to: LocalDate): List<SumActionCountByDay>

    @Query(
        """SELECT
                habit.id AS habit_id,
                habit.name AS name,
                date(min(`action`.timestamp) / 1000, 'unixepoch', 'localtime') as first_day,
                count(habit_id) AS count
            FROM habit
            LEFT JOIN `action` ON habit.id = `action`.habit_id
            GROUP BY habit.id
            ORDER BY count DESC
            LIMIT :count"""
    )
    suspend fun getMostSuccessfulHabits(count: Int): List<HabitActionCount>

    @Query(
        """SELECT
                id as habit_id,
                name,
                (
                    SELECT strftime('%w', `action`.timestamp / 1000, 'unixepoch', 'localtime') as day_of_week
                    FROM `action`
                    WHERE habit_id = habit.id
                    GROUP BY day_of_week
                    ORDER BY count(*) DESC
                    LIMIT 1
                ) as top_day_of_week,
                (
                    SELECT count(*) as count
                    FROM `action`
                    WHERE habit_id = habit.id
                    GROUP BY strftime('%w', `action`.timestamp / 1000, 'unixepoch', 'localtime')
                    ORDER BY count DESC
                    LIMIT 1
                ) as action_count_on_day
            FROM habit"""
    )
    suspend fun getTopDayForHabits(): List<HabitTopDay>

    @Query(
        """SELECT habit.*
            FROM habit
            INNER JOIN `action` ON habit.id = `action`.habit_id
            WHERE date(`action`.timestamp / 1000, 'unixepoch', 'localtime') = date(:date)
            ORDER BY habit.`order` ASC
        """
    )
    suspend fun getCompletedHabitsAt(date: LocalDate): List<Habit>

}