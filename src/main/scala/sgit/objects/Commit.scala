package sgit.objects

import sgit._
import sgit.sgitTrait._

case class Commit(
    rootPath: String,
    name: String,
    message: String,
    date: String,
    father: Option[Commit],
    blops: Seq[Blop]
) extends Savable
    with Printable {

  def print: String = {
    name
  }
  def save(rootPath: String): Unit = {
    Saver.saveCommit(rootPath, name, message, date, father, blops)
  }
}
