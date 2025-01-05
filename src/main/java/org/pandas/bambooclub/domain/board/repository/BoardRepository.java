package org.pandas.bambooclub.domain.board.repository;

import org.pandas.bambooclub.domain.board.dto.PostDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<PostDetail, String> {
List<PostDetail> findByMbtiIn(List<String> mbtis);
List<PostDetail> findByUserId(String userId);
PostDetail findByPostId(String postId);

}
