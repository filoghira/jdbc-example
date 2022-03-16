import Database.Database;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Main {

    private static final String address = "localhost";
    private static final String port = "3306";
    private static final String database = "fastfood";
    private static final String user = "scuola";
    private static final String password = "scuola";

    public static void main(String[] args) throws SQLException {

        Database db = new Database(address, port, database, user, password);

        ResultSet rs = db.getAllFromTable("t_ingredient");

        // Get table header
        Vector<String> header = new Vector<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            header.add(rs.getMetaData().getColumnName(i));
        }

        Vector<Vector<Object>> v = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getObject(i));
            }
            v.add(row);
        }

        JFrame frame = new JFrame("Tabelle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(null);

        TableModel model = new TableModel();
        model.setData(v);
        model.setHeader(header);

        JTable table = new JTable(model);
        table.setBounds(10, 10, 480, 200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 480, 200);
        frame.add(scrollPane);

        frame.setVisible(true);
    }
}

