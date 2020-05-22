package sk.fiitter;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.fiitter.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    //Post findByUsername(String username);
}
