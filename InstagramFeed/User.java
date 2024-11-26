import java.util.ArrayList;

public class User {
    public String userId;

    // (user) list users following users;
    ArrayList<User> followList = new ArrayList<>();

    // (post) list users own posts;
    ArrayList<Post> ownPosts = new ArrayList<>();

    // (post) list viewed posts;
    ArrayList<Post> viewedPosts = new ArrayList<>();

    // (post) list liked posts;
    ArrayList<Post> likedPosts = new ArrayList<>();

    public User(String userId){
        this.userId = userId;
    }
}
