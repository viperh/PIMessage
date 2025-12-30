package org.me.qviperh.pIMessage.database

import org.me.qviperh.pIMessage.PIMessage
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.concurrent.ConcurrentHashMap

class DatabaseManager(val plugin: PIMessage) {
    lateinit var dbFile: File
    private var connection: Connection? = null

    private val cache = ConcurrentHashMap<String, Int>()

    fun init() {
        this.dbFile = File(plugin.dataFolder, "ids.db")
        if (!dbFile.exists()) {
            plugin.dataFolder.mkdirs()
            dbFile.createNewFile()
            plugin.logger.info("Database file created")
        }
        connect()
        createTables()
        loadIds()
    }

    fun connect() {
        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
        plugin.logger.info("Connected to database successfully!")
    }

    private fun createTables() {
        connection?.createStatement()?.use {stmt ->
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ids (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user TEXT UNIQUE
                )
            """.trimIndent())
        }
    }

    fun insertId(user: String) {
        val sql = "INSERT OR IGNORE INTO ids(user) VALUES (?)"

        connection?.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)?.use { stmt ->
            stmt.setString(1, user)
            stmt.executeUpdate()

            stmt.generatedKeys.use { keys ->
                if (keys.next()) {
                    val generatedId = keys.getInt(1)
                    // new ID was created
                } else {
                    // user already exists, fetch the existing ID
                    connection?.prepareStatement("SELECT id FROM ids WHERE user = ?")?.use { select ->
                        select.setString(1, user)
                        select.executeQuery().use { rs ->
                            if (rs.next()) {
                                val existingId = rs.getInt("id")
                                cache.putIfAbsent(user, existingId)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getId(user: String) : Int {
        val cached = cache[user] ?: run {
            connection?.prepareStatement("""
                SELECT id from ids WHERE user = ?;
            """.trimIndent())
                .use { stmt ->
                    stmt?.setString(1, user)
                    stmt?.executeQuery().use { rs ->
                        val id = rs?.getInt(1) ?: return -1
                        cache.putIfAbsent(user, id)
                        return id
                    }
                }
        }
        return cached
    }

    private fun loadIds() {
        for (user in plugin.server.onlinePlayers) {
            val id = getId(user.name)
            cache.putIfAbsent(user.name, id)
        }
    }

}