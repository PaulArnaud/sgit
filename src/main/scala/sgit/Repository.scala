package sgit

import java.io.File

object Repository {

  def getRoot: Option[File] = {
    val currentDir = new File(new File(".").getCanonicalPath())
    FileTools.fileExploration(currentDir, ".sgit")
  }

}

class Repository(rootPath: String) {

  val workingDirectory: Array[Blop] =
    Initialization.importBlopsInDirectory(rootPath)

  val stage: Array[Blop] = Initialization.importBlopsFromStage(rootPath)

  val head: String = Initialization.getHead(rootPath)

  val lastCommit: Option[Commit] = Initialization.getLastCommit(rootPath)

  val branchs: Array[String] = Initialization.getBranchs(rootPath)

  val tags: Array[String] = Initialization.getTags(rootPath)

}
