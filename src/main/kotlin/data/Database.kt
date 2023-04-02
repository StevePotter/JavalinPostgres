package data

import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

internal val database by lazy {
    Database.connect(
        url = "jdbc:postgresql://localhost:5433/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres",
        logger = ConsoleLogger(threshold = LogLevel.INFO)
    )
}

sealed class InsertResult {
    object AlreadyExists: InsertResult()
    data class Success(val primaryKey: Int): InsertResult()
}
