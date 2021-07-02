package com.duckvis.bob.ui.command

import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.utils.DateTimeMaker
import org.springframework.stereotype.Component

@Component
class ResponseModifySelfPayWithSupportParser : CommandParser<ResponseModifySelfPayWithSupport> {
  override fun fromText(text: String): ResponseModifySelfPayWithSupport? {
    val splitText = text.split(" ")
    if ((splitText.size == 5 && splitText[0] == ("!금액") && splitText[2] == "%개인결제")
      || (splitText.size == 5 && splitText[0] == ("!배달비") && splitText[2] == "%지원")
    ) {
      return ResponseModifySelfPayWithSupport(
        splitText[1].toInt(),
        DateTimeMaker.stringToDate(splitText[3]),
        BobTimeType.fromString(splitText[4])
      )
    }
    return null
  }
}