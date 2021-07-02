package com.duckvis.core.types.nuguri.service

enum class NuguriServicePermission(val rank: Int) {
  EVERYBODY(4),
  MEMBER(3),
  MANAGER(2),
  ADMIN(1),
  ;

  fun available(rule: NuguriServicePermission): Boolean {
    return this.rank <= rule.rank
  }
}