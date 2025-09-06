package sanghun.project.uncomfortablehub.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sanghun.project.uncomfortablehub.domain.Discomfort;

/** 불편함 엔티티의 데이터 영속성을 처리하는 리포지토리 */
@Repository
public interface DiscomfortRepository extends JpaRepository<Discomfort, Long> {

  /** 생성일시 기준으로 내림차순 정렬하여 모든 불편함을 조회합니다. */
  List<Discomfort> findAllByOrderByCreatedAtDesc();

  /** 특정 UUID로 작성된 불편함들을 조회합니다. */
  List<Discomfort> findByUuidOrderByCreatedAtDesc(String uuid);
}
