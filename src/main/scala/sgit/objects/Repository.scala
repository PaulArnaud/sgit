package sgit.objects

import java.io.File
import sgit._
import sgit.sgitTrait._
import java.io.File.{separator => sep}

object Repository {

  def getRoot: Option[File] = {
    val currentDir = new File(new File(".").getCanonicalPath())
    FileTools.fileExploration(currentDir, ".sgit")
  }

}

class Repository(rootPath: String) extends Savable {

  val workingDirectory: Array[Blop] =
    Initialization.importBlopsInDirectory(rootPath)

  val stage: Array[Blop] = Initialization.importBlopsFromStage(rootPath)

  val head: String = Initialization.getHead(rootPath)

  val lastCommit: Option[Commit] = Initialization.getLastCommit(rootPath)

  val branchs: Array[Branch] = Initialization.getBranchs(rootPath)

  val tags: Array[Tag] = Initialization.getTags(rootPath)

  val (common, delete, untracked, modified) =
    Utils.getCDUM(workingDirectory, stage)

  def getDiff: Seq[(String, Array[String], Array[String])] = {
    modified.map(blop => {
      val wdFile = FileManager.readFile(blop.filePath).split("\n")
      val stageFile = FileManager
        .readFile(s"${rootPath}${sep}.sgit${sep}objects${sep}${blop.sha1}")
        .split("\n")
      val lcs = LCS.lcsDP(wdFile, stageFile)
      val linesAdded = wdFile.diff(lcs)
      val linesDeleted = stageFile.diff(lcs)
      (blop.filePath, linesAdded, linesAdded)
    })
  }

  def save(rootPath: String): Unit = {}
}
