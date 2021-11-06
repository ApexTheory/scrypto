package scorex.crypto.authds.avltree.batch.serialization

import scorex.crypto.authds.avltree.batch.ProverNodes
import scorex.crypto.hash.Digest

/**
  * AVL subtree, starting from Manifests FinalInternalNode and ending with Leafs
  */
case class BatchAVLProverSubtree[D <: Digest](subtreeTop: ProverNodes[D])