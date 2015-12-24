package scorex.crypto.hash

import fr.cryptohash.{Digest, SHA512}

object Sha512 extends FRHash {
  override protected def hf: Digest = new SHA512
}