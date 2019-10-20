package sgit

import java.io.File
import java.io.File.{separator => sep}
import sgit.filer._
import sgit.objects._

object FileTools {

  def createRepo: Unit = {
    FileManager.createFileOrDirectory(s".sgit", true)
    FileManager.createFileOrDirectory(s".sgit${sep}tags", true)
    FileManager.createFileOrDirectory(s".sgit${sep}branchs", true)
    FileManager.createFileOrDirectory(s".sgit${sep}objects", true)
    FileManager.createFileOrDirectory(s".sgit${sep}STAGE", false)
    FileManager.createFileOrDirectory(s".sgit${sep}HEAD", false)
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

  def computeNewStage(repository: Repository, blops: Seq[Blop]): Seq[Blop] = {
    repository.common ++ repository.delete.diff(blops) ++ repository.untracked
      .intersect(blops) ++ modified(repository.modified, blops)
  }

  def modified(list: Seq[Blop], blops: Seq[Blop]): Seq[Blop] = {
    if (blops.size != 0) {
      modified(list.map(b => {
        if (b.filePath == blops.head.filePath) {
          blops.head
        } else {
          b
        }
      }), blops.tail)
    } else {
      list
    }
  }

  def getContentFileFromBlop(
      rootPath: String,
      A: Seq[Blop]
  ): Seq[(String, Seq[String], Seq[String])] = {
    A.map(blop => {
      val wdFile = FileManager.readFile(blop.filePath).split("\n")
      val stageFile = FileManager
        .readFile(s"${rootPath}${sep}.sgit${sep}objects${sep}${blop.sha1}")
        .split("\n")
      (blop.filePath, wdFile, stageFile)
    })
  }

  def checkIfExisting(strings: Seq[String]): Seq[File] = {
    strings.map(s => new File(s)).filter(f => f.exists)
  }

  def getAllFiles(elements: Seq[File]): Seq[File] = {
    val (files, directorys) = elements.partition(f => f.isFile)
    files ++ directorys.flatMap(d => listFilesInDirectory(d))
  }
}
