package sgit

import java.io.File.{separator => sep}
import sgit.objects._

object Saver {

  def saveBlop(rootPath: String, sha1: String, filePath: String): Unit = {
    val fileContent = FileManager.readFile(filePath)
    val blopPath = s"${rootPath}${sep}.sgit${sep}objects${sep}${sha1}"
    FileManager.createFileOrDirectory(blopPath, false)
    FileManager.writeFile(blopPath, fileContent)
  }

  def saveCommit(
      rootPath: String,
      name: String,
      message: String,
      date: String,
      father: Option[Commit],
      blops: Seq[Blop]
  ): Unit = {
    val commitPath = s"${rootPath}${sep}.sgit${sep}objects${sep}${name}"
    val fatherName = if (father.isDefined) father.get.name else ""
    val firstLine = s"${name} ${message} ${date} ${fatherName}"
    val content = blops.map(blop => blop.print).mkString("\n")
    FileManager.createFileOrDirectory(commitPath, false)
    FileManager.writeFile(commitPath, s"${firstLine}\n${content}")
  }
}
