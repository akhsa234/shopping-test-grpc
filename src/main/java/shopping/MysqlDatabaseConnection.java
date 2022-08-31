package shopping;

import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Gets a connection to the database
 * Loads a database driver class and gets connection using url, username and password
 * */
public class MysqlDatabaseConnection {
    private static final Logger logger = Logger.getLogger(MysqlDatabaseConnection.class.getName());

    static {
        try {
            initializeDatabase(getConnectionToDatabase());
        } catch (FileNotFoundException exception) {
            logger.log(Level.SEVERE, "Could not find the .sql file", exception);
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "SQL error", exception);
        }
    }

    static Server server;

    public static Connection getConnectionToDatabase() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver"); // Class.forName("org.h2.Driver");
//            connection = DriverManager.getConnection(
//                    "jdbc:h2:mem:shoppingDb", "", "");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shopping", "root", "123456");

        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Could not set up connection", exception);
        }
        logger.info("Connection set up completed");
        return connection;
    }

    /*
     * Starts the database TCP server in case one needs to access it using a 3rd party(external) client
     *
     * */
    public static void startDatabase() throws SQLException {
        server = Server.createTcpServer().start();
    }

    /*
     * Stops the database server
     *
     * */
    public static void stopDatabase() {
        server.stop();
    }

    /* Loads the initialize.sql file from the classpath folder "resources".
    Runs all the queries from the file to create tables, insert records and make it ready to use
    **/
    public static void initializeDatabase(Connection conn) throws FileNotFoundException, SQLException {
        InputStream resource = MysqlDatabaseConnection.class.getClassLoader().getResourceAsStream("initialize.sql");
        RunScript.execute(conn, new InputStreamReader(resource));
    }

}
