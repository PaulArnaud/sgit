package sgit

object Utils {

  def intersection[T](A: Seq[T], B: Seq[T]): Seq[T] = {
    A.intersect(B)
  }

  def difference[T](A: Seq[T], B: Seq[T]): Seq[T] = {
    A.diff(B)
  }

}
