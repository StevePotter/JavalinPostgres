
package api

import data.UserEntity
import io.javalin.validation.ValidationError
import io.javalin.validation.ValidationException
import org.apache.commons.validator.routines.EmailValidator
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.WhitespaceRule
import java.time.Instant


data class User(
    val id: Int,
    val email: String,
    val createdAt: Instant,
)

data class UserCreateRequest(
    val email: String,
    val password: String
) {
    fun validate() {
        val errors = mutableMapOf<String, List<String>>()
        if (!isEmailValid(email)) errors[this::email.name] = listOf("Must be a valid email")

        val passwordValidationResults = passwordValidator.validate(PasswordData(password))
        if (!passwordValidationResults.isValid) {
            errors[this::password.name] = passwordValidator.getMessages(passwordValidationResults)
        }

        if (errors.isNotEmpty()) {
            throw ValidationException(
                errors.mapValues { it.value.map { ValidationError(it) } }
            )
        }
    }
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginSuccess(val apiToken: String)

data class LogoutRequest(val apiToken: String)


fun isEmailValid(email: String) = EmailValidator.getInstance().isValid(email)

private val passwordValidator by lazy {
    PasswordValidator(
        LengthRule(8, 30),
        CharacterRule(EnglishCharacterData.LowerCase),
        CharacterRule(EnglishCharacterData.UpperCase),
        CharacterRule(EnglishCharacterData.Digit),
        CharacterRule(EnglishCharacterData.Special),
        WhitespaceRule()
    )
}

data class ValidationErrors(
    val errors: Map<String, List<String>>
)
