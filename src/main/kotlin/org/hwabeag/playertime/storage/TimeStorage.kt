package org.hwabeag.playertime.storage

data class TopEntry(
    val name: String,
    val minutes: Int
)

interface TimeStorage {
    fun initialize()
    fun close()

    fun ensurePlayer(uuid: String, name: String)
    fun incrementMinutes(uuid: String, name: String)

    fun getMinutesByName(name: String): Int?
    fun setMinutesByName(name: String, minutes: Int): Boolean
    fun addMinutesByName(name: String, minutes: Int): Int?

    fun top(limit: Int): List<TopEntry>
}
