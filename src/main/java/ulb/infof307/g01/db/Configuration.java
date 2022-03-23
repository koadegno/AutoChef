package ulb.infof307.g01.db;

public class Configuration {
    private static Configuration current;
    private Database database;

    private Configuration(){}

    public static Configuration getCurrent(){
        if(current == null){
            current = new Configuration();
        }
        return current;
    }

    public void setDatabase(String dbPath){
        this.database = new Database(dbPath);
    }

    public Database getDatabase(){
        return database;
    }
}
