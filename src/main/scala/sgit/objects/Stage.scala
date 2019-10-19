package sgit.objects
import sgit.sgitTrait.Savable

case class Stage(sha1: String, blops: Seq[Blop]) extends Savable {

  def save(rootPath: String): Unit = {
    blops.foreach(blop => blop.save(rootPath))
  }
}
