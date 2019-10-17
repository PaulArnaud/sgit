package sgit

import java.io.File
import java.io.File.{separator => sep}
import org.apache.commons.codec.digest.DigestUtils

object Initialization {

  def importBlopsFromStage(rootPath: String): Array[Blop] = {
    val stage = FileManager.readFile(s"${rootPath}${sep}.sgit${sep}STAGE")
    stage
      .split("\n")
      .map(line => {
        val name = line.split(" ")(0)
        val path = line.split(" ")(1)
        new Blop(name, path)
      })
  }

  def importBlopsFromCommit(rootPath: String, name: String): Array[Blop] = {
    val commit =
      FileManager.readFile(s"${rootPath}${sep}.sgit${sep}objects${sep}${name}")
    commit
      .split("\n")
      .tail //because the first line is the presentation informations
      .map(line => {
        val name = line.split(" ")(0)
        val path = line.split(" ")(1)
        new Blop(name, path)
      })
  }

  def importBlopsInDirectory(rootPath: String): Array[Blop] = {
    val rootDir = new File(rootPath)
    FileTools
      .listFilesInDirectory(rootDir)
      .map(f => {
        val filePath = f.getCanonicalPath
        val sha1 = DigestUtils.sha1Hex(FileManager.readFile(filePath))
        new Blop(sha1, filePath)
      })
  }

  def getHead(rootPath: String): String = {
    FileManager.readFile(s"${rootPath}${sep}.sgit${sep}HEAD")
  }

  def getLastCommit(rootPath: String): Option[Commit] = {
    val sgit = s"${rootPath}${sep}.sgit${sep}"
    val sha1Commit = FileManager.readFile(s"${sgit}REF")
    if (sha1Commit == "") {
      None
    } else {
      val commitContent =
        FileManager.readFile(s"${sgit}objects${sep}${sha1Commit}").split("\n")
      val firstLine = commitContent.head.split(" ")
      val blops =
        commitContent.tail //because the first line is the presentation informations
          .map(line => {
            val name = line.split(" ")(0)
            val path = line.split(" ")(1)
            new Blop(name, path)
          })
      Some(
        new Commit(
          rootPath,
          firstLine(0),
          firstLine(1),
          firstLine(2),
          firstLine(3),
          blops
        )
      )
    }
  }

  def getBranchs(rootPath: String): Array[String] = {
    FileTools
      .listFiles(s"${rootPath}${sep}.sgit${sep}branchs")
      .map(f => f.getName)
  }

  def getTags(rootPath: String): Array[String] = {
    FileTools.listFiles(s"${rootPath}${sep}.sgit${sep}tags").map(f => f.getName)
  }
}
