package Database;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Table {
    private final String name;
    private final TableDescription columns;
    private final Vector<Vector<Object>> rows;
    private TableModel model;

    public Table(String name) {
        this.name = name;
        columns = new TableDescription(name);
        rows = new Vector<>();
    }

    public void initHeader(ResultSet data) throws SQLException {
        // Get table header
        for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
            columns.addColumn(
                    new Column(
                            data.getMetaData().getColumnName(i),
                            JDBCType.valueOf(data.getMetaData().getColumnType(i)).getName(),
                            true
                    )
            );
        }
    }

    public void initRows(ResultSet data) throws SQLException {
        // Get table rows
        while (data.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= data.getMetaData().getColumnCount(); i++) {
                row.add(data.getObject(i));
            }
            rows.add(row);
        }
    }

    public void initModel(Database db) {
        model = new TableModel();
        model.setData(rows);
        model.setHeader(columns);
        model.setDb(db);
        model.setTableName(name);
    }

    public TableModel getModel() {
        return model;
    }

    public void init(ResultSet allFromTable, Database db) {
        try {
            initHeader(allFromTable);
            initRows(allFromTable);
            initModel(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
