package com.catshi.nuguri.services

import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.domain.*
import com.catshi.nuguri.exceptions.AlreadyAttendedException
import com.catshi.nuguri.exceptions.NotWorkingException
import com.catshi.nuguri.types.AttendanceOption
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

    @BeforeEach
    fun prepare() {
        val project = projectRepository.save(Project("기본", "ㄱㅂ", 1))
        val project2 = projectRepository.save(Project("기본아님", "ㄱㅂㅇㄴ", 1))
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
            projectRepository.findAll()[0].id,
            CardType.WORK,
            AttendanceOption.NORMAL,
            TimeHandler.nowDateTime()
        )
        attendanceCardRepository.save(card)

        // when & then
        assertThrows<AlreadyAttendedException> {
            attendance.responseLogin(user.id, projectRepository.findAll()[0].id)
        }
    }

    @Test
    @Transactional
    fun `기본 프로젝트가 아닌 프로젝트에 출근해 있는 사람이 기본 프로젝트로 출근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))
        val card = AttendanceCard(
            user.id,
            projectRepository.findAll()[0].id,
            CardType.WORK,
            AttendanceOption.NORMAL,
            TimeHandler.nowDateTime()
        )
        val savedCard = attendanceCardRepository.save(card)

        // when
        val loginCard = attendance.responseLogin(user.id, projectRepository.findAll()[1].id)

        // then
        assertThat(savedCard.logoutDateTime).isNotNull
        assertThat(savedCard.durationSeconds).isNotNull
        assertThat(savedCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(savedCard.option).isEqualTo(AttendanceOption.NORMAL)
        assertThat(savedCard.type).isEqualTo(CardType.WORK)
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.projectId).isEqualTo(projectRepository.findAll()[1].id)
        assertThat(loginCard.logoutDateTime).isNull()
        assertThat(loginCard.option).isEqualTo(AttendanceOption.NORMAL)
        assertThat(loginCard.type).isEqualTo(CardType.WORK)
    }

    @Test
    fun `현재 출근해 있지 않은 사람이 출근한다`() {
        // given
        val user = userRepository.save(User("user", "user"))

        // when
        val loginCard = attendance.responseLogin(user.id, projectRepository.findAll()[0].id)

        // then
        assertThat(loginCard.logoutDateTime).isNull()
        assertThat(loginCard.durationSeconds).isNull()
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(loginCard.option).isEqualTo(AttendanceOption.NORMAL)
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
            AttendanceOption.NORMAL,
            TimeHandler.nowDateTime()
        )
        val savedCard = attendanceCardRepository.save(card)

        // when
        val loginCard = attendance.responseLogin(user.id, savedCard.projectId, AttendanceOption.NIGHT)

        // then
        assertThat(loginCard.userId).isEqualTo(user.id)
        assertThat(loginCard.logoutDateTime).isNull()
        assertThat(loginCard.durationSeconds).isNull()
        assertThat(loginCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(loginCard.option).isEqualTo(AttendanceOption.NIGHT)
        assertThat(loginCard.type).isEqualTo(CardType.WORK)
        assertThat(savedCard.logoutDateTime).isNotNull
        assertThat(savedCard.durationSeconds).isNotNull
        assertThat(savedCard.option).isEqualTo(AttendanceOption.NORMAL)
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
            AttendanceOption.NORMAL,
            TimeHandler.nowDateTime()
        )
        attendanceCardRepository.save(card)

        // when
        val logoutCard = attendance.responseLogout(user.id)

        // then
        assertThat(logoutCard.userId).isEqualTo(user.id)
        assertThat(logoutCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(logoutCard.logoutDateTime).isNotNull
        assertThat(logoutCard.durationSeconds).isNotNull
        assertThat(logoutCard.option).isEqualTo(AttendanceOption.NORMAL)
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
        assertThat(mistakeCard.logoutDateTime).isNull()
        assertThat(mistakeCard.userId).isEqualTo(user.id)
        assertThat(mistakeCard.projectId).isEqualTo(projectRepository.findAll()[0].id)
        assertThat(mistakeCard.option).isEqualTo(AttendanceOption.NORMAL)
        assertThat(mistakeCard.type).isEqualTo(CardType.MISTAKE)

    }

    @Test
    fun `현재 출근해 있는 사람이 몇시간을 입력한다`() {

    }

    @Test
    fun `현재 출근해 있지 않은 사람이 몇시간을 입력한다`() {

    }
}