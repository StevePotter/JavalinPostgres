import io.javalin.Javalin

fun main() {
    val app = Javalin.create {
        it.showJavalinBanner = false
    }
        .get("/") { ctx -> ctx.result("Hello World") }
        .get("/json") { ctx -> ctx.json(Result("hel"))}
        .start(8765)
}

data class Result(val name: String)