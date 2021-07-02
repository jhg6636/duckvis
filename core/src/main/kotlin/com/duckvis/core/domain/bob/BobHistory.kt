package com.duckvis.core.domain.bob

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.bob.PayType
import javax.persistence.*

@Entity
@Table(
  uniqueConstraints = [UniqueConstraint(
    name = "uni_bob_history_1",
    columnNames = ["bob_ticket_id"]
  )]
)
class BobHistory(
  @Column(name = "bob_ticket_id")
  val bobTicketId: Long,

  val userId: Long,
  var isBobLeader: Boolean,
  var bobTeamNumber: Int,

  @Enumerated(EnumType.STRING)
  val payType: PayType
) : BaseDuckvisEntity() {
}