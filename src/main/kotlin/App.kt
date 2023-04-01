import io.javalin.Javalin
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.select

fun main() {
    val app = Javalin.create {
        it.showJavalinBanner = false
    }
        .get("/") { ctx -> ctx.result("Hello World") }
        .get("/users") {
            val database = Database.connect(
                url = "jdbc:postgresql://localhost:5433/postgres",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "postgres"
            )
            database.from(Users).select().forEach { row ->
                println(row[Users.email])
            }

        }
        .get("/json") { ctx -> ctx.json(Result("hel"))}
        .start(8765)
}

data class Result(val name: String)