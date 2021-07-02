package com.duckvis.core.domain

import com.duckvis.core.utils.DateTimeMaker
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseDuckvisEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  open val id: Long = 0

) {
  @CreatedDate
  open var createdDateTime: LocalDateTime = DateTimeMaker.nowDateTime()

  @LastModifiedDate
  open var modifiedDateTime: LocalDateTime = DateTimeMaker.nowDateTime()
}