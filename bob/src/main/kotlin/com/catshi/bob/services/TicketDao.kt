package com.catshi.bob.services

import com.catshi.bob.domain.BobTicketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketDao(val bobTicketRepository: BobTicketRepository) {
    @Transactional
    fun isFirst(): Boolean {
        return bobTicketRepository.count() == 1L
    }
}