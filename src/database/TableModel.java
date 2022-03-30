package database;

import javax.swing.event.TableModelListener;
import java.util.Vector;

class TableModel implements javax.swing.table.TableModel {

    private TableDescription table;
    private Vector<Vector<Object>> data;
    private String tableName;
    private Database db;

    public void setDb(Database db) {
        this.db = db;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setHeader(TableDescription table) {
        this.table = table;
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
        return table.getColumns().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return table.getColumns().get(columnIndex).name();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return data.get(0).get(columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return table.getColumns().get(columnIndex).editable();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        db.updateRow(tableName, rowIndex+1, table.getColumns().get(columnIndex), aValue.toString());
        data.set(rowIndex, db.getRow(tableName, rowIndex+1));
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
