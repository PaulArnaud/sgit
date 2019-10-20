package sgit.objects

import sgit.sgitTrait._
import sgit._

case class Tag(name: String, commit: String)
    extends Savable
    with Printable {

  def print: String = {
    s"${name}"
  }

  def save(rootPath: String): Unit = {
    Saver.saveTag(rootPath, name, commit)
  }

}
