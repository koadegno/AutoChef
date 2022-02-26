import java.lang.constant.Constable;
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

    private Integer createAndGetIdShoppingList(String query){
        try {
            request.executeUpdate(query);
            ResultSet getKey = request.getGeneratedKeys();
            getKey.next();
            return getKey.getInt(1);
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

    public boolean creationTableShoppingList(){
        String req = "CREATE TABLE IF NOT EXISTS ListeCourse (\n" +
                "    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT " +
                ");\n";
        return sendRequest(req);
    }

    public Integer createIdShoppingList(){
        String req = "INSERT INTO ListeCourse values (null)";
        return createAndGetIdShoppingList(req);
    }


}
