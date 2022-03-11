package ulb.infof307.g01.cuisine;

import javafx.util.Pair;

import java.time.LocalDate;

public class TempMenu {
    //private Pair<String, String> duration;
    public LocalDate[] duration; //apparement String[] plus efficace que Pair, à vérifier?
    public int numOfDays;
    public String name;
    public String[][] menuContent;
    //menuContent[day][recipes];


    public TempMenu(String name, LocalDate[] duration, int numOfDays){
        this.duration =  duration;
        this.name = name;
        this.numOfDays = numOfDays;
    }

    public String getName(){return name;}
}
