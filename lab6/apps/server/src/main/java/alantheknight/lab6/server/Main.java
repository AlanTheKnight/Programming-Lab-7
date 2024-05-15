package alantheknight.lab6.server;

import alantheknight.lab6.server.managers.CollectionManager;
import alantheknight.lab6.server.managers.DBManager;
import alantheknight.lab6.server.managers.ServerCommandManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;


public class Main {
    public static final ServerCommandManager commandManager = new ServerCommandManager();
    public static final Logger logger = LogManager.getLogger(Main.class);
    public static final DBManager dbManager = new DBManager();
    public static final CollectionManager collectionManager = new CollectionManager();

    public static void main(String[] args) {
        try {
            dbManager.connectSSH();
        } catch (DBManager.DBConnectionException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            collectionManager.pullFromDB();
            logger.log(Level.INFO, "Collection has been loaded from the database");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to load the collection: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        commandManager.initCommands();
        logger.log(Level.INFO, "Commands have been initialized");

        UDPServer server = new UDPServer();
        server.start();
    }
}