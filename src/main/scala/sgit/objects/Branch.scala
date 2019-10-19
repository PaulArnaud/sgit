package sgit.objects

import sgit.sgitTrait._

case class Branch(rootPath: String, commit: Option[Commit])
    extends Savable
    with Printable {

  def print: String = {
    s"${rootPath}"
  }

  def save(rootPath: String) = {
    //TODO
  }
}
