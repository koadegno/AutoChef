package ulb.infof307.g01.model;

/**
 * Enumeration représentant les jours de la semaine
 */
public enum Day {
    Monday(0,"Lundi") ,
    Tuesday(1,"Mardi") ,
    Wednesday(2,"Mercredi"),
    Thursday(3,"Jeudi") ,
    Friday(4,"Vendredi") ,
    Saturday(5,"Samedi") ,
    Sunday(6,"Dimanche")    ;

    final int index;
    final String dayName;

    Day(int index,String dayName) {
        this.index = index;
        this.dayName = dayName;
    }

    public String toString() {return dayName;}
}
