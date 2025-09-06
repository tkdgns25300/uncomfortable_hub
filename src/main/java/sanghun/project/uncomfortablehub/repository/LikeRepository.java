package sanghun.project.uncomfortablehub.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sanghun.project.uncomfortablehub.domain.Like;

/** 좋아요 엔티티의 데이터 영속성을 처리하는 리포지토리 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  /** 특정 불편함과 UUID로 좋아요를 조회합니다. */
  Optional<Like> findByDiscomfortIdAndUuid(Long discomfortId, String uuid);

  /** 특정 불편함의 좋아요 수를 조회합니다. */
  @Query("SELECT COUNT(l) FROM Like l WHERE l.discomfortId = :discomfortId")
  long countByDiscomfortId(@Param("discomfortId") Long discomfortId);

  /** 특정 불편함에 대한 좋아요 존재 여부를 확인합니다. */
  boolean existsByDiscomfortIdAndUuid(Long discomfortId, String uuid);
}
