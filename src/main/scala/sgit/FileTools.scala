package sgit

import java.io.File
import java.io.File.{separator => sep}
import sgit._
import sgit.objects._

object FileTools {

  def createRepo: Unit = {
    FileManager.createFileOrDirectory(s".sgit", true)
    FileManager.createFileOrDirectory(s".sgit${sep}tags", true)
    FileManager.createFileOrDirectory(s".sgit${sep}branchs", true)
    FileManager.createFileOrDirectory(s".sgit${sep}objects", true)
    FileManager.createFileOrDirectory(s".sgit${sep}STAGE", false)
    FileManager.createFileOrDirectory(s".sgit${sep}HEAD", false)
    FileManager.createFileOrDirectory(s".sgit${sep}LOGS", false)
    FileManager.createFileOrDirectory(s".sgit${sep}REF", false)
    FileManager.writeFile(s".sgit${sep}HEAD", "master")
    FileManager.createFileOrDirectory(
      s".sgit${sep}branchs${sep}master",
      false
    )
  }

  def fileExploration(
      directory: File,
      research: String
  ): Option[File] = {
    val root = directory.listFiles.find(f => f.getName == research)
    if (root.isDefined) {
      root
    } else {
      if (directory.getParentFile == null) {
        None
      } else {
        fileExploration(directory.getParentFile, research)
      }
    }
  }

  def listFilesInDirectory(root: File): Seq[File] = {
    val (directory, files) = root.listFiles.partition(e => e.isDirectory)
    return files ++ directory
      .filter(c => c.getName != ".sgit")
      .flatMap(d => listFilesInDirectory(d))
  }

  def listFiles(dirPath: String): Seq[File] = {
    new File(dirPath).listFiles
  }

  def func(repository: Repository, blops: Seq[Blop], blop: Blop): Seq[Blop] = {
    val b = Seq(blop)
    repository.common ++ repository.delete.intersect(b) ++ repository.untracked
      .intersect(b) ++ modified(repository.modified, blop)
  }

  def modified(list: Seq[Blop], blop: Blop): Seq[Blop] = {
    list.map(b => {
      if (b.filePath == blop.filePath) {
        blop
      } else {
        b
      }
    })
  }

  def writeInStage(rootPath: String, stage: Seq[Blop]): Unit = {
    val stagePath = s"${rootPath}${sep}.sgit${sep}STAGE"
    val content = stage.map(blop => blop.print).mkString("\n")
    FileManager.writeFile(stagePath, content)
  }

}
