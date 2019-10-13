package sgit

object LCS {

    /* https://rosettacode.org/wiki/Longest_common_subsequence#Scala */

    def lcsRec[T]: (IndexedSeq[T], IndexedSeq[T]) => IndexedSeq[T] = {
        case (a +: as, b +: bs) if a == b => a +: lcsRec(as, bs)
        case (as, bs) if as.isEmpty || bs.isEmpty => IndexedSeq[T]()
        case (a +: as, b +: bs) =>
          val (s1, s2) = (lcsRec(a +: as, bs), lcsRec(as, b +: bs))
          if(s1.length > s2.length) s1 else s2
    }
    
}