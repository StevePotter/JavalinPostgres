
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import io.javalin.json.JavalinJackson
import io.javalin.validation.ValidationException

fun main() {
    val userService = UserService()

    val app = Javalin.create {
        it.showJavalinBanner = false
        it.jsonMapper(
            JavalinJackson(
                jacksonMapperBuilder()
                .addModule(JavaTimeModule())
                .build()
            )
        )
    }
        .post("/users") { ctx ->
            ValidationException()
            val userRequest = ctx.bodyValidator<UserCreateRequest>()
                .check({ validateUser(it) })
                .get()
            when (userService.createUser(email = userRequest.email, password = userRequest.password)) {
                is CreateUserResult.Success -> {
                    ctx.status(HttpStatus.CREATED)
                }
                is CreateUserResult.AlreadyExists -> {
                    ctx.status(HttpStatus.CONFLICT)
                    ctx.result("Someone already signed up with that email.")
                }
            }
        }
//        .routes {
//            path("users") {
//                path("{id}") {
//                    get {
//
//                    }
//                }
//            }
//        }
//        .get("/") { ctx -> ctx.result("Hello World") }
//
//        .get("/users") {
//            val user = userService.getUserByEmail("me@stevepotter.me").toViewModel()
//            if (user != null)
//                it.json(user)
//
//        }
//        .get("/json") { ctx -> ctx.json(Result("hel"))}
        .start(8765)
}

private fun UserEntity?.toViewModel(): User? = this?.let {
    User(
        id = it.id,
        email = it.email,
        createdAt = it.createdAt
    )
}
