package org.pandas.bambooclub.domain.board.repository;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.xml.stream.events.Comment;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<PostDetail.Comment, String> {
    List<PostDetail.Comment> findByUserId(String userId);

    PostDetail.Comment findByCommentId(String commentId);

    List<PostDetail.Comment> findByPostId(String postId);
}
