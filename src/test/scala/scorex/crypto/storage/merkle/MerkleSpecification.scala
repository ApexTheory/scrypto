package scorex.crypto.storage.merkle

import java.io.{File, FileOutputStream}

import org.scalacheck.Gen
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.storage.auth.{MerkleTree, _}

import scala.util.Random

class MerkleSpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks with Matchers {

  property("value returned from byIndex() is valid for random dataset") {
    //fix block numbers for faster tests
    for (blocks <- List(7, 8, 9, 128)) {
      val smallInteger = Gen.choose(0, blocks - 1)
      val (treeDirName: String, _, tempFile: String) = generateFile(blocks)
      val (tree, segmentsStorage) = MerkleTree.fromFile(tempFile, treeDirName, 1024)
      forAll(smallInteger) { (index: Int) =>
        val leafOption = tree.byIndex(index).map(sig => AuthDataBlock(segmentsStorage.get(index).get, sig))
        leafOption should not be None
        val leaf = leafOption.get
        val resp = leaf.check(tree.rootHash)(DefaultHashFunction)
        resp shouldBe true
      }
    }
  }

  property("hash root is the same") {
    //fix block numbers for faster tests
    for (blocks <- List(7, 8, 9, 128)) {
      val (treeDirName: String, _, tempFile: String) = generateFile(blocks, "2")

      val (fileTree, segmentsStorage) = MerkleTree.fromFile(tempFile, treeDirName, 1024)
      val rootHash = fileTree.rootHash

      val tree = new MerkleTree(fileTree.storage, fileTree.nonEmptyBlocks)
      val newRootHash = tree.rootHash
      rootHash shouldBe newRootHash
    }
  }


  def generateFile(blocks: Int, subdir: String = "1"): (String, File, String) = {
    val treeDirName = "/tmp/scorex-test/test/" + subdir + "/"
    val treeDir = new File(treeDirName)
    val tempFile = treeDirName + "/data.file"


    val data = new Array[Byte](1024 * blocks)
    Random.nextBytes(data)
    treeDir.mkdirs()
    for (file <- treeDir.listFiles) file.delete

    val fos = new FileOutputStream(tempFile)
    fos.write(data)
    fos.close()
    (treeDirName, treeDir, tempFile)
  }
}