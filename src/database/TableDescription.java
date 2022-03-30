package database;

import java.util.Map;
import java.util.Vector;

public class TableDescription {
    private final Vector<Column> columns;
    private final String tableName;

    public TableDescription(String tableName) {
        this.tableName = tableName;
        columns = new Vector<>();
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public Vector<Column> getColumns() {
        return columns;
    }

    public void setEditable(boolean[] editable) {
        if (editable.length != columns.size()) {
            throw new IllegalArgumentException("Editable array length does not match number of columns");
        }
        for (int i = 0; i < columns.size(); i++) {
            Column oldCol = columns.get(i);
            columns.set(i, new Column(oldCol.name(), oldCol.type(), editable[i]));
        }
    }

    public void setEditable(Map<String, Boolean> editable) {
        for (String colName : editable.keySet()) {
            for (Column col : columns) {
                if (col.name().equals(colName)) {
                    columns.set(columns.indexOf(col), new Column(col.name(), col.type(), editable.get(colName)));
                }
            }
        }
    }
}
