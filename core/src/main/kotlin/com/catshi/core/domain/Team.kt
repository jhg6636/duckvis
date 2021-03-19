package com.catshi.core.domain

import com.catshi.core.exceptions.NotTeamMemberException
import com.catshi.core.exceptions.TeamMemberAlreadyExistsException
import javax.persistence.*
import javax.transaction.Transactional

@Entity
class Team(
    private val name: String,
    private var managerId: Long = -1,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

//    @Transactional
//    fun addTeamMember(userId: Long) {
//        if (this.teamMembers.contains(userId)) throw TeamMemberAlreadyExistsException()
//        this.teamMembers.add(userId)
//    }
//
//    @Transactional
//    fun deleteTeamMember(userId: Long) {
//        if (!this.teamMembers.remove(userId)) throw NotTeamMemberException()
//    }

    fun setManager(managerId: Long) {
        this.managerId = managerId
    }

//    @Transactional
//    fun getTeamSize(): Int {
//        return this.teamMembers.size
//    }
}