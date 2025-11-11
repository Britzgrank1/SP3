import util.TextUI;

public class Series extends Media implements Playable {
    private TextUI ui = new TextUI();

    public Series(String title) {
        super(title);
    }

    public void playMovie() {
        ui.displayMsg("Now streaming series: " + title);
    }
}
