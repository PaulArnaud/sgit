package sgit

import sgit.sgitTrait._
import sgit.objects.Commit

object MessagePrinter {

  def printMessage(color: String, content: String): Unit = {
    println(s"${color}${content}")
  }
  def printSimpleMessage(color: String, label: String, content: String): Unit = {
    println(s"${color}${label} ${content}")
  }

  def printDiffMessage(color: String, label: String, content: Seq[String], sign: String): Unit = {
    if (content.size > 0) {
      println(Console.WHITE + label)
      content.foreach(s => println(s"${color} ${sign} ${s}"))
    }
  }

  def log(color: String, commit: Option[Commit]): Unit = {
    commit match {
      case None => println(s"Initial Commit")
      case Some(value) => {
        value.print
        log(color, value.father)
      }
    }
  }

  def logP(color: String, commit: Option[Commit]): Unit = {
    commit match {
      case None => println(s"Initial Commit")
      case Some(value) => {
        value.father match {
          case None => println(s"Initial Commit")
          case Some(fathervalue) => {
            val diff = Utils.getCDUM(fathervalue.blops, value.blops)
            printable(Console.RED, "Modified Files:", diff._4)
            printable(Console.RED, "Untracked Files:", diff._3)
            printable(Console.RED, "Deleted Files:", diff._2)
          }
        }
      }
    }
  }

  def printable[T](
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
