package scorex.crypto.storage.merkle

import org.scalacheck.Arbitrary
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.storage.auth.AuthDataBlock
import scorex.utils.Random.randomBytes

class AuthDataBlockSpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks with Matchers {

  val keyVal = for {
    key: Long <- Arbitrary.arbitrary[Long]
    value <- Arbitrary.arbitrary[String]
  } yield AuthDataBlock(value.getBytes, Seq(randomBytes(), randomBytes()))

  property("decode-encode roundtrip") {
    forAll(keyVal) { case b: AuthDataBlock[Array[Byte]] =>
      val bytes = AuthDataBlock.encode(b)
      val decoded = AuthDataBlock.decode(bytes).get
      decoded.data shouldBe b.data
      decoded.merklePath.size shouldBe b.merklePath.size
      decoded.merklePath.head shouldBe b.merklePath.head
      decoded.merklePath(1) shouldBe b.merklePath(1)
    }
  }
}