public class Season extends Series{
    protected String title;
    protected int year;
    protected String category;

    public Season(String title, int year, String category, String title1, int year1, String category1) {
        super(title, year, category);
        this.title = title1;
        this.year = year1;
        this.category = category1;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }
}
