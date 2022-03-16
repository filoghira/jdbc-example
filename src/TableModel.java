import javax.swing.event.TableModelListener;
import java.util.Vector;

class TableModel implements javax.swing.table.TableModel {

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
