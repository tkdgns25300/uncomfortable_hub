package sanghun.project.uncomfortablehub.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

  /** 특정 UUID가 작성한 모든 좋아요를 조회합니다. */
  List<Like> findByUuidOrderByCreatedAtDesc(String uuid);

  /** 특정 UUID가 작성한 좋아요를 페이징 조회합니다. */
  Page<Like> findByUuidOrderByCreatedAtDesc(String uuid, Pageable pageable);

  /** 특정 불편함의 모든 좋아요를 조회합니다. */
  List<Like> findByDiscomfortIdOrderByCreatedAtDesc(Long discomfortId);

  /** 특정 불편함의 좋아요를 페이징 조회합니다. */
  Page<Like> findByDiscomfortIdOrderByCreatedAtDesc(Long discomfortId, Pageable pageable);

  /** 특정 UUID가 작성한 좋아요 수를 조회합니다. */
  long countByUuid(String uuid);

  /** 특정 불편함의 좋아요를 모두 삭제합니다. */
  @Modifying
  @Transactional
  @Query("DELETE FROM Like l WHERE l.discomfortId = :discomfortId")
  void deleteByDiscomfortId(@Param("discomfortId") Long discomfortId);

  /** 특정 UUID의 모든 좋아요를 삭제합니다. */
  @Modifying
  @Transactional
  @Query("DELETE FROM Like l WHERE l.uuid = :uuid")
  void deleteByUuid(@Param("uuid") String uuid);

  /** 특정 불편함과 UUID로 좋아요를 삭제합니다. */
  @Modifying
  @Transactional
  @Query("DELETE FROM Like l WHERE l.discomfortId = :discomfortId AND l.uuid = :uuid")
  void deleteByDiscomfortIdAndUuid(
      @Param("discomfortId") Long discomfortId, @Param("uuid") String uuid);

  /** 가장 많은 좋아요를 받은 불편함 ID들을 조회합니다. */
  @Query(
      "SELECT l.discomfortId FROM Like l " + "GROUP BY l.discomfortId " + "ORDER BY COUNT(l) DESC")
  List<Long> findMostLikedDiscomfortIds(Pageable pageable);

  /** 특정 불편함들의 좋아요 수를 조회합니다. */
  @Query(
      "SELECT l.discomfortId, COUNT(l) FROM Like l "
          + "WHERE l.discomfortId IN :discomfortIds "
          + "GROUP BY l.discomfortId")
  List<Object[]> countByDiscomfortIds(@Param("discomfortIds") List<Long> discomfortIds);
}
