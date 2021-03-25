package com.duckvis.bob.exceptions

import com.duckvis.bob.domain.BobTicket

object NeverMatchThem {
    private fun isPossibleTeam(originalList: List<BobTicket>): Boolean {
        val nameList = originalList.map { it.user.name }
        if (nameList.contains("솔아")) {
            if (nameList.contains("바롬") || nameList.contains("남길")) {
                return false
            }
        } else if (nameList.contains("유리")) {
            if (nameList.contains("바롬") || nameList.contains("남길")) {
                return false
            }
        }
        return true
    }

    fun isPossibleChunkedList(chunkedList: List<List<BobTicket>>): Boolean {
        chunkedList.forEach { list ->
            if (!isPossibleTeam(list)) return false
        }
        return true
    }
}