import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


// GÜLCE TAHTASIZ

public class Main {
    // INPUT READING / OUTPUT WRITING
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0])); // input file name
             BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]))) { // output file name

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String command = parts[0];

                //create_user
                if (command.equals("create_user")) {
                    String userId = parts[1];
                    String result = create_user(userId);
                    bw.write(result + "\n");
                }

                //follow_user
                if (command.equals("follow_user")) {
                    String userId1 = parts[1];
                    String userId2 = parts[2];
                    String result = follow_user(userId1,userId2);
                    bw.write(result + "\n");
                }

                //unfollow_user
                if (command.equals("unfollow_user")) {
                    String userId1 = parts[1];
                    String userId2 = parts[2];
                    String result = unfollow_user(userId1,userId2);
                    bw.write(result + "\n");
                }

                //create_post
                if (command.equals("create_post")) {
                    String userId = parts[1];
                    String postId = parts[2];
                    String content = parts[3];
                    String result = create_post(userId,postId,content);
                    bw.write(result + "\n");
                }

                //see_post
                if (command.equals("see_post")) {
                    String userId = parts[1];
                    String postId = parts[2];
                    String result = see_post(userId,postId);
                    bw.write(result + "\n");
                }

                //see_all_posts_from_user
                if (command.equals("see_all_posts_from_user")) {
                    String userId1 = parts[1];
                    String userId2 = parts[2];
                    String result = see_all_posts_from_user(userId1,userId2);
                    bw.write(result + "\n");
                }

                //toggle_like
                if (command.equals("toggle_like")) {
                    String userId = parts[1];
                    String postId = parts[2];
                    String result = toggle_like(userId,postId);
                    bw.write(result + "\n");
                }

                //generate_feed
                if (command.equals("generate_feed")) {
                    String userId = parts[1];
                    int num = Integer.parseInt(parts[2]);
                    String result = generate_feed(userId,num);
                    bw.write(result + "\n");
                }

                //scroll_through_feed
                if (command.equals("scroll_through_feed")){
                    String userId = parts[1];
                    int num = Integer.parseInt(parts[2]);
                    int[] likeOrNot = new int[num];
                    for (int i = 0; i < num; i++){
                        likeOrNot[i] = Integer.parseInt(parts[3+i]);
                    }
                    String result = scroll_through_feed(userId, num, likeOrNot);
                    bw.write(result + "\n");
                }

                //sort_posts
                if (command.equals("sort_posts")) {
                    String userId = parts[1];
                    ArrayList<Post> sortedPosts = sort_post(userId);

                    if (sortedPosts == null) {
                        bw.write("Some error occurred in sort_posts.\n");
                    } else if (sortedPosts.isEmpty()) {
                        bw.write("No posts from " + userId + ".\n");
                    } else {
                        bw.write("Sorting " + userId + "'s posts:\n");
                        for (Post sortedPost : sortedPosts) {
                            bw.write(sortedPost.postId + ", Likes: " + sortedPost.likeCount + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("File reading/writing error: " + e.getMessage());
        }
    }


    // the hashMap that stores the user objects with their userId key.
    private  static MyHashMap<String, User> allUsers = new MyHashMap<>();

    // the hashMap that stores the post objects with their postId key.
    public static MyHashMap<String, Post> allPosts = new MyHashMap<>();


    // create_user
    // returns the String output messages of the actions.
    public static String create_user(String userId){
        // Check if the user doesn't exist
        if (allUsers.containsKey(userId)) {
            return "Some error occurred in create_user.";
        }
        // Create new user and adds it to the user hashmap with its Id.
        else {
            User newUser = new User(userId);
            allUsers.addtoHash(userId, newUser);
            return "Created user with Id " + userId + ".";
        }
    }


    // follow_user
    // returns the String output messages of the actions.
    public static String follow_user(String userId1,String userId2){
        // Retrieve the user objects from the hashmap using their Ids.
        User user1 = allUsers.getValueFromKey(userId1);
        User user2 = allUsers.getValueFromKey(userId2);

        // Check if user1 or user2 doesn't exist or if they are the same user.
        if (user1 == null || user2 == null || user1 == user2) {
            return "Some error occurred in follow_user.";
        }

        // Check if user2 is already in user1's follow list.
        if (user1.followList.contains(user2)) {
            return "Some error occurred in follow_user.";
        }

        // If all conditions are met, add user2 to user1's following list.
        else{
            user1.followList.add(user2);
            return userId1 + " followed " + userId2 + ".";
        }
    }


    // unfollow_user
    // returns the String output messages of the actions.
    public static String unfollow_user(String userId1,String userId2){
        // Retrieve the user objects from the hashmap using their Ids.
        User user1 = allUsers.getValueFromKey(userId1);
        User user2 = allUsers.getValueFromKey(userId2);

        // Check if user1 or user2 doesn't exist or if they are the same user.
        if (user1 == null || user2 == null || user1 == user2) {
            return "Some error occurred in unfollow_user.";
        }

        // Check if user2 is not in user1's follow list.
        if (!user1.followList.contains(user2)) {
            return "Some error occurred in unfollow_user.";
        }

        // If all conditions are met, remove user2 to user1's following list.
        else{
            user1.followList.remove(user2);
            return userId1 + " unfollowed " + userId2 + ".";
        }
    }


    // create_post
    // returns the String output messages of the actions.
    public static String create_post(String userId, String postId, String content){
        // Retrieve the user object from the hashmap using its Id.
        User user = allUsers.getValueFromKey(userId);

        // Check if userId doesn't exist.
        if (user == null) {
            return "Some error occurred in create_post.";
        }

        // Check if post with <postId> is already present.
        if(allPosts.containsKey(postId)){
            return "Some error occurred in create_post.";
        }

        // Add post to user's ownPosts list and allPosts hashmap.
        Post newPost = new Post(postId,content);
        newPost.authorId = userId;
        user.ownPosts.add(newPost);
        allPosts.addtoHash(postId, newPost);
        return userId + " created a post with Id " + postId + ".";

    }


    // see_post
    // returns the String output messages of the actions.
    public static String see_post(String userId, String postId){
        // Retrieve the user and post objects from the hashmaps using their Ids.
        User user = allUsers.getValueFromKey(userId);
        Post post = allPosts.getValueFromKey(postId);

        // Check if user or post doesn't exist.
        if(user==null || post==null){
            return "Some error occurred in see_post.";
        }

        // If all conditions are met, add post <postId> to users viewed posts list.
        else{
            user.viewedPosts.add(post);
            return userId + " saw " + postId + ".";
        }
    }


    // see_all_posts_from_user
    // returns the String output messages of the actions.
    public static String see_all_posts_from_user(String userId1, String userId2){
        // Retrieve the user objects from the hashmap using their Ids.
        User user1 = allUsers.getValueFromKey(userId1);
        User user2 = allUsers.getValueFromKey(userId2);

        // Check if user1 or user2 doesn't exist or if they are the same user.
        if (user1 == null || user2 == null) {
            return "Some error occurred in see_all_posts_from_user.";
        }

        // If all conditions are met, add all posts from userId2's own post list to the userId1's viewed post list,
        else{
            for (Post post : user2.ownPosts) {
                if (!user1.viewedPosts.contains(post)) {
                    user1.viewedPosts.add(post);
                }
            }
            return userId1 + " saw all posts of " + userId2 + ".";
        }
    }


    // toggle_like
    // returns the String output messages of the actions.
    public static String toggle_like(String userId, String postId){
        // Retrieve the user and post objects from the hashmaps using their Ids.
        User user = allUsers.getValueFromKey(userId);
        Post post =allPosts.getValueFromKey(postId);

        // Check if user or post doesn't exist.
        if(user==null || post == null){
            return "Some error occurred in toggle_like.";
        }

        // if post is not already in the users viewed post list, add it to the list.
        if (!user.viewedPosts.contains(post)) {
                user.viewedPosts.add(post);
        }

        // if post is not in the users liked post list, add it.
        // Since given post is liked, increase the like count of this post by one.
        if (!user.likedPosts.contains(post)) {
            user.likedPosts.add(post);
            post.likeCount += 1;
            return userId + " liked " + postId + ".";
        }

        // if post is already in the userIds liked post list,remove post to users liked post list.
        // Since given post is liked, decrease the like count of this post by one.
        else {
            user.likedPosts.remove(post);
            post.likeCount -= 1;
            return userId + " unliked " + postId + ".";
        }
    }


    // generate_feed
    // returns the String output messages of the actions.
    public static String generate_feed(String userId, int num) {
        // Retrieve the user object from the hashmap using its Id.
        User user = allUsers.getValueFromKey(userId);

        // Check if user does not exist.
        if (user == null) {
            return "Some error occurred in generate_feed.";
        }

        // Get the unsorted feed for the user.
        ArrayList<Post> feed = getFeed(user);

        // Initialize a maximum binary heap to sort the feed's posts by their like count.
        MaxBinaryHeap heap = new MaxBinaryHeap(feed.size());
        for (int i = 0; i < feed.size(); i++) {
            heap.array[i + 1] = feed.get(i);
        }
        heap.currentSize = feed.size();
        heap.buildHeap();

        // Initialize variables for feed generation.
        int count = 0; // Tracks the number of posts added to the feed.
        String outputString = "Feed for " + userId + ":\n";

        // Got posts from the heap until the required number is reached or the heap is empty.
        while (!heap.isEmpty() && num > count) {
            Post topPost = heap.deleteMax();
            outputString += "Post ID: " + topPost.postId + ", Author: " + topPost.authorId + ", Likes: " + topPost.likeCount + "\n";
            count++;
        }

        // Check if there are fewer posts than requested.
        if (num > count) {
            outputString += "No more posts available for " + userId + ".";
        }
        return outputString.trim();
    }


    // scroll_through_feed
    // returns the String output messages of the actions.
    public static String scroll_through_feed(String userId, int num, int[] likeOrNot) {
        // Retrieve the user object from the hashmap using its Id.
        User user = allUsers.getValueFromKey(userId);

        // Check if user does not exist.
        if (user == null){
            return "Some error occurred in scroll_through_feed.";
        }

        // Get the unsorted feed for the user.
        ArrayList<Post> feed = getFeed(user);

        // Initialize a maximum binary heap to sort the feed's posts by their like count.
        MaxBinaryHeap heap = new MaxBinaryHeap(feed.size());
        for (int i = 0; i < feed.size(); i++) {
            heap.array[i + 1] = feed.get(i);
        }
        heap.currentSize = feed.size();
        heap.buildHeap();

        // Initialize variables for feed generation.
        int i = 0; // Tracks the index of likeOrNot array.
        String outputString = userId + " is scrolling through feed:\n";

        // Got posts from the heap until the required number is reached or the heap is empty.
        while (num > i && !heap.isEmpty()) {
            // Get the post with the most like count.
            Post currentPost = heap.deleteMax();

            // seeing the post.
            if (likeOrNot[i] == 0) {
                // if the user does not viewed the post before, add it to the its viewedPosts list.
                if (!user.viewedPosts.contains(currentPost)) {
                    user.viewedPosts.add(currentPost);
                    outputString += userId + " saw " + currentPost.postId  + " while scrolling.\n";
                }
            }
            // seeing and liking the post.
            else if (likeOrNot[i] == 1) {
                // if the user does not viewed and liked the post before, add it to the its viewedPosts and likedPosts lists.
                if (!user.viewedPosts.contains(currentPost) && !user.likedPosts.contains(currentPost)) {
                    user.viewedPosts.add(currentPost);
                    user.likedPosts.add(currentPost);
                    currentPost.likeCount++;
                    outputString += userId + " saw " + currentPost.postId + " while scrolling and clicked the like button.\n";
                }
            }
            i++;
        }
        // if there are no post left to fill the num number,
        if (i < num) {
            outputString += "No more posts in feed.";
        }
        return outputString.trim();
    }


    // sort_post
    // returns the like sorted ArrayList of given users own posts.
    public static ArrayList<Post> sort_post(String userId) {
        User user = allUsers.getValueFromKey(userId);

        // Check if user does not exist.
        if (user == null) {
            return null;
        }

        // Get the users ownPosts list
        ArrayList<Post> usersOwnPosts = user.ownPosts;

        // If the user's post list is null or empty, return an empty list.
        if (usersOwnPosts == null || usersOwnPosts.isEmpty()) {
            return new ArrayList<>();
        }

        // Initialize a maximum binary heap to sort the feed's posts by their like count.
        MaxBinaryHeap heap = new MaxBinaryHeap(usersOwnPosts.size());
        for (int i = 0; i < usersOwnPosts.size(); i++) {
            heap.array[i + 1] = usersOwnPosts.get(i);
        }
        heap.currentSize = usersOwnPosts.size();
        heap.buildHeap();

        // Got posts from the heap from maximum to minimum and store them in the sorted list.
        ArrayList<Post> sortedPosts = new ArrayList<>();
        while (!heap.isEmpty()) {
            sortedPosts.add(heap.deleteMax());
        }
        return sortedPosts;
    }


    // The method for helping the generate_feed and scroll_through_feed methods.
    // Get the user and return the post feed ArrayList that meets the conditions.
    private static ArrayList<Post> getFeed(User user) {
        // Initialize an ArrayList to store the user's feed
        ArrayList<Post> feed = new ArrayList<>();

        // Iterate through the users that the given user is following.
        for (User followingUsers : user.followList) {
            // Iterate through each followed user's posts.
            for (Post feedPosts : followingUsers.ownPosts) {
                // Check if the post has not been viewed by the user. if not, add the post to the feed.
                if (!user.viewedPosts.contains(feedPosts)) {
                    feed.add(feedPosts);
                }
            }
        }
        return feed;
    }
}