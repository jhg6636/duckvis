package com.duckvis.core.dtos.nuguri

data class Work(
  val userId: Long,
  val projectId: Long,
  val subProjectId: Long?,
  val workTypeDto: WorkTypeDto = WorkTypeDto(),
) {

}