package com.duckvis.bob.ui.command

import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.utils.DateTimeMaker
import org.springframework.stereotype.Component

@Component
class ResponseModifyPayWithSupportParser : CommandParser<ResponseModifyPayWithSupport> {
  override fun fromText(text: String): ResponseModifyPayWithSupport? {
    val splitText = text.split(" ")
    if (splitText.size == 5 && splitText[0] == "!금액" && splitText[2] == "%지원") {
      return ResponseModifyPayWithSupport(
        splitText[1].toInt(),
        DateTimeMaker.stringToDate(splitText[3]),
        BobTimeType.fromString(splitText[4])
      )
    }
    return null
  }
}