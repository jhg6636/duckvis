package com.duckvis.bob.ui.command

import com.duckvis.core.types.bob.BobTimeType
import org.springframework.stereotype.Component

@Component
class ResponseDetermineBobTeamParser : CommandParser<ResponseDetermineBobTeam> {
  override fun fromText(text: String): ResponseDetermineBobTeam? {
    return when (text) {
      "!점심밥팀" -> ResponseDetermineBobTeam(BobTimeType.LUNCH)
      "!저녁밥팀" -> ResponseDetermineBobTeam(BobTimeType.DINNER)
      else -> null
    }
  }
}