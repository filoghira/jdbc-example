import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws SQLException {

        Database db = new Database("localhost", "3306", "scuola", "scuola", "fastfood");

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

        MyModel model = new MyModel();
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

class MyModel implements TableModel {

    private Vector<String> header;
    private Vector<Vector<Object>> data;

    public void setHeader(Vector<String> header) {
        this.header = header;
    }

    public void setData(Vector<Vector<Object>> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return header.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return data.get(0).get(columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}