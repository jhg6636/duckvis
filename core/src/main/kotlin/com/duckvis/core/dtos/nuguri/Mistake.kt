package com.duckvis.core.dtos.nuguri

data class Mistake(
  val userId: Long,
  val projectId: Long,
  val subProjectId: Long?,
  val durationSeconds: Int,
  val workTypeDto: WorkTypeDto = WorkTypeDto(),
) {
}
