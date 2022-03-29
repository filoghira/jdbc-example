import Database.Database;
import Database.Table;

import javax.swing.*;

public class Main {

    private static final String address = "localhost";
    private static final String port = "3306";
    private static final String database = "fastfood";
    private static final String user = "scuola";
    private static final String password = "scuola";

    private static final String TABLE_NAME = "t_ingredient";

    public static void main(String[] args) {

        Database db = new Database(address, port, user, password, database);

        Table logicalTable = new Table(TABLE_NAME);
        logicalTable.init(db.getAllFromTable(TABLE_NAME), db);

        JFrame frame = new JFrame("Tabelle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(null);

        JTable table = new JTable(logicalTable.getModel());
        table.setBounds(10, 10, 480, 200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 480, 200);
        frame.add(scrollPane);

        frame.setVisible(true);
    }
}

