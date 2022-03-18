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

    public void setDatabase(String dbPAth){
        this.database = new Database(dbPAth);
    }

    public Database getDatabase(){
        return database;
    }
}
