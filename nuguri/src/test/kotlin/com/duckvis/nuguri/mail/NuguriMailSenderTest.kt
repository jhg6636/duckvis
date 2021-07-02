package com.duckvis.nuguri.mail

import com.duckvis.nuguri.external.mail.NuguriMailSender
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.nio.file.Paths

@SpringBootTest
class NuguriMailSenderTest(
  @Autowired private val nuguriMailSender: NuguriMailSender
) {

  @Disabled
  @Test
  fun `메일을 보낸다`() {
    val path = Paths.get("").toAbsolutePath().toString()
    val file = File("$path/a.txt")
    file.createNewFile()
    file.writeText("hihihihihihihihi")
    nuguriMailSender.send("mercury@selectstar.ai", "test", "testtesttest", file)
    file.delete()
  }

}