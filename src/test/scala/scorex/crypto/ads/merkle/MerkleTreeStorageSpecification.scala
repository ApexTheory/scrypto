package scorex.crypto.ads.merkle

import java.io.File

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.ads.merkle.TreeStorage.Key


class MerkleTreeStorageSpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks with Matchers {

  val treeDirName = "/tmp/scorex-test/test/MapDBStorageSpecification/"
  val treeDir = new File(treeDirName)
  treeDir.mkdirs()
  val dbFile = new File(treeDirName + "/db.file")
  val maxLevel = 50
  dbFile.delete()

  val keyVal = for {
    level: Int <- Gen.choose(1, maxLevel)
    key: Long <- Arbitrary.arbitrary[Long]
    value <- Arbitrary.arbitrary[String]
  } yield ((level, math.abs(key)), value.getBytes)

  property("set value and get it") {
    lazy val storage = new MapDbTreeStorage(Some(treeDirName + "/test_db"), maxLevel)

    forAll(keyVal) { case (key: Key, value: Array[Byte]) =>
      whenever(key._1 >= 0.toLong && key._2 >= 0.toLong) {
        storage.set(key, value)
        assert(storage.get(key).get sameElements value)
      }
    }
    storage.close()
  }
}