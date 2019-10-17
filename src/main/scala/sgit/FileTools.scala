package sgit

import java.io.File
import java.io.File.{separator => sep}
import org.apache.commons.codec.digest.DigestUtils

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
    FileManager.writeFile(
      s".sgit${sep}branchs${sep}master",
      "INITIAL COMMIT"
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

  def listFilesInDirectory(root: File): Array[File] = {
    val (directory, files) = root.listFiles.partition(e => e.isDirectory)
    return files ++ directory
      .filter(c => c.getName != ".sgit")
      .flatMap(d => listFilesInDirectory(d))
  }

  def filesFromStage(rootPath: String): Array[File] = {
    val stageContent = FileManager
      .readFile(s"${rootPath}${sep}.sgit${sep}STAGE")
      .split("\n")
    if (stageContent(0) != "") {
      stageContent.map(s => new File(s.split(" ")(1)))
    } else {
      Array()
    }
  }

  def sha1FromStage(rootPath: String, file: File): String = {
    val filePath = file.getCanonicalPath
    val stageContent = FileManager.readFile(
      s"${rootPath}${sep}.sgit${sep}STAGE"
    )
    val line = stageContent.split("\n").find(s => s.split(" ")(1) == filePath)
    line.get.split(" ")(0)
  }

  def getSHA1(file: File): String = {
    val fileContent = FileManager.readFile(file.getCanonicalPath)
    return DigestUtils.sha1Hex(fileContent)
  }

  def findCommit(rootPath: String, name: String): Option[File] = {
    val sgit = s"${rootPath}${sep}.sgit${sep}"
    if (new File(s"${sgit}objects${sep}${name}").exists) {
      Some(new File(s"${sgit}objects${sep}${name}"))
    } else if (new File(s"${sgit}branch${sep}${name}").exists) {
      val commitName = FileManager.readFile(s"${sgit}branch${sep}${name}")
      Some(new File(s"${sgit}objects${sep}${commitName}"))
    } else if (new File(s"${sgit}tag${sep}${name}").exists) {
      val commitName = FileManager.readFile(s"${sgit}tag${sep}${name}")
      Some(new File(s"${sgit}objects${sep}${commitName}"))
    } else {
      None
    }
  }

  def checkoutFromCommit(rootPath: String, commit: File): Unit = {
    val commitContent = FileManager.readFile(commit.getCanonicalPath)
    val files = commitContent
      .split("\n")
      .tail
      .foreach(l => {
        val linesplit = l.split(" ")
        val fileContent =
          FileManager.readFile(
            s"${rootPath}${sep}.sgit${sep}objects${sep}${linesplit(0)}"
          )
        val fileName = rootPath + linesplit(1)
        FileManager.createFileOrDirectory(fileName, false)
        FileManager.writeFile(fileName, fileContent)
      })
  }

  def listBranchs(rootPath: String): Array[File] = {
    new File(
      s"${rootPath}${sep}.sgit${sep}branchs"
    ).listFiles
  }

  def listTags(rootPath: String): Array[File] = {
    new File(s"${rootPath}${sep}.sgit${sep}tags").listFiles
  }

  def createBlop(
      rootPath: String,
      blopName: String,
      blopContent: String
  ): Unit = {
    val blopFullPath =
      s"${rootPath}${sep}.sgit${sep}objects${sep}${blopName}"
    FileManager.createFileOrDirectory(blopFullPath, false)
    FileManager.writeFile(blopFullPath, blopContent)
  }

  def getBranch(rootPath: String): Option[String] = {
    val sgit = s"${rootPath}${sep}.sgit${sep}"
    val head = FileManager.readFile(s"${sgit}HEAD")
    val branch = new File(s"${sgit}branchs${sep}${head}")
    if (branch.exists) {
      Some(head)
    } else {
      None
    }
  }

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
      father: String,
      blops: Array[Blop]
  ): Unit = {}

  def listFiles(dirPath: String): Array[File] = {
    new File(dirPath).listFiles
  }

}
