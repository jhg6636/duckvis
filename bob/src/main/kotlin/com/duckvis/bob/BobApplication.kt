package com.duckvis.bob

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableScheduling
@EnableRetry
@EnableJpaAuditing
class BobApplication {
  // 1. java process는 OS의 default TZ config를 따라가는데 배포 자동화 스크립트에서 OS TZ를 챙겨줘야한다.
  // 2. DB connection TZ sys TZ 다 챙겨줘야 한다. 여기서 바꾸면 JVM 옵션을 통채로 바꿔 같은 인스턴스 위에 있다는 다른 java process에 영향을 준다
  // 3. 이 옵션이 켜져 있을 경우 H2 날짜 오류가 발생
  // 타협안 -> 현재 cashmission에서는 dev & prod 만 옵션이 돌도록 설정 @ActiveProfile 찾아보세요!

  @PostConstruct
  private fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC.id))
    println("현재시각: ${LocalDateTime.now()}")
    // TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
  }
}

fun main(args: Array<String>) {
  runApplication<BobApplication>(*args)
}