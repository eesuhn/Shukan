package com.eesuhn.habittracker.feature.misc.export

import com.eesuhn.habittracker.core.database.entity.Action
import com.eesuhn.habittracker.core.database.entity.Habit
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Reader
import java.time.Instant

private val FORMAT = CSVFormat.DEFAULT
    .builder()
    .setRecordSeparator("\n")
    .build()

internal class BackupData(
    val habits: List<Habit>,
    val actions: List<Action>,
    val backupVersion: Int
)

internal object CSVHandler {

    fun exportHabitList(habits: List<Habit>): StringBuilder {
        val stringBuilder = StringBuilder()
        val printer = CSVPrinter(stringBuilder, FORMAT)
        printer.printRecord("id", "name", "color", "order", "archived", "notes")

        habits.forEach {
            printer.printRecord(it.id, it.name, it.color, it.order, it.archived, it.notes)
        }

        return stringBuilder
    }

    fun exportActionList(actions: List<Action>): StringBuilder {
        val stringBuilder = StringBuilder()
        val printer = CSVPrinter(stringBuilder, FORMAT)
        printer.printRecord("id", "habit_id", "timestamp")

        actions.forEach {
            printer.printRecord(it.id, it.habit_id, it.timestamp.toEpochMilli())
        }

        return stringBuilder
    }

    fun importHabitList(csvReader: Reader): List<Habit> {
        val habits = FORMAT.builder().setHeader().build().parse(csvReader).map {
            Habit(
                id = it.get("id").toInt(),
                name = it.get("name"),
                color = Habit.Color.valueOf(it.get("color")),
                order = it.get("order").toInt(),
                archived = it.get("archived").toBoolean(),
                notes = it.get("notes")
            )
        }
        return habits
    }

    fun importActionList(csvReader: Reader): List<Action> {
        val actions = FORMAT.builder().setHeader().build().parse(csvReader).map {
            Action(
                id = it.get("id").toInt(),
                habit_id = it.get("habit_id").toInt(),
                timestamp = Instant.ofEpochMilli(it.get("timestamp").toLong())
            )
        }
        return actions
    }
}