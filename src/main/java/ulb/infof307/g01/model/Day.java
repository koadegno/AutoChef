package ulb.infof307.g01.model;

/** Enumeration représentant les jours de la semaine
*/
public enum Day {
    Monday(0)    {@Override public String toString() {return "Lundi";}},
    Tuesday(1)   {@Override public String toString() {return "Mardi";}},
    Wednesday(2) {@Override public String toString() {return "Mercredi";}},
    Thursday(3)  {@Override public String toString() {return "Jeudi";}},
    Friday(4)    {@Override public String toString() {return "Vendredi";}},
    Saturday(5)  {@Override public String toString() {return "Samedi";}},
    Sunday(6)    {@Override public String toString() {return "Dimanche";}};

    final int index;

    Day(int index) {
        this.index = index;
    }
    public int getIndex(){return index;}
}
