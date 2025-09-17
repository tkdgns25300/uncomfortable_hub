package sanghun.project.uncomfortablehub.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sanghun.project.uncomfortablehub.domain.Discomfort;

/** 불편함 엔티티의 데이터 영속성을 처리하는 리포지토리 */
@Repository
public interface DiscomfortRepository extends JpaRepository<Discomfort, Long> {

  /** 생성일시 기준으로 내림차순 정렬하여 모든 불편함을 조회합니다. */
  List<Discomfort> findAllByOrderByCreatedAtDesc();

  /** 좋아요 많은순 + 최신순으로 모든 불편함을 조회합니다. */
  @Query(
      "SELECT d FROM Discomfort d LEFT JOIN Like l ON d.id = l.discomfortId "
          + "GROUP BY d.id "
          + "ORDER BY COUNT(l.id) DESC, d.createdAt DESC")
  List<Discomfort> findAllOrderByLikeCountDescCreatedAtDesc();

  /** 생성일시 기준으로 내림차순 정렬하여 불편함을 페이징 조회합니다. */
  Page<Discomfort> findAllByOrderByCreatedAtDesc(Pageable pageable);

  /** 특정 UUID로 작성된 불편함들을 조회합니다. */
  List<Discomfort> findByUuidOrderByCreatedAtDesc(String uuid);

  /** 특정 UUID로 작성된 불편함을 페이징 조회합니다. */
  Page<Discomfort> findByUuidOrderByCreatedAtDesc(String uuid, Pageable pageable);

  /** 제목에 키워드가 포함된 불편함을 조회합니다. */
  List<Discomfort> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

  /** 설명에 키워드가 포함된 불편함을 조회합니다. */
  List<Discomfort> findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

  /** 제목 또는 설명에 키워드가 포함된 불편함을 조회합니다. */
  @Query(
      "SELECT d FROM Discomfort d WHERE "
          + "LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
          + "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) "
          + "ORDER BY d.createdAt DESC")
  List<Discomfort> findByTitleOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword);

  /** 특정 UUID가 작성한 불편함 수를 조회합니다. */
  long countByUuid(String uuid);

  /** 특정 불편함이 존재하는지 확인합니다. */
  boolean existsByIdAndUuid(Long id, String uuid);
}
