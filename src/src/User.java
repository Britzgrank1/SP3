import util.TextUI;
import java.util.ArrayList;

public class User {
    protected String username;
    protected String password;
    ArrayList<String> seen = new ArrayList<>();
    protected ArrayList<String> favorite = new ArrayList<>();

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<String> favorite) {
        this.favorite = favorite;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getSeen() {
        return seen;
    }



    public void addSeen(String mediaTitle) {
        if (!seen.contains(mediaTitle)) {
            seen.add(mediaTitle);
        }
    }



    @Override
    public String toString() {
        return username + ", " + password;
    }
}



