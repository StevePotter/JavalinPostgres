import org.apache.commons.validator.routines.EmailValidator

fun isEmailValid(email: String) = EmailValidator.getInstance().isValid(email)

fun isPasswordValid(password: String) =
