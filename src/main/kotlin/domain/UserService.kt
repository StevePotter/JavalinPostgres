package domain

import data.InsertResult
import data.UserQueries
import data.UserTable
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*

object UserService {
    fun createUser(email: String, password: String): CreateUserResult {
        val salt = generateSalt()
        val hashedPassword = hash(password + salt)
        val apiToken = UUID.randomUUID().toString()
        return when (UserQueries.insert {
            set(it.email, email)
            set(it.passwordHash, hashedPassword)
            set(it.passwordSalt, salt)
            set(it.apiToken, apiToken)
            set(it.createdAt, Instant.now())
        }) {
            is InsertResult.Success -> CreateUserResult.Success(apiToken)
            is InsertResult.AlreadyExists -> CreateUserResult.AlreadyExists
        }
    }

    fun login(email: String, password: String): LoginResult {
        val user = UserQueries.get(UserTable.email, email) ?: return LoginResult.DoesNotExist

        val hashedPassword = hash(password + user.passwordSalt)
        if (hashedPassword != user.passwordHash) {
            return LoginResult.WrongPassword
        }

        // create a new API token
        val apiToken = UUID.randomUUID().toString()
        UserQueries.update(user.id) {
            set(it.apiToken, apiToken)
        }
        return LoginResult.Success(apiToken)
    }

    fun logout(apiToken: String): LogoutResult {
        val user = UserQueries.get(UserTable.apiToken, apiToken) ?: return LogoutResult.DoesNotExist
        UserQueries.update(user.id) {
            set(it.apiToken, null)
        }
        return LogoutResult.Success
    }
}

fun generateSalt(): String {
    val salt = ByteArray(16)
    SecureRandom().nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

fun hash(value: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
    return Base64.getEncoder().encodeToString(bytes)
}

sealed class CreateUserResult {
    object AlreadyExists: CreateUserResult()

    data class Success(val apiToken: String): CreateUserResult()
}

sealed class LoginResult {
    object DoesNotExist: LoginResult()

    object WrongPassword: LoginResult()

    data class Success(val apiToken: String): LoginResult()
}

sealed class LogoutResult {
    object DoesNotExist: LogoutResult()

    object Success: LogoutResult()
}