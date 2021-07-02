package com.duckvis.bob.ui.command

import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.utils.DateTimeMaker

class ResponseModifySelfPayWithoutSupportParser : CommandParser<ResponseModifySelfPayWithoutSupport> {

  override fun fromText(text: String): ResponseModifySelfPayWithoutSupport? {
    val splitText = text.split(" ")
    if (splitText.size == 5 && splitText[0] == "!배달비" && splitText[2] == "%미지원") {
      return ResponseModifySelfPayWithoutSupport(
        splitText[1].toInt(),
        DateTimeMaker.stringToDate(splitText[3]),
        BobTimeType.fromString(splitText[4])
      )
    }
    return null
  }

}