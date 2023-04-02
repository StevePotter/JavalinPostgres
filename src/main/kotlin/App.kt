
import api.ValidationErrors
import api.registerRoutes
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.json.JavalinJackson
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

fun main() {
    Javalin
        .create { config ->
            config.showJavalinBanner = false
            config.jsonMapper(
                JavalinJackson(
                    jacksonMapperBuilder()
                        .addModule(JavaTimeModule())
                        .build()
                )
            )
        }
        .exception(MissingKotlinParameterException::class.java) { e, ctx ->
            ctx.invalid(e.parameter, "Missing parameter")
        }
        .exception(UnrecognizedPropertyException::class.java) { e, ctx ->
            ctx.invalid(e.propertyName, "Unknown parameter")
        }
        .also { registerRoutes(it) }
        .start(8765)
}

fun Context.invalid(parameter: KParameter, message: String) = this.invalid(parameter.name ?: "UNKNOWN", message)

fun <T> Context.invalid(property: KProperty<T>, message: String) = this.invalid(property.name, message)

fun Context.invalid(parameter: String, message: String) = this.invalid(ValidationErrors(mapOf(parameter to listOf(message))))

fun Context.invalid(errors: ValidationErrors) = this.status(HttpStatus.BAD_REQUEST).json(errors)
