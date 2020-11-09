package judge.repository;

import judge.model.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, String> {
    Optional<Homework> findFirstByAuthor_IdAndExercise_Name (String authorId, String name);
    Optional<Homework> findFirstByAuthor_IdOrderByAddedOnDesc(String authorId);
    Collection<Homework> findAllByGit(String git);
    Collection<Homework> findAllByAuthor_Id(String authorId);
    List<Homework> findAllByExercise_Id(String exerciseId);
}
