package dev.typr.foundationskt

import dev.typr.foundationskt.connect.*
import org.junit.Assert.assertEquals
import org.junit.Test

class EnumTypeTest {

    enum class Status { PENDING, ACTIVE, COMPLETED }

    @Test
    fun reifiedDuckDbEnum() {
        val statusType = DuckDbTypes.ofEnum<Status>("status")
        val tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor()

        tx.transact { conn ->
            Fragment.of("CREATE TYPE status AS ENUM('PENDING', 'ACTIVE', 'COMPLETED')").execute().run(conn)
            Fragment.of("CREATE TABLE eval_enum_test (id INTEGER, status status)").execute().run(conn)
            Fragment.of("INSERT INTO eval_enum_test VALUES (1, ")
                .value(statusType, Status.ACTIVE)
                .append(")")
                .execute().run(conn)

            val result = Fragment.of("SELECT status FROM eval_enum_test WHERE id = 1")
                .queryExactlyOne(statusType)
                .run(conn)

            assertEquals(Status.ACTIVE, result)
        }
    }

    @Test
    fun reifiedMariaDbEnum() {
        val statusType = MariaTypes.ofEnum<Status>()
        val rendered = Fragment.of("SELECT ").value(statusType, Status.PENDING).render()
        assertEquals("SELECT ?", rendered)
    }

    @Test
    fun reifiedSqlServerEnum() {
        val statusType = SqlServerTypes.ofEnum<Status>()
        val rendered = Fragment.of("SELECT ").value(statusType, Status.COMPLETED).render()
        assertEquals("SELECT ?", rendered)
    }

    @Test
    fun reifiedDb2Enum() {
        val statusType = Db2Types.ofEnum<Status>()
        val rendered = Fragment.of("SELECT ").value(statusType, Status.ACTIVE).render()
        assertEquals("SELECT ?", rendered)
    }

    @Test
    fun valuesBasedDuckDbEnum() {
        val statusType = DuckDbTypes.ofEnum("status", Status.values())
        val tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor()

        tx.transact { conn ->
            Fragment.of("CREATE TYPE status AS ENUM('PENDING', 'ACTIVE', 'COMPLETED')").execute().run(conn)
            Fragment.of("CREATE TABLE eval_enum_test2 (id INTEGER, status status)").execute().run(conn)
            Fragment.of("INSERT INTO eval_enum_test2 VALUES (1, ")
                .value(statusType, Status.COMPLETED)
                .append(")")
                .execute().run(conn)

            val result = Fragment.of("SELECT status FROM eval_enum_test2 WHERE id = 1")
                .queryExactlyOne(statusType)
                .run(conn)

            assertEquals(Status.COMPLETED, result)
        }
    }
}
