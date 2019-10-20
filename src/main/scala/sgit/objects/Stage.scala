package sgit.objects

import sgit.sgitTrait.Savable
import sgit._
import org.apache.commons.codec.digest.DigestUtils

case class Stage(blops: Seq[Blop]) extends Savable {

  def save(rootPath: String): Unit = {
    Saver.saveStage(rootPath, blops)
  }

  def sha1: String = {
    DigestUtils.sha1Hex(blops.map(b => b.print).mkString("\n"))
  }
}
