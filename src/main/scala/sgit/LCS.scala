package sgit

import scala.math.max

object LCS {

  /* https://rosettacode.org/wiki/Longest_common_subsequence#Scala */

  def lcsRec[T]: (IndexedSeq[T], IndexedSeq[T]) => IndexedSeq[T] = {
    case (a +: as, b +: bs) if a == b         => a +: lcsRec(as, bs)
    case (as, bs) if as.isEmpty || bs.isEmpty => IndexedSeq[T]()
    case (a +: as, b +: bs) =>
      val (s1, s2) = (lcsRec(a +: as, bs), lcsRec(as, b +: bs))
      if (s1.length > s2.length) s1 else s2
  }

  def lcsDP(
      s1: Seq[String],
      s2: Seq[String]
  ): Seq[String] = {
    if (s1 == null || s1.size == 0 || s2 == null || s2.size == 0) Seq()
    else if (s1 == s2) s1
    else {
      val up = 1
      val left = 2
      val charMatched = 3

      val s1Length = s1.size
      val s2Length = s2.size

      val lcsLengths = Array.fill[Int](s1Length + 1, s2Length + 1)(0)

      for (i <- 0 until s1Length) {
        for (j <- 0 until s2Length) {
          if (s1(i) == s2(j)) {
            lcsLengths(i + 1)(j + 1) = lcsLengths(i)(j) + 1
          } else {
            if (lcsLengths(i)(j + 1) >= lcsLengths(i + 1)(j)) {
              lcsLengths(i + 1)(j + 1) = lcsLengths(i)(j + 1)
            } else {
              lcsLengths(i + 1)(j + 1) = lcsLengths(i + 1)(j)
            }
          }
        }
      }

      var subSeq: Seq[String] = Seq()
      var s1Pos = s1Length
      var s2Pos = s2Length

      // build longest subsequence by backtracking
      do {
        if (lcsLengths(s1Pos)(s2Pos) == lcsLengths(s1Pos - 1)(s2Pos)) {
          s1Pos -= 1
        } else if (lcsLengths(s1Pos)(s2Pos) == lcsLengths(s1Pos)(s2Pos - 1)) {
          s2Pos -= 1
        } else {
          assert(s1(s1Pos - 1) == s2(s2Pos - 1))
          subSeq = subSeq :+ s1(s1Pos - 1)
          s1Pos -= 1
          s2Pos -= 1
        }

      } while (s1Pos > 0 && s2Pos > 0)

      subSeq.reverse
    }
  }
}
