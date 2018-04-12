package scorex.crypto.authds.avltree.batch.serialization

import scorex.crypto.authds.avltree.batch.{InternalProverNode, ProverNodes}
import scorex.crypto.authds.{ADKey, Balance}
import scorex.crypto.hash.{CryptographicHash, Digest}

class ProxyInternalNode[D <: Digest](protected var pk: ADKey,
                                     val leftLabel: D,
                                     val rightLabel: D,
                                     protected var pb: Balance)
                                    (implicit val phf: CryptographicHash[D])
  extends InternalProverNode(k = pk, l = null, r = null, b = pb)(phf) {

  def mutate(n: ProverNodes[D]): Unit = {
    if (n.label sameElements leftLabel) {
      l = n
    } else if (n.label sameElements rightLabel) {
      r = n
    } else {
      throw new AssertionError("Unable to determine direction to mutate")
    }
  }

  def isEmty: Boolean = l == null || r == null
}

object ProxyInternalNode {
  def apply[D <: Digest](node: InternalProverNode[D])(implicit phf: CryptographicHash[D]): ProxyInternalNode[D] = {
    new ProxyInternalNode[D](node.key, node.left.label, node.right.label, node.balance)
  }
}
