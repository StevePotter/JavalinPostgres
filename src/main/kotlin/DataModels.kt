
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import java.time.Instant

interface UserEntity: Entity<UserEntity> {
    companion object : Entity.Factory<UserEntity>()
    val id: Int
    val email: String
    val password: String
    val createdAt: Instant
}

object UserTable : Table<UserEntity>("users") {
    val id = int("id").primaryKey().bindTo(UserEntity::id)
    val email = varchar("email").bindTo(UserEntity::email)
    val password = varchar("password").bindTo(UserEntity::password)
    val createdAt = timestamp("created_on").bindTo(UserEntity::createdAt)
}