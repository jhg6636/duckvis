package com.duckvis.nuguri.dtos

import com.duckvis.nuguri.domain.AttendanceCard

class LoginResponse(
    val userId: Long,
    val logOutCard: AttendanceCard?,
    val logInCard: AttendanceCard,
) {
}