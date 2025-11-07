import util.TextUI;

public abstract class Media {
    private String title;
    private int releaseDate;
    private String category;
    private double rating;

    public Media(String title, int releaseDate, String category, double rating) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.rating = rating;
    }

    private void play() {

    }

}
