package org.hwabeag.playertime.storage

import org.hwabeag.playertime.PlayerTime
import java.sql.Connection
import java.sql.DriverManager

abstract class JdbcTimeStorage(
    protected val plugin: PlayerTime,
    private val jdbcUrl: String,
    private val user: String? = null,
    private val password: String? = null
) : TimeStorage {

    protected fun connection(): Connection {
        return if (user != null) {
            DriverManager.getConnection(jdbcUrl, user, password ?: "")
        } else {
            DriverManager.getConnection(jdbcUrl)
        }
    }

    override fun close() {
        // 각 작업마다 연결을 열고 닫기 때문에 종료 시 정리할 리소스가 없다.
    }
}
