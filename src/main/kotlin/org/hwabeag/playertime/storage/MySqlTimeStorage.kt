package org.hwabeag.playertime.storage

import org.hwabeag.playertime.PlayerTime

class MySqlTimeStorage(plugin: PlayerTime) : JdbcTimeStorage(
    plugin = plugin,
    jdbcUrl = buildUrl(plugin),
    user = plugin.configManager.mysqlUsername(),
    password = plugin.configManager.mysqlPassword()
) {

    override fun initialize() {
        Class.forName("com.mysql.cj.jdbc.Driver")

        connection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS player_time (
                        uuid VARCHAR(36) PRIMARY KEY,
                        name VARCHAR(16) NOT NULL,
                        minutes INT NOT NULL DEFAULT 0,
                        updated_at BIGINT NOT NULL,
                        INDEX idx_player_time_name (name),
                        INDEX idx_player_time_minutes (minutes)
                    )
                    """.trimIndent()
                )
            }
        }
    }

    override fun ensurePlayer(uuid: String, name: String) {
        connection().use { conn ->
            conn.prepareStatement(
                """
                INSERT INTO player_time (uuid, name, minutes, updated_at)
                VALUES (?, ?, 0, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    updated_at = VALUES(updated_at)
                """.trimIndent()
            ).use { ps ->
                ps.setString(1, uuid)
                ps.setString(2, name)
                ps.setLong(3, System.currentTimeMillis())
                ps.executeUpdate()
            }
        }
    }

    override fun incrementMinutes(uuid: String, name: String) {
        connection().use { conn ->
            conn.prepareStatement(
                """
                INSERT INTO player_time (uuid, name, minutes, updated_at)
                VALUES (?, ?, 1, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    minutes = minutes + 1,
                    updated_at = VALUES(updated_at)
                """.trimIndent()
            ).use { ps ->
                ps.setString(1, uuid)
                ps.setString(2, name)
                ps.setLong(3, System.currentTimeMillis())
                ps.executeUpdate()
            }
        }
    }

    override fun getMinutesByName(name: String): Int? {
        connection().use { conn ->
            conn.prepareStatement(
                "SELECT minutes FROM player_time WHERE LOWER(name) = LOWER(?) LIMIT 1"
            ).use { ps ->
                ps.setString(1, name)
                ps.executeQuery().use { rs ->
                    return if (rs.next()) rs.getInt("minutes") else null
                }
            }
        }
    }

    override fun setMinutesByName(name: String, minutes: Int): Boolean {
        connection().use { conn ->
            conn.prepareStatement(
                "UPDATE player_time SET minutes = ?, updated_at = ? WHERE LOWER(name) = LOWER(?)"
            ).use { ps ->
                ps.setInt(1, minutes)
                ps.setLong(2, System.currentTimeMillis())
                ps.setString(3, name)
                return ps.executeUpdate() > 0
            }
        }
    }

    override fun addMinutesByName(name: String, minutes: Int): Int? {
        connection().use { conn ->
            conn.prepareStatement(
                "UPDATE player_time SET minutes = minutes + ?, updated_at = ? WHERE LOWER(name) = LOWER(?)"
            ).use { ps ->
                ps.setInt(1, minutes)
                ps.setLong(2, System.currentTimeMillis())
                ps.setString(3, name)
                val changed = ps.executeUpdate()
                if (changed <= 0) return null
            }

            conn.prepareStatement(
                "SELECT minutes FROM player_time WHERE LOWER(name) = LOWER(?) LIMIT 1"
            ).use { ps ->
                ps.setString(1, name)
                ps.executeQuery().use { rs ->
                    return if (rs.next()) rs.getInt("minutes") else null
                }
            }
        }
    }

    override fun top(limit: Int): List<TopEntry> {
        val list = mutableListOf<TopEntry>()
        connection().use { conn ->
            conn.prepareStatement(
                "SELECT name, minutes FROM player_time ORDER BY minutes DESC, name ASC LIMIT ?"
            ).use { ps ->
                ps.setInt(1, limit)
                ps.executeQuery().use { rs ->
                    while (rs.next()) {
                        list += TopEntry(
                            name = rs.getString("name"),
                            minutes = rs.getInt("minutes")
                        )
                    }
                }
            }
        }
        return list
    }

    companion object {
        private fun buildUrl(plugin: PlayerTime): String {
            val host = plugin.configManager.mysqlHost()
            val port = plugin.configManager.mysqlPort()
            val database = plugin.configManager.mysqlDatabase()
            val useSsl = plugin.configManager.mysqlUseSsl()
            return "jdbc:mysql://$host:$port/$database?useSSL=$useSsl&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        }
    }
}
