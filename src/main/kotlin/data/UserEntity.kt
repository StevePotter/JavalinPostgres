package data

import org.ktorm.database.Database
import org.ktorm.dsl.AssignmentsBuilder
import org.ktorm.dsl.UpdateStatementBuilder
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.Entity
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import org.postgresql.util.PSQLException
import java.time.Instant

interface UserEntity: Entity<UserEntity> {
    companion object : Entity.Factory<UserEntity>()
    val id: Int
    val email: String
    val passwordHash: String
    val passwordSalt: String
    val apiToken: String
    val createdAt: Instant
}

object UserTable : Table<UserEntity>("users") {
    val id = int("id").primaryKey().bindTo(UserEntity::id)
    val email = varchar("email").bindTo(UserEntity::email)
    val passwordHash = varchar("password_hash").bindTo(UserEntity::passwordHash)
    val passwordSalt = varchar("password_salt").bindTo(UserEntity::passwordSalt)
    val apiToken = varchar("api_token").bindTo(UserEntity::apiToken)
    val createdAt = timestamp("created_on").bindTo(UserEntity::createdAt)
}

val Database.users get() = this.sequenceOf(UserTable)

object UserQueries {
    fun <T: Any> get(column: Column<T>, value: T) = database.users.find { column eq value }

    fun insert(setValues: AssignmentsBuilder.(UserTable) -> Unit): InsertResult {
        val userId = try {
            database.insertAndGenerateKey(UserTable, setValues)
        } catch (e: PSQLException) {
            // we could check for duplicates with an additional query above.  however that adds
            // overhead of an additional query and doesn't handle the race condition where createUser is called
            // concurrently and the record is inserted after the check in another thread
            // see https://www.postgresql.org/docs/current/errcodes-appendix.html for error codes
            if (e.sqlState == "23505") {
                return InsertResult.AlreadyExists
            }
            throw e
        }
        return InsertResult.Success(userId.toString().toInt())
    }

    fun update(userId: Int, setValues: UpdateStatementBuilder.(UserTable) -> Unit) {
        database.update(UserTable) {
            setValues(it)
            where {
                it.id eq userId
            }
        }
    }
}
