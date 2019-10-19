package sgit

import java.io.File
import sgit.sgitTrait._

object MessagePrinter {

  def printFiles(color: String, label: String, files: Seq[File]): Unit = {
    if (files.size != 0) {
      println(color + label)
      files.foreach(f => println(color + " -> " + f.getCanonicalPath))
    }
  }

  def printNameFile(color: String, label: String, files: Seq[File]): Unit = {
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

  def things[T](
      color: String,
      label: String,
      list: Seq[T with Printable]
  ): Unit = {
    if (list.size > 0) {
      println(Console.WHITE + label)
      list.foreach(e => println(s"${color}   ${e.print}"))
    }
  }

}
