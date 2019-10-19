package sgit

import java.io.File
import java.io.File.{separator => sep}
import org.apache.commons.codec.digest.DigestUtils
import sgit.objects._

object Initialization {

  def importStage(rootPath: String): Stage = {
    val stage = FileManager.readFile(s"${rootPath}${sep}.sgit${sep}STAGE")
    if (stage == "") {
      new Stage("", Seq())
    } else {
      new Stage(
        DigestUtils.sha1Hex(stage),
        stage
          .split("\n")
          .map(line => {
            val name = line.split(" ")(0)
            val path = line.split(" ")(1)
            new Blop(name, path)
          })
      )

    }
  }

  def importBlopsFromCommit(
      rootPath: String,
      commitPath: String
  ): Seq[Blop] = {
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

  def importBlopsInDirectory(rootPath: String): Seq[Blop] = {
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
      val commit = getCommitRec(rootPath, firstLine(3))
      val blops = importBlopsFromCommit(rootPath, commitPath)
      Some(
        new Commit(
          rootPath,
          firstLine(0),
          firstLine(1),
          firstLine(2),
          commit,
          blops
        )
      )
    } else {
      None
    }

  }

  def getCommitRec(rootPath: String, fatherName: String): Option[Commit] = {
    if (fatherName == "") {
      None
    } else {
      val fatherPath = s"${rootPath}${sep}.sgit${sep}objects${sep}${fatherName}"
      val fatherCommit = FileManager.readFile(fatherPath).split("\n")
      val firstLine = fatherCommit.head.split(" ")
      val blops = importBlopsFromCommit(rootPath, fatherPath)
      Some(
        new Commit(
          rootPath,
          firstLine(0),
          firstLine(1),
          firstLine(2),
          getCommitRec(rootPath, firstLine(3)),
          blops
        )
      )
    }
  }

  def getBranchs(rootPath: String): Seq[Branch] = {
    FileTools
      .listFiles(s"${rootPath}${sep}.sgit${sep}branchs")
      .map(branch => {
        val branchPath = branch.getCanonicalPath
        val commit = createCommit(rootPath, FileManager.readFile(branchPath))
        new Branch(rootPath, commit)
      })
  }

  def getTags(rootPath: String): Seq[Tag] = {
    FileTools
      .listFiles(s"${rootPath}${sep}.sgit${sep}tags")
      .map(tag => {
        val tagPath = tag.getCanonicalPath
        val commit = createCommit(rootPath, FileManager.readFile(tagPath))
        new Tag(rootPath, commit)
      })
  }

}
