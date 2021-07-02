package com.duckvis.bob.ui.command

import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.utils.DateTimeMaker
import org.springframework.stereotype.Component

@Component
class ResponseModifyPayWithoutSupportParser : CommandParser<ResponseModifyPayWithoutSupport> {
  override fun fromText(text: String): ResponseModifyPayWithoutSupport? {
    val splitText = text.split(" ")
    if (splitText.size == 5 && splitText[0] == "!금액" && splitText[2] == "%미지원") {
      return ResponseModifyPayWithoutSupport(
        splitText[1].toInt(),
        DateTimeMaker.stringToDate(splitText[3]),
        BobTimeType.fromString(splitText[4])
      )
    }
    return null
  }
}