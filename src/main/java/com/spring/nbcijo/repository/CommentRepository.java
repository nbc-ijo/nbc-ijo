package com.spring.nbcijo.repository;

import com.spring.nbcijo.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    List<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
