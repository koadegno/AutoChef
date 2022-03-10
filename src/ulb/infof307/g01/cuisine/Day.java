package ulb.infof307.g01.cuisine;

public enum Day {
    Monday(0), Tuesday(1), Wednesday(2),
    Thursday(3), Friday(4), Saturday(5), Sunday(6);

    final int index;

    Day(int index) {
        this.index = index;
    }
}
