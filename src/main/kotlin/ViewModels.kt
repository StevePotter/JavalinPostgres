import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import org.apache.commons.validator.routines.EmailValidator
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
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

        .validate(PasswordData(password))
        if (!passwordValidationResults.isValid) {
            errors[this::password.name] = passwordValidationResults.
        }
    }
}


val validateUser = Validation<UserCreateRequest> {
    UserCreateRequest::email {
        minLength(2)
        maxLength(200)
        addConstraint("Must be a valid email address") {
            EmailValidator.getInstance().isValid(it)
        }
    }

    UserCreateRequest::password {
        minLength(8)
        maxLength(100)
        addConstraint("Must contain at least one lowercase letter, one uppercase letter, one number, and one special character") {
            PasswordValidator(
                CharacterRule(EnglishCharacterData.LowerCase),
                CharacterRule(EnglishCharacterData.UpperCase),
                CharacterRule(EnglishCharacterData.Digit),
                CharacterRule(EnglishCharacterData.Special),
                WhitespaceRule()
            ).validate(it).isValid
        }
    }
}
