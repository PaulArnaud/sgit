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
    A.filter(blopA => B.map(blopB => blopB.filePath).contains(blopA.filePath))
  }

  def getCDUM(
      A: Seq[Blop],
      B: Seq[Blop]
  ): (Seq[Blop], Seq[Blop], Seq[Blop], Seq[Blop]) = {
    val common = intersection(A, B)
    val inANotB = difference(A, B)
    val inBNotA = difference(B, A)
    val modified = pathCorrespondence(inBNotA, inANotB)
    val untracked = inBNotA.diff(modified)
    val deleted = inANotB.diff(modified)
    (common, deleted, untracked, modified)
  }
}
