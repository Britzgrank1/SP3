public abstract class Media {
    protected String title;
    protected int releaseYear;
    protected String category;
    protected double rating;


    public Media(String title, int releaseYear, String category, double rating) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.category = category;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public int getReleaseYear() { return releaseYear; }
    public String getCategory() { return category; }
    public double getRating() { return rating; }


    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                '}';
    }
}
