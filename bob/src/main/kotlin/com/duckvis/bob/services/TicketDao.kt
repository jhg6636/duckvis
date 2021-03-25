package com.duckvis.bob.services

import com.duckvis.bob.domain.BobTicketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketDao(val bobTicketRepository: BobTicketRepository) {
    @Transactional
    fun isFirst(): Boolean {
        return bobTicketRepository.count() == 1L
    }
}