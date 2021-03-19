package com.catshi.nuguri.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Project(
    val name: String,
    val nickname: String,
    val teamId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1
}