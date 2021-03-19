package com.catshi.bob.domain

import javax.persistence.*

@Entity
class BobHistory(
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false) val bobTicket: BobTicket,
    var isBobLeader: Boolean,
    var bobTeamNumber: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
}