public class Series implements Media{
    protected String title;
    protected int year;
    protected String category;

    public Series(String title, int year, String category) {
        this.title = title;
        this.year = year;
        this.category = category;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public int getYear() {
        return year;
    }
}
