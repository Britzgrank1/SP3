import util.TextUI;

public interface Media {
    String getTitle();
    //int getYear();
    //String getCategory();
    TextUI ui = new TextUI();

     private void playMovie() {
       ui.displayMsg("Playing " +getTitle() + "...");
    }

}
