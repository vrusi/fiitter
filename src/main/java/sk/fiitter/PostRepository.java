package sk.fiitter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.fiitter.model.Post;
import sk.fiitter.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE :user MEMBER OF p.user.followers OR p.user =:user ORDER BY p.dateCreated DESC ")
    List<Post> getHomeFeedByUser(User user);
}
