package judge.repository;

import judge.model.entity.Comment;
import judge.model.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Collection<Comment> findAllByHomework(Homework homework);
    Collection<Comment> findAllByAuthor_Id(String authorId);

    @Query("select count(c) from Comment c where c.homework=?1")
    long getCountByHomework(Homework homework);
}
