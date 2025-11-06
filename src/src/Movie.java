public class Movie implements Media{
    private String title;
    private int year;
    private String category;


    public Movie(String title, int year, String category) {
        this.title = title;
        this.year = year;
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public String getCategory() {

        return category;
    }

    public String getTitle() {

        return title;
    }
}
