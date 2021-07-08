package ucf.assignments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Connect {
    private Connect() {}

    public static void connect() {
        Connection  connection = null;

        try {
            String url = "jdbc:sqlite:ToDoListDB.sqlite";

            connection = DriverManager.getConnection(url);

            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
