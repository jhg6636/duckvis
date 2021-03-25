package com.duckvis.nuguri.services

import com.duckvis.nuguri.services.Administration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AdministrationServiceTest constructor(
    @Autowired private val administration: Administration
) {
    // 플젝 관련 일반인
    @Test
    fun `아무 플젝도 등록하지 않은 상태에서 플젝목록을 입력한다`() {

    }

    @Test
    fun `플젝 등록 후 플젝목록을 입력한다`() {

    }

    @Test
    fun `일반인이 플젝 등록을 시도한다`() {

    }

    @Test
    fun `일반인이 플젝 시작을 시도한다`() {

    }

    @Test
    fun `일반인이 플젝 종료를 시도한다`() {

    }

    // 플젝 관련 관리자
    @Test
    fun `등록하지 않은 플젝을 플젝 시작한다`() {

    }

    @Test
    fun `등록한 플젝을 시작한다`() {

    }

    @Test
    fun `시작한 플젝을 종료한다`() {

    }

    @Test
    fun `시작하지 않은 플젝을 종료한다`() {

    }

    // 플젝 관련 매니저
    @Test
    fun `한 매니저가 다른 팀의 플젝을 종료한다`() {

    }

    // 팀, 사원 관련 일반인 테스트
    @Test
    fun `아무 사원도 등록하지 않은 상태에서 사원목록을 확인한다`() {

    }

    @Test
    fun `사원 등록 후 사원목록을 확인한다`() {

    }

    @Test
    fun `아무 팀도 등록하지 않은 상태에서 팀목록을 확인한다`() {

    }

    @Test
    fun `팀 등록 후 팀목록을 확인한다`() {

    }

    @Test
    fun `아무 팀도 등록하지 않은 상태에서 팀장목록을 확인한다`() {

    }

    @Test
    fun `팀 등록 후 팀장목록을 확인한다`() {

    }

    @Test
    fun `내 팀 등록을 하지 않은 상태에서 내 팀을 확인한다`() {

    }

    @Test
    fun `내 팀 등록 후 내 팀을 확인한다`() {

    }

    // 팀, 사원 관련 매니저

    @Test
    fun `사원이 아닌 사람을 팀원으로 등록한다`() {

    }

    @Test
    fun `팀이 없는 사원 중 한 명을 팀원으로 등록한다`() {

    }

    @Test
    fun `이미 하나의 팀에 속해 있는 사원 중 한 명을 팀원으로 등록한다`() {

    }

    @Test
    fun `이미 두 팀 이상에 속해 있는 사원 중 한 명을 팀원으로 등록한다`() {

    }

    @Test
    fun `팀에 없는 사람을 팀원에서 삭제한다`() {

    }

    @Test
    fun `한 팀에만 속해 있던 팀원을 팀원에서 삭제한다`() {

    }

    @Test
    fun `두 팀에 속해 있던 팀원을 팀원에서 삭제한다`() {

    }

    @Test
    fun `세 팀 이상에 속해 있던 팀원을 팀원에서 삭제한다`() {

    }

    // 팀, 사원 관련 관리자

    @Test
    fun `팀을 생성한다`() {

    }

    @Test
    fun `생성되지 않은 팀을 삭제한다`() {

    }

    @Test
    fun `생성된 팀을 삭제한다`() {

    }

    @Test
    fun `생성되지 않은 팀의 팀장을 변경한다`() {

    }

    @Test
    fun `생성된 팀의 팀장을 변경한다`() {

    }

    @Test
    fun `아무 관리자도 등록하지 않은 상태에서 관리자목록을 입력한다`() {

    }

    @Test
    fun `사원이 아닌 사람을 관리자로 등록한다`() {
        
    }

    @Test
    fun `관리자가 아니었던 사원을 관리자로 등록한다`() {

    }

    @Test
    fun `현재 관리자인 사원을 관리자로 등록한다`() {

    }

    @Test
    fun `현재 관리자가 아닌 사원을 관리자에서 삭제한다`() {

    }

    @Test
    fun `현재 관리자인 사원을 관리자에서 삭제한다`() {

    }
}