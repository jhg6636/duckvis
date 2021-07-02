package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.dtos.nuguri.Mistake
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.CardType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.StartAndEndTime
import com.duckvis.core.utils.dateTimeString
import com.duckvis.core.utils.secondsToString
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.UniqueConstraint

// TODO 실수의 경우 login null이면 좋지 않을까 - 통계 처리 이제 date 기준으로 하면 되니까

@Entity
class AttendanceCard(
  val userId: Long,
  val projectId: Long,
  val subProjectId: Long?,

  @Enumerated(EnumType.STRING)
  val type: CardType,

//  val aDate: LocalDate, // THINKING 한국기준 날짜 (6시 아무튼 다 고려)
  // 날짜 7일 가져와 between 05-01 - 06:00:00 and 05-31 + plusDays(1) + 06:00:00

  val loginDateTime: LocalDateTime,
  var durationSeconds: Int? = null,
  val isNight: Boolean,
  val isHoliday: Boolean,
  val isExtended: Boolean,
  var logoutDateTime: LocalDateTime? = null
) : BaseDuckvisEntity() {

  // 출근을 할 떄 카드를 만들 때
  constructor(request: Work) : this(
    userId = request.userId,
    projectId = request.projectId,
    subProjectId = request.subProjectId,
    type = CardType.WORK,
    loginDateTime = DateTimeMaker.nowDateTime(),
    isNight = request.workTypeDto.isNight,
    isHoliday = request.workTypeDto.isHoliday,
    isExtended = request.workTypeDto.isExtended
  )

  // for test
  constructor(request: Work, loginDateTime: LocalDateTime) : this(
    userId = request.userId,
    projectId = request.projectId,
    subProjectId = request.subProjectId,
    type = CardType.WORK,
    loginDateTime = loginDateTime,
    isNight = request.workTypeDto.isNight,
    isHoliday = request.workTypeDto.isHoliday,
    isExtended = request.workTypeDto.isExtended
  )

  // for test
  constructor(request: Work, loginDateTime: LocalDateTime, durationSeconds: Int, logoutDateTime: LocalDateTime) : this(
    userId = request.userId,
    projectId = request.projectId,
    subProjectId = request.subProjectId,
    type = CardType.WORK,
    loginDateTime = loginDateTime,
    isNight = request.workTypeDto.isNight,
    isHoliday = request.workTypeDto.isHoliday,
    isExtended = request.workTypeDto.isExtended,
    durationSeconds = durationSeconds,
    logoutDateTime = logoutDateTime
  )

  constructor(request: Mistake, loginDateTime: LocalDateTime) : this(
    userId = request.userId,
    projectId = request.projectId,
    subProjectId = request.subProjectId,
    type = CardType.MISTAKE,
    loginDateTime = loginDateTime,
    durationSeconds = request.durationSeconds,
    isNight = request.workTypeDto.isNight,
    isHoliday = request.workTypeDto.isHoliday,
    isExtended = request.workTypeDto.isExtended
  )

  val date: LocalDate = if (loginDateTime.hour < 21) {
    loginDateTime.toLocalDate()
  } else {
    loginDateTime.toLocalDate().plusDays(1)
  }

  val nowWorkingSeconds: Int
    get() = Duration.between(this.loginDateTime, DateTimeMaker.nowDateTime()).toSeconds().toInt()

  val isNowWorking: Boolean
    get() = this.type == CardType.WORK && this.durationSeconds == null && this.logoutDateTime == null

  val isWorkCard: Boolean
    get() = this.type == CardType.WORK

  val backUpCsvString: String
    get() = "$userId,$projectId,$subProjectId,$type,${loginDateTime.dateTimeString},${logoutDateTime.dateTimeString},$durationString,$isNight,$isHoliday,$isExtended"

  val durationString: String
    get() = durationSeconds.secondsToString

  val coreTimeDuration: Long
    get() {
      if (loginDateTime >= StartAndEndTime.coreTimeEnd || (logoutDateTime != null && logoutDateTime!! <= StartAndEndTime.coreTimeStart)) {
        return 0
      }

      val from = maxOf(loginDateTime, StartAndEndTime.coreTimeStart)
      val to = if (logoutDateTime == null) {
        StartAndEndTime.coreTimeEnd
      } else {
        minOf(StartAndEndTime.coreTimeEnd, logoutDateTime!!)
      }

      return Duration.between(from, to).seconds
    }

  fun logOut(): Boolean {
    val now = DateTimeMaker.nowDateTime()
    this.logoutDateTime = now
    val duration = Duration.between(this.loginDateTime, now)
    this.durationSeconds = duration.toSeconds().toInt()
    return durationSeconds!! >= 12 * 3600
  }

  fun checkAlreadyWork(work: Work) {
    if (this.projectId == work.projectId &&
      this.isNight == work.workTypeDto.isNight &&
      this.isExtended == work.workTypeDto.isExtended &&
      this.isHoliday == work.workTypeDto.isHoliday &&
      this.subProjectId == work.subProjectId
    )
      throw NuguriException(ExceptionType.ALREADY_ATTENDED)
  }

  fun isMyCard(userId: Long): Boolean {
    return this.userId == userId
  }

  fun isSameWorkType(workTypeDto: WorkTypeDto): Boolean {
    return workTypeDto == WorkTypeDto(this.isNight, this.isExtended, this.isHoliday)
  }

}