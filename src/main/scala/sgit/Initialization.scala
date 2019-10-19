package sgit

import java.io.File
import java.io.File.{separator => sep}
import org.apache.commons.codec.digest.DigestUtils
import sgit.objects._

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

  def importBlopsFromCommit(
      rootPath: String,
      commitPath: String
  ): Array[Blop] = {
    val commit =
      FileManager.readFile(commitPath)
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
    createCommit(rootPath, s"${sgit}objects${sep}${sha1Commit}")
  }

  def createCommit(rootPath: String, commitPath: String): Option[Commit] = {
    if ((FileManager.exists(commitPath)) && !(FileManager
          .isDirectory(commitPath))) {
      val commitContent = FileManager
        .readFile(commitPath)
        .split("\n")
      val firstLine = commitContent.head.split(" ")
      val blops = importBlopsFromCommit(rootPath, commitPath)
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
    } else {
      None
    }

  }

  def getBranchs(rootPath: String): Array[Branch] = {
    FileTools
      .listFiles(s"${rootPath}${sep}.sgit${sep}branchs")
      .map(branch => {
        val branchPath = branch.getCanonicalPath
        val commit = createCommit(rootPath, FileManager.readFile(branchPath))
        new Branch(rootPath, commit)
      })
  }

  def getTags(rootPath: String): Array[Tag] = {
    FileTools
      .listFiles(s"${rootPath}${sep}.sgit${sep}tags")
      .map(tag => {
        val tagPath = tag.getCanonicalPath
        val commit = createCommit(rootPath, FileManager.readFile(tagPath))
        new Tag(rootPath, commit)
      })
  }

}
