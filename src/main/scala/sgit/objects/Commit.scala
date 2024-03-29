package sgit.objects

import sgit._
import sgit.sgitTrait._

case class Commit(
    name: String,
    message: String,
    date: String,
    father: Option[Commit],
    blops: Seq[Blop]
) extends Savable
    with Printable {

  def print: String = {
    s"${name} ${message} ${date}"
  }

  def printFile: String = {
    name
  }

  def save(rootPath: String): Unit = {
    Saver.saveCommit(rootPath, name, message, date, father, blops)
  }

}
