package sanghun.project.uncomfortablehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 불편함 관련 비즈니스 로직을 처리하는 서비스 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiscomfortService {

  /** 불편함을 등록합니다. */
  @Transactional
  public void createDiscomfort() {
    log.info("Creating new discomfort");
    // TODO: 구현 예정
  }

  /** 모든 불편함을 조회합니다. */
  public void findAllDiscomforts() {
    log.info("Finding all discomforts");
    // TODO: 구현 예정
  }

  /** ID로 불편함을 조회합니다. */
  public void findDiscomfortById(Long id) {
    log.info("Finding discomfort by id: {}", id);
    // TODO: 구현 예정
  }

  /** 불편함을 수정합니다. */
  @Transactional
  public void updateDiscomfort(Long id) {
    log.info("Updating discomfort with id: {}", id);
    // TODO: 구현 예정
  }

  /** 불편함을 삭제합니다. */
  @Transactional
  public void deleteDiscomfort(Long id) {
    log.info("Deleting discomfort with id: {}", id);
    // TODO: 구현 예정
  }
}
