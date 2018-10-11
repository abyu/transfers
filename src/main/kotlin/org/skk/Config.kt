package org.skk

class Config {

    val appPort: Int
        get() = System.getenv("APP_PORT")?.toInt() ?: 8080

    val databaseConfig: DatabaseConfig
        get() = DatabaseConfig()
}

class DatabaseConfig {

    val connectionString: String
        get() = System.getenv("DB_SOURCE") ?: "jdbc:h2:mem:regular;DB_CLOSE_DELAY=1"

    val driver: String
        get() = System.getenv("DB_TYPE") ?: "org.h2.Driver"
}