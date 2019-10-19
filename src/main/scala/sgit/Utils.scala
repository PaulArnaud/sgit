package sgit

import sgit.objects.Blop
object Utils {

  def union[T](A: Seq[T], B: Seq[T]): Seq[T] = {
    A.union(B)
  }

  def intersection[T](A: Seq[T], B: Seq[T]): Seq[T] = {
    A.intersect(B)
  }

  def difference[T](A: Seq[T], B: Seq[T]): Seq[T] = {
    A.diff(B)
  }

  def pathCorrespondence(A: Seq[Blop], B: Seq[Blop]): Seq[Blop] = {
    val pathList = B.map(blopB => blopB.filePath)
    A.filter(blopA => pathList.contains(blopA.filePath))
  }

  def getCDUM(
      A: Seq[Blop],
      B: Seq[Blop]
  ): (Seq[Blop], Seq[Blop], Seq[Blop], Seq[Blop]) = {
    /* 
    Si :
    A = a, b, c, d 
    B = a', c, d, f
    common = c, d
    deleted = f
    untracked = b
    modified = a' (remplacé par a)
    */
    val common = intersection(A, B) // c,d
    val inANotB = difference(A, B) // a,b
    val inBNotA = difference(B, A) // a',f

    val modifiedInA = pathCorrespondence(inANotB, inBNotA) //a'
    val modifiedInB = pathCorrespondence(inBNotA, inANotB) //a

    val deleted = inBNotA.diff(modifiedInB) // b
    val untracked = inANotB.diff(modifiedInA)  // f

    (common, deleted, untracked, modifiedInB)
  }
}
