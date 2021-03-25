package com.duckvis.nuguri.services

import com.duckvis.nuguri.domain.TeamRepository
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.exceptions.NoSuchTeamException
import com.duckvis.core.exceptions.TeamNameAlreadyExistsException
import com.duckvis.core.types.UserLevelType
import com.duckvis.nuguri.domain.Project
import com.duckvis.nuguri.domain.ProjectRepository
import com.duckvis.nuguri.domain.Team
import com.duckvis.nuguri.exceptions.NoSuchProjectException
import com.duckvis.nuguri.exceptions.NoSuchUserException
import com.duckvis.nuguri.exceptions.ProjectNameAlreadyExistsException
import com.duckvis.nuguri.exceptions.ProjectNicknameAlreadyExistsException
import com.duckvis.nuguri.services.Administration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdministrationService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
): Administration {
    @Transactional
    override fun responseShowAllProjects(): List<Project> {
        return projectRepository.findAll()
    }

    @Transactional
    override fun responseAddProject(name: String, nickname: String, teamId: Long): Project {
        checkIfProjectNameExists(name)
        checkIfProjectNicknameExists(nickname)
        return projectRepository.save(Project(name, nickname))
    }

    private fun checkIfProjectNameExists(name: String) {
        if (projectRepository.existsByName(name)) {
            throw ProjectNameAlreadyExistsException()
        }
    }

    private fun checkIfProjectNicknameExists(nickname: String) {
        if (projectRepository.existsByNickname(nickname)) {
            throw ProjectNicknameAlreadyExistsException()
        }
    }

    @Transactional
    override fun responseDeleteProject(projectId: Long) {
        val project = projectRepository.findByIdOrNull(projectId) ?: throw NoSuchProjectException()
        projectRepository.delete(project)
    }

    @Transactional
    override fun responseAddTeam(name: String, managerId: Long): Team {
        checkIfTeamNameExists(name)
        val team = Team(name)
        return teamRepository.save(team)
    }

    private fun checkIfTeamNameExists(name: String) {
        if (teamRepository.existsByName(name)) {
            throw TeamNameAlreadyExistsException()
        }
    }

    @Transactional
    override fun responseDeleteTeam(name: String) {
        val team = teamRepository.findByName(name) ?: throw NoSuchTeamException()
        teamRepository.delete(team)
    }

    @Transactional
    override fun responseAddTeamMember(userId: Long, teamId: Long) {
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
        checkIfTeamExists(teamId)
    }

    private fun checkIfTeamExists(teamId: Long) {
        if (teamRepository.existsById(teamId)) {
            throw NoSuchTeamException()
        }
    }

    override fun responseDeleteTeamMember(userId: Long, teamId: Long) {
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
        checkIfTeamExists(teamId)
    }

    override fun responseHelp(userLevel: UserLevelType): String {
        return "를 참조하세요옹"
    }
}