package sk.fiitter.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity()
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "likedPosts")
    Set<User> likes;

    // constructors
    public Post() {

    }

    // getters setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    // methods
    public boolean isLikedBy(User user) {
        return likes.stream().anyMatch(like -> like.getUsername().equals(user.getUsername()));
    }
}
