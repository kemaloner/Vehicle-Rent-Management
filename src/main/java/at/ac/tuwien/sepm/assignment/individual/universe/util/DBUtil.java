package at.ac.tuwien.sepm.assignment.individual.universe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Connection con= null;

    public static Connection getConnection() throws SQLException {
        if (con == null) {
            con=openConnection();
        }
        return con;
    }

    public static void closeConnection() throws SQLException {
        try {
            if(con!=null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Close the connection not possible",e);
        }
    }

    private static Connection openConnection() throws SQLException {

        Connection connection=null;

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOG.error("Setting the driver is not possible",e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:h2:~/sepm;INIT=RUNSCRIPT FROM 'classpath:sql/createAndinsert.sql'", "sa", "");
            LOG.info("Connection is successfull");
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Connection to Database failed.",e);
        }

        return connection;
    }

}
