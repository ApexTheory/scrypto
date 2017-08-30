package scorex.crypto

import supertagged.TaggedType

package object authds {

  type LeafData = LeafData.Type
  type Side = Side.Type
  type ADKey = ADKey.Type
  type ADValue = ADValue.Type
  type ADDigest = ADDigest.Type
  type ADProof = ADProof.Type
  type Balance = Balance.Type

  object LeafData extends TaggedType[Array[Byte]]

  object Side extends TaggedType[Byte]

  object ADKey extends TaggedType[Array[Byte]]

  object ADValue extends TaggedType[Array[Byte]]

  object ADDigest extends TaggedType[Array[Byte]]

  object ADProof extends TaggedType[Array[Byte]]

  object Balance extends TaggedType[Byte]
}