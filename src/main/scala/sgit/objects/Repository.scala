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

class Repository(
    val rootPath: String,
    val workingDirectory: Seq[Blop],
    val stage: Stage,
    val head: String,
    val lastCommit: Option[Commit],
    val branchs: Seq[Branch],
    val tags: Seq[Tag]
) extends Savable {

  val (common, delete, untracked, modified) =
    Utils.getCDUM(stage.blops, workingDirectory)

  def getDiff: Seq[(String, Seq[String], Seq[String])] = {
    modified.map(blop => {
      val wdFile = FileManager.readFile(blop.filePath).split("\n")
      val stageFile = FileManager
        .readFile(s"${rootPath}${sep}.sgit${sep}objects${sep}${blop.sha1}")
        .split("\n")
      val lcs = LCS.lcsDP(wdFile, stageFile)
      val linesAdded = wdFile.diff(lcs)
      val linesDeleted = stageFile.diff(lcs)
      (blop.filePath, linesAdded, linesDeleted)
    })
  }

  def save(rootPath: String): Unit = {
    stage.save(rootPath)
    FileManager.writeFile(s"${rootPath}${sep}.sgit${sep}HEAD", head)
    lastCommit match {
      case None        =>
      case Some(value) => value.save(rootPath)
    }
    branchs.foreach(branch => branch.save(rootPath))
    tags.foreach(tag => tag.save(rootPath))
  }
}
