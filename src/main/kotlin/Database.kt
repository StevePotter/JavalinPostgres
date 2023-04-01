import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar

object Users : Table<Nothing>("users") {
    val id = int("id").primaryKey()
    val email = varchar("email")
    val createdAt = timestamp("created_on")
}