package data

import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

internal val database by lazy {
    val host = System.getenv("DB_HOST") ?: "localhost"
    val port = System.getenv("DB_PORT") ?: "5432"

    println("jdbc:postgresql://$host:$port/postgres")

    Database.connect(
        url = "jdbc:postgresql://$host:$port/postgres",
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
