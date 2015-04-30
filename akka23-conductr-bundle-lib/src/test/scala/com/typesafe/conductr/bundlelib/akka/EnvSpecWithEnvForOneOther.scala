package com.typesafe.conductr.bundlelib.akka

import com.typesafe.conductr.AkkaUnitTest
import com.typesafe.config.ConfigException.Missing

class EnvSpecWithEnvForOneOther extends AkkaUnitTest("EnvSpecWithEnvForOthers", "akka.loglevel = INFO") {

  val config = Env.asConfig

  "The Env functionality in the library" should {
    "return seed properties when running with one other seed node" in {
      config.getString("akka.cluster.seed-nodes.0") shouldBe "akka.udp://some-system-1_0_0@10.0.1.11:10001"
      intercept[Missing](config.getString("akka.cluster.seed-nodes.1"))
    }
  }
}
