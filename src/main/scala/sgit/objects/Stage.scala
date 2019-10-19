package sgit.objects

import sgit.sgitTrait.Savable
import sgit._

case class Stage(sha1: String, blops: Seq[Blop]) extends Savable {

  def save(rootPath: String): Unit = {
    blops.foreach(blop => blop.save(rootPath))
    Saver.saveStage(rootPath, blops)
  }
}
