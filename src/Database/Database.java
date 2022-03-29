package Database;

import java.sql.*;
import java.util.Vector;

public class Database {

    static final String protocol = "jdbc:mariadb://";
    static final String driverName = "org.mariadb.jdbc.Driver";

    private Connection connection = null;

    public Database(String address, String port, String userName, String password, String databaseName)
    {

        loadDatabaseDriver();

        try{
            connection = DriverManager.getConnection(protocol + address + ":" + port + "/" + databaseName, userName, password);
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }

    }

    private void loadDatabaseDriver()
    {
        // Load the Java DB driver
        try {
            Class.forName(Database.driverName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create an SQL table. By default, there's an Identity Database.Column
     * @param name Name of the table
     * @param col String matrix. Each row contains the name of the column and the data type
     */
    public void addTable(String name, Column[] col){

        // Prepare the query
        StringBuilder query = new StringBuilder("CREATE TABLE " + name + " ("
                + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1), ");

        // Run through the list of columns and update the query
        int i=0;
        while(i < col.length){
            query.append(col[i].name()).append(" ").append(col[i].type()).append(", ");
            i++;
        }

        query.append("PRIMARY KEY(Id))");

        // Create the statement
        PreparedStatement statement;
        try {
            // Execute the query and close it
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            System.out.println("Executing query:\n" + query);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

    /**
     * Delete a table from the database
     * @param name Name of the table
     */
    public void deleteTable(String name){
        // Prepare the query
        StringBuilder query = new StringBuilder("DROP TABLE " + name);

        // Prepare the statement
        PreparedStatement statement;

        try {
            // Execute the statement and get as result the ID of the item that has just been added
            System.out.println("Executing query:\n" + query);
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            statement.executeUpdate();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

    /**
     * Insert a row in a table
     * @param tableName Name of the table
     * @param values String matrix. Each row contains the name of the column and the data.
     * @return Returns the ID of the row
     */
    public int addRow(String tableName, String[][] values){
        // Prepare the query
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder partialQuery = new StringBuilder(" (");

        // Run through the values
        int i=0;
        while(i < values.length){

            String val;

            if(values[i][2].contains("INT"))
                val = values[i][1];
            else
                val = SQLUtils.quote(values[i][1]);

            // Build two different strings, one for the columns and the other for the values
            query.append(values[i][0]);
            partialQuery.append(val);

            if(i < values.length - 1) {
                query.append(", ");
                partialQuery.append(", ");
            }else{
                query.append(") VALUES");
                partialQuery.append(")");
                break;
            }

            i++;
        }

        // Concatenate the strings
        query.append(partialQuery);
        // Prepare the statement
        PreparedStatement statement;

        try {
            // Execute the statement and get as result the ID of the item that has just been added
            System.out.println("Executing query:\n" + query);
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int out = rs.getInt(1);
            statement.close();
            return out;
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
        return -1;
    }

    /**
     * Delete a row in a table by a row's id
     * @param tableName Name of the table
     * @param id ID of the row
     */
    public void deleteRow(String tableName, int id){
        // Prepare the query
        StringBuilder query = new StringBuilder("DELETE FROM " + tableName + " WHERE ID="+id);

        // Prepare the statement
        PreparedStatement statement;

        try {
            // Execute the statement and get as result the ID of the item that has just been added
            System.out.println("Executing query:\n" + query);
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            statement.executeUpdate();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

    public void updateRow(String tableName, int id, Column parameter, String value){
        // Prepare the query
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET " + parameter.name() + "=");

        if(parameter.type().equals("VARCHAR"))
            query.append(SQLUtils.quote(value));
        else
            query.append(value);

        query.append(" WHERE ID=").append(id);

        // Prepare the statement
        PreparedStatement statement;
        try {
            // Execute the statement and get as result the ID of the item that has just been added
            System.out.println("Executing query:\n" + query);
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            statement.executeUpdate();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

    public void renameTable(String oldName, String newName){
        // Prepare the query
        StringBuilder query = new StringBuilder("RENAME TABLE " + oldName + " TO " + newName);

        // Prepare the statement
        PreparedStatement statement;
        try {
            // Execute the statement and get as result the ID of the item that has just been added
            System.out.println("Executing query:\n" + query);
            statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS );
            statement.executeUpdate();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

    /**
     *
     * @param tableName Name of the table
     * @return A resultSet containing all the data in the table
     */
    public ResultSet getAllFromTable(String tableName){
        String query = "SELECT * FROM " + tableName;

        // Create the statement
        Statement state;

        // Execute the query
        try {
            state = connection.createStatement();
            return state.executeQuery(query);
        }catch(SQLException e){
            SQLUtils.printSQLException(e);
        }

        return null;
    }

    public Vector<Object> getRow(String tableName, int id) {
        String query = "SELECT * FROM " + tableName + " WHERE ID=" + id;

        // Create the statement
        Statement state;

        // Execute the query
        try {
            state = connection.createStatement();
            ResultSet rs = state.executeQuery(query);
            Vector<Object> out = new Vector<>();

            while(rs.next()){
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                    out.add(rs.getObject(i));
                }
            }

            return out;
        }catch(SQLException e){
            SQLUtils.printSQLException(e);
        }

        return null;
    }

        /**
     * Close the connection of the database
     */
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            SQLUtils.printSQLException(e);
        }
    }

}