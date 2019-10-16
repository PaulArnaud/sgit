package sgit

import java.io.File
import org.apache.commons.codec.digest.DigestUtils

object FileTools {

  def createRepo: Unit = {
    FileManager.createFileOrDirectory(s".sgit", true)
    FileManager.createFileOrDirectory(s".sgit${File.separator}tags", true)
    FileManager.createFileOrDirectory(s".sgit${File.separator}branchs", true)
    FileManager.createFileOrDirectory(s".sgit${File.separator}objects", true)
    FileManager.createFileOrDirectory(s".sgit${File.separator}STAGE", false)
    FileManager.createFileOrDirectory(s".sgit${File.separator}HEAD", false)
    FileManager.createFileOrDirectory(s".sgit${File.separator}LOGS", false)
    FileManager.createFileOrDirectory(s".sgit${File.separator}REF", false)
    FileManager.writeFile(s".sgit${File.separator}HEAD", "master")
    FileManager.createFileOrDirectory(
      s".sgit${File.separator}branchs${File.separator}master",
      false
    )
    FileManager.writeFile(
      s".sgit${File.separator}branchs${File.separator}master",
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
      .readFile(s"${rootPath}${File.separator}.sgit${File.separator}STAGE")
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
      s"${rootPath}${File.separator}.sgit${File.separator}STAGE"
    )
    val line = stageContent.split("\n").find(s => s.split(" ")(1) == filePath)
    line.get.split(" ")(0)
  }

  def getSHA1(file: File): String = {
    val fileContent = FileManager.readFile(file.getCanonicalPath)
    return DigestUtils.sha1Hex(fileContent)
  }

  def findCommit(rootPath: String, name: String): Option[File] = {
    val sep = File.separator
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
            s"${rootPath}${File.separator}.sgit${File.separator}objects${File.separator}${linesplit(0)}"
          )
        val fileName = rootPath + linesplit(1)
        FileManager.createFileOrDirectory(fileName, false)
        FileManager.writeFile(fileName, fileContent)
      })
  }

  def listBranchs(rootPath: String): Array[File] = {
    new File(
      s"${rootPath}${File.separator}.sgit${File.separator}branchs"
    ).listFiles
  }

  def listTags(rootPath: String): Array[File] = {
    new File(s"${rootPath}${File.separator}.sgit${File.separator}tags").listFiles
  }

  def createBlop(
      rootPath: String,
      blopName: String,
      blopContent: String
  ): Unit = {
    val blopFullPath =
      s"${rootPath}${File.separator}.sgit${File.separator}objects${File.separator}${blopName}"
    FileManager.createFileOrDirectory(blopFullPath, false)
    FileManager.writeFile(blopFullPath, blopContent)
  }
}
