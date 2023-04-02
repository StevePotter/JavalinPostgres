
package api

import domain.CreateUserResult
import domain.LoginResult
import domain.LogoutResult
import domain.UserService
import invalid
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass

fun registerRoutes(app: Javalin): Javalin {
    app.post("/users") { ctx ->
        val request: UserCreateRequest = ctx.bodyAsClass()
        request.validate()
        when (val result = UserService.createUser(email = request.email, password = request.password)) {
            is CreateUserResult.Success -> {
                ctx.status(HttpStatus.CREATED)
                    .json(result)
            }
            is CreateUserResult.AlreadyExists -> {
                ctx.invalid(request::email, "Someone already signed up with that email.")
                    .status(HttpStatus.CONFLICT)
            }
        }
    }
    app.post("/login") { ctx ->
        val request: LoginRequest = ctx.bodyAsClass()
        when (val result = UserService.login(email = request.email, password = request.password)) {
            is LoginResult.WrongPassword -> {
                ctx.invalid(request::password, "Incorrect password.")
                    .status(HttpStatus.UNAUTHORIZED)
            }
            is LoginResult.Success -> {
                ctx.status(HttpStatus.OK)
                    .json(LoginSuccess(result.apiToken))
            }
            is LoginResult.DoesNotExist -> {
                ctx.invalid(request::email, "Nobody has signed up with that email.")
                    .status(HttpStatus.UNAUTHORIZED)
            }
        }
    }
    app.post("/logout") { ctx ->
        val request: LogoutRequest = ctx.bodyAsClass()
        when (UserService.logout(apiToken = request.apiToken)) {
            is LogoutResult.Success -> {
                ctx.status(HttpStatus.OK).result("Bye!")
            }
            is LogoutResult.DoesNotExist -> {
                ctx.invalid(request::apiToken, "API token not found.")
            }
        }
    }
    return app
}
