package scorex.crypto.hash

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.Base58

class Base58Specification extends PropSpec
with PropertyChecks
with GeneratorDrivenPropertyChecks
with Matchers {


  property("Base58 encoding then decoding preserves data") {
    forAll { data: Array[Byte] =>
      whenever(data.length > 0 && data.head != 0) {
        val encoded = Base58.encode(data)
        val restored = Base58.decode(encoded).get
        restored shouldBe data
      }
    }
  }

  property("Base58 encoding then decoding for genesis signature") {
    val data = Array.fill(64)(0: Byte)
    val encoded = Base58.encode(data)
    encoded shouldBe "1111111111111111111111111111111111111111111111111111111111111111"
    val restored = Base58.decode(encoded).get
    restored.length shouldBe data.length
    restored shouldBe data
  }

  property("base58 sample") {
    val b58 = "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62i"
    Base58.encode(Base58.decode(b58).get) shouldBe b58
  }
}
