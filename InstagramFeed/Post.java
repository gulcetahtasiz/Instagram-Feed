public class Post implements Comparable<Post>{
    public String postId;
    public String authorId;
    public String content;
    public int likeCount = 0;

    public Post(String postId, String content){
        this.postId = postId;
        this.content = content;
    }

    // Method to compare two post objects based on their likeCount and postId
    public int compareTo(Post other) {
        // if likeCounts are different, return the one with bigger likeCount.
        if (this.likeCount != other.likeCount) {
            return Integer.compare(this.likeCount, other.likeCount);
        }
        // If likeCount is the same, compare postId lexicographically and return it.
        return this.postId.compareTo(other.postId);
    }
}
