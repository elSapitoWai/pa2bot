package com.pa2.bot.database;

import com.pa2.bot.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try{
            final File dbFile = new File("database.db");

            if (!dbFile.exists()){
                if (dbFile.createNewFile()){
                    LOGGER.info("Created database");
                }else {
                    LOGGER.info("Couldn't create the database");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try(final Statement statement = getConnection().createStatement()){
            final String defaultPrefix = Config.get("prefix");


            // language=SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '"+defaultPrefix+"');");
            LOGGER.info("Table initialised");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDataSource() {

    }

    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
}
