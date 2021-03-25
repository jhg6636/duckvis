package com.duckvis.nuguri.services

import com.duckvis.core.domain.User
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.utils.TimeHandler
import com.duckvis.nuguri.dtos.WorkTypeDto
import com.duckvis.nuguri.exceptions.AlreadyAttendedException
import com.duckvis.nuguri.exceptions.NotWorkingException
import com.duckvis.nuguri.domain.*
import com.duckvis.nuguri.services.Attendance
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class AttendanceServiceTest constructor(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val attendanceCardRepository: AttendanceCardRepository,
    @Autowired private val projectRepository: ProjectRepository,
    @Autowired private val attendance: Attendance,
) {
    lateinit var project1: Project
    lateinit var project2: Project

    @BeforeEach
    fun prepare() {
        project1 = projectRepository.save(Project("기본", "ㄱㅂ"))
        project2 = projectRepository.save(Project("기본아님", "ㄱㅂㅇㄴ"))
    }

    @AfterEach
    fun cleanUp() {
        attendanceCardRepository.deleteAll()
        userRepository.deleteAll()
        projectRepository.deleteAll()
    }

    @Test
    fun `현재 기본 프로젝트에 출근해 있는 사람이 기본 프로젝트로 출근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))
        val card = AttendanceCard(
            user.id,
            project1.id,
            CardType.WORK,
            TimeHandler.nowDateTime()
        )
        attendanceCardRepository.save(card)

        // when & then
        assertThrows<AlreadyAttendedException> {
            attendance.responseLogin(user.id, project1.id)
        }
    }

    @Test
    @Transactional
    fun `기본 프로젝트가 아닌 프로젝트에 출근해 있는 사람이 기본 프로젝트로 출근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))
        val card = AttendanceCard(
            user.id,
            project1.id,
            CardType.WORK,
            TimeHandler.nowDateTime()
        )
        val savedCard = attendanceCardRepository.save(card)

        // when
        val loginCard = attendance.responseLogin(user.id, project2.id)

        // then
        assertThat(savedCard.isNowWorking).isFalse
        assertThat(savedCard.durationSeconds).isNotNull
        assertThat(savedCard.projectId).isEqualTo(project1.id)
        assertThat(savedCard.type).isEqualTo(CardType.WORK)
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.projectId).isEqualTo(project2.id)
        assertThat(loginCard.isNowWorking).isTrue
        assertThat(loginCard.type).isEqualTo(CardType.WORK)
    }

    @Test
    fun `현재 출근해 있지 않은 사람이 출근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))

        // when
        val loginCard = attendance.responseLogin(user.id, project1.id)

        // then
        assertThat(loginCard.isNowWorking).isTrue
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.projectId).isEqualTo(project1.id)
        assertThat(loginCard.type).isEqualTo(CardType.WORK)
    }

    @Test
    @Transactional
    fun `옵션 없는 상태의 기본 프로젝트로 출근한 사람이 ㄱ %(야간,연장,휴일 중 1)을 입력한다`() {
        // given
        val user = userRepository.save(User("user", "user"))
        val card = AttendanceCard(
            user.id,
            projectRepository.findAll()[0].id,
            CardType.WORK,
            TimeHandler.nowDateTime()
        )
        val savedCard = attendanceCardRepository.save(card)

        // when
        val loginCard = attendance.responseLogin(user.id, savedCard.projectId, workTypeDto = WorkTypeDto(true))

        // then
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.isNowWorking).isTrue
        assertThat(loginCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(loginCard.isNight).isTrue
        assertThat(loginCard.type).isEqualTo(CardType.WORK)
        assertThat(savedCard.isNowWorking).isFalse
        assertThat(savedCard.type).isEqualTo(CardType.WORK)
    }

    @Test
    fun `현재 출근해 있는 사람이 퇴근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))
        val card = AttendanceCard(
            user.id,
            projectRepository.findAll()[0].id,
            CardType.WORK,
            TimeHandler.nowDateTime()
        )
        attendanceCardRepository.save(card)

        // when
        val logoutCard = attendance.responseLogout(user.id)

        // then
        assertThat(logoutCard.userId).isEqualTo(user.id)
        assertThat(logoutCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(logoutCard.isNowWorking).isFalse
        assertThat(logoutCard.type).isEqualTo(CardType.WORK)
    }

    @Test
    fun `현재 출근해 있지 않은 사람이 퇴근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))

        // when & then
        assertThrows<NotWorkingException> {
            attendance.responseLogout(user.id)
        }
    }

    @Test
    @Transactional
    fun `실수를 입력한다`() {
        // given
        val user = userRepository.save(User("user", "user"))

        // when
        val mistakeCard = attendance.responseMistake(user.id, projectRepository.findAll()[0].id, 10)

        // then
        assertThat(mistakeCard.durationSeconds).isEqualTo(600)
        assertThat(mistakeCard.isNowWorking).isFalse
        assertThat(mistakeCard.userId).isEqualTo(user.id)
        assertThat(mistakeCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(mistakeCard.type).isEqualTo(CardType.MISTAKE)

    }

    @Test
    @Transactional
    fun `3시간 일한 사람이 퇴근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))

        val loggedinCard = AttendanceCard(user.id, project1.id, CardType.WORK, TimeHandler.nowDateTime().minusHours(3))
        attendanceCardRepository.save(loggedinCard)

        // when
        val logoutCard = attendance.responseLogout(user.id)

        // then
        assertThat(logoutCard.isNowWorking).isFalse
        assertThat(logoutCard.durationSeconds).isEqualTo(10800)
        assertThat(logoutCard.projectId).isEqualTo(project1.id)
        assertThat(logoutCard.userId).isEqualTo(user.id)
    }
}