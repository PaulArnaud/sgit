package sgit

import better.files._
import better.files.File._

object FileManager {

  def createFileOrDirectory(name: String, isDirectory: Boolean): Unit = {
    name.toFile.createIfNotExists(isDirectory)
  }

  def writeFile(nameFile: String, content: String): Unit = {
    nameFile.toFile.overwrite(content)
  }

  def addLineInFile(nameFile: String, newLine: String): Unit = {
    if (readFile(nameFile)==""){
      writeFile(nameFile, newLine)
    }
    else {
      nameFile.toFile.appendLine().append(newLine)
    }
  }

  def readFile(nameFile: String): String = {
    nameFile.toFile.contentAsString
  }
}
