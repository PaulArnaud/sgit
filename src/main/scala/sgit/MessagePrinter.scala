package sgit

import java.io.File
import java.awt.Color

object MessagePrinter {

  def printFiles(color: String, label: String, files: Array[File]): Unit = {
    if (files.size != 0) {
      println(color + label)
      files.foreach(f => println(color + " -> " + f.getCanonicalPath))
    }
  }

  def printNameFile(color: String, label: String, files: Array[File]): Unit = {
    if (files.size != 0) {
      println(color + label)
      files.foreach(f => println(color + " -> " + f.getName()))
    }
  }

  def printSimpleMessage(color: String, messageContent: String): Unit = {
    println(color + messageContent)
  }

  def printlog(color: String, logContent: String): Unit = {
    logContent.split("\n").reverse.foreach(s => println(color + s))
  }

}
