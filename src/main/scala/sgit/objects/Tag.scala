package sgit.objects

import sgit.sgitTrait._

case class Tag(rootPath: String, commit: Option[Commit])
    extends Savable
    with Printable {

  def print: String = {
    rootPath
  }

  def save(rootPath: String): Unit = {}

}
