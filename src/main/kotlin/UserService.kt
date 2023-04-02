import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.postgresql.util.PSQLException
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*

val Database.users get() = this.sequenceOf(UserTable)

class UserService {
    private val database = Database.connect(
        url = "jdbc:postgresql://localhost:5433/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres",
        logger = ConsoleLogger(threshold = LogLevel.INFO)
    )

    fun getUserByEmail(email: String): UserEntity? =
        database
            .users
            .find { it.email eq email }

    fun createUser(email: String, password: String): CreateUserResult {
        val salt = generateSalt()
        val hashedPassword = hash(password + salt)
        val userId = try {
            database.insertAndGenerateKey(UserTable) {
                set(it.email, email)
                set(it.password, hashedPassword)
                set(it.createdAt, Instant.now())
            }
        } catch (e: PSQLException) {
            // we could check for duplicates with an additional query above.  however that adds
            // overhead of an additional query and doesn't handle the race condition where createUser is called
            // concurrently and the record is inserted after the check in another thread
            // see https://www.postgresql.org/docs/current/errcodes-appendix.html for error codes
            if (e.sqlState == "23505") {
                return CreateUserResult.AlreadyExists
            }
            throw e
        }
        return CreateUserResult.Success(userId as Int)
    }


//    database.from(Users).select().forEach { row ->
//        println(row[Users.email])
//    }

}

fun generateSalt(size: Int = 16): String {
    val salt = ByteArray(size)
    SecureRandom().nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

fun hash(value: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
    return Base64.getEncoder().encodeToString(bytes)
}

sealed class CreateUserResult {
    object AlreadyExists: CreateUserResult()

    data class Success(val userId: Int): CreateUserResult()
}