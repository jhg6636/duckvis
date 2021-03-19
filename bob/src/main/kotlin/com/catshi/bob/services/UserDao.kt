package com.catshi.bob.services

import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.types.UserPathType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserDao(val userRepository: UserRepository) {
    @Transactional(readOnly = true)
    open fun getUserIdByUserCodeAndPath(stringId: String, pathType: UserPathType): Long? {
        return userRepository.findByUserCodeAndPath(stringId, pathType)?.id
    }

    @Transactional
    open fun getUserById(userId: Long): User? {
        return userRepository.findByIdOrNull(userId)
    }

    @Transactional
    open fun saveNewUser(stringId: String, name: String): User {
        return userRepository.save(User(stringId, name))
    }
}