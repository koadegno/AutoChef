import java.sql.*;

public class Database {
    private String dbName;
    private Connection connection;
    private Statement request;

    public Database(String nameDB) {
        dbName = "jdbc:sqlite:" + nameDB;
        try {
            connection = DriverManager.getConnection(dbName);
            request  = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public boolean sendRequest(String req){
        try {
            return request.execute(req);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet sendQuery(String query){
        try {
            return request.executeQuery(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean creationTableFamilleAliment(){
        String req = "CREATE TABLE IF NOT EXISTS FamilleAliment (\n" +
                "    FamilleAlimentID INTEGER PRIMARY KEY,\n" +
                "    Nom CHAR(20) NOT NULL\n" +
                ");\n";
        return sendRequest(req);
    }

}
