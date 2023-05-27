package at.ac.tuwien.sepm.assignment.individual.universe;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtilTest {

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
        }
    }

    private static Connection openConnection() throws SQLException {

        Connection connection=null;

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:h2:~/sepm;INIT=RUNSCRIPT FROM 'classpath:sql/createAndinsert.sql'", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
