package com.duckvis.bob.services

import com.duckvis.core.domain.User
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.types.UserPathType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDao(val userRepository: UserRepository) {
    @Transactional(readOnly = true)
    fun getUserIdByUserCodeAndPath(stringId: String, pathType: UserPathType): Long? {
        return userRepository.findByCodeAndPath(stringId, pathType)?.id
    }

    @Transactional
    fun getUserById(userId: Long): User? {
        return userRepository.findByIdOrNull(userId)
    }

    @Transactional
    fun saveNewUser(stringId: String, name: String): User {
        return userRepository.save(User(stringId, name))
    }
}