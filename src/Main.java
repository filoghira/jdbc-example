import database.Database;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    private static final String ADDRESS = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "fastfood";
    private static final String USER = "scuola";
    private static final String PASSWORD = "scuola";

    private static final String TABLE_NAME = "t_ingredient";

    public static void main(String[] args) throws SQLException {

        Database db = new Database(ADDRESS, PORT, USER, PASSWORD, DATABASE);

        JFrame frame = new JFrame("Tabelle");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                db.closeConnection();
            }
        });

        frame.setSize(500, 300);
        frame.setLayout(null);

        frame.add(db.getTable(TABLE_NAME).getScrollPane(10, 10, 480, 200));

        frame.setVisible(true);
    }
}

