package sgit.objects

import java.io.File
import sgit._
import sgit.sgitTrait._

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
    Utils.getCDUM(workingDirectory, stage.blops)

  def getDiff: Seq[(String, Seq[String], Seq[String])] = {
    FileTools
      .getContentFileFromBlop(rootPath, modified)
      .map(tuple => {
        val lcs = LCS.lcsDP(tuple._2, tuple._3)
        val linesAdded = tuple._2.diff(lcs)
        val linesDeleted = tuple._3.diff(lcs)
        (tuple._1, linesAdded, linesDeleted)
      })
  }

  def save(rootPath: String): Unit = {
    Saver.saveRepository(rootPath, this)
  }

}
