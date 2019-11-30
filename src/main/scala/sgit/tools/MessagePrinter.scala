package sgit

import sgit.sgitTrait._
import sgit.objects.Commit

object MessagePrinter {

  def printMessage(color: String, content: String): Unit = {
    //println(s"${color}${content}")
    println(s"${content}")
  }
  def printSimpleMessage(
      color: String,
      label: String,
      content: String
  ): Unit = {
    //println(s"${color}${label} ${content}")
    println(s"${label} ${content}")
  }

  def printDiffMessage(
      color: String,
      label: String,
      content: Seq[String],
      sign: String
  ): Unit = {
    if (content.size > 0) {
      //println(Console.WHITE + label)
      println(label)
      //content.foreach(s => println(s"${color} ${sign} ${s}"))
      content.foreach(s => println(s"    ${sign} ${s}"))
    }
  }

  def log(color: String, commit: Option[Commit]): Unit = {
    commit match {
      case None => println(s"Initial Commit")
      case Some(value) => {
        println("Commit :" + value.name)
        println("Message :" + value.message)
        println("Date :" + value.date)
        println("------------")
        log(color, value.father)
      }
    }
  }

  def printable[T](
      color: String,
      label: String,
      list: Seq[T with Printable]
  ): Unit = {
    if (list.size > 0) {
      //println(Console.WHITE + label)
      println(label)
      list.foreach(e => println(s"      ${e.printFile}") /*println(s"${color}   ${e.printFile}")*/)
    }
  }

}
