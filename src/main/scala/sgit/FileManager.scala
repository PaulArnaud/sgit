package sgit

import better.files._
import better.files.File._

object FileManager {

  def createFileOrDirectory(name: String, isDirectory: Boolean): Unit = {
    name.toFile.createIfNotExists(isDirectory, true)
  }

  def writeFile(nameFile: String, content: String): Unit = {
    nameFile.toFile.overwrite(content)
  }

  def addLineInFile(nameFile: String, newLine: String): Unit = {
    if (readFile(nameFile) == "") {
      writeFile(nameFile, newLine)
    } else {
      nameFile.toFile.appendLine().append(newLine)
    }
  }

  def readFile(nameFile: String): String = {
    nameFile.toFile.contentAsString
  }

  def cleanDirectory(name: String): Unit = {
    name.toFile.clear()
  }

  def exists(path: String): Boolean = {
    path.toFile.exists
  }

  def isDirectory(path: String): Boolean = {
    path.toFile.isDirectory
  }
}
