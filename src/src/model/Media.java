package model;

public abstract class Media {
    protected String title;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title;
    }
}
