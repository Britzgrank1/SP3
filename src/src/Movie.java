public class Movie extends Media{
    private int duration;
    public Movie(String title, int releaseDate, String category, double rating, int duration) {
        super(title, releaseDate, category, rating);
        this.duration=duration;
    }
}
