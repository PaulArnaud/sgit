package sgit.objects

import sgit.sgitTrait._
import sgit._

case class Branch(rootPath: String, name: String, commit: Option[Commit])
    extends Savable
    with Printable {

  def print: String = {
    s"${name}"
  }

  def save(rootPath: String) = {
    Saver.saveBranch(rootPath, name, commit)
  }
}
