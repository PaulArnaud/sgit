package sgit.objects

import sgit._
import sgit.sgitTrait._

case class Blop(sha1: String, filePath: String) extends Savable with Printable {

  def save(rootPath: String): Unit = {
    Saver.saveBlop(rootPath, sha1, filePath)
  }

  def print: String = {
    s"${sha1} ${filePath}"
  }
}
