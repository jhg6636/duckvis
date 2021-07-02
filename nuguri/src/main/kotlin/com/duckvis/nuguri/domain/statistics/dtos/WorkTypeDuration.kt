package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.dtos.nuguri.WorkTypeDto

data class WorkTypeDuration(
  val workTypeDto: WorkTypeDto,
  val durationSeconds: Int,
)
