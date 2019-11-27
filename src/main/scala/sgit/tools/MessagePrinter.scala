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
      content.foreach(s => println(s" ${sign} ${s}"))
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

  def logP(color: String, commit: Option[Commit], rootPath: String): Unit = {
    commit match {
      case None => println(s"Initial Commit")
      case Some(value) => {
        value.father match {
          case None => {
            val diff = Utils.getCDUM(value.blops, Seq())
            //println(color + value.print)
            println(value.print)
            printable(Console.GREEN, "   New Files:", diff._3)
            println(" ")
            println(s"Initial Commit")
          }
          case Some(fathervalue) => {
            val diff = Utils.getCDUM(value.blops, fathervalue.blops)
            //println(color + value.print)
            println(value.print)
            if (diff._4.size > 0) {
              println("Diff :")
              val length = diff._4.size
              for ( blop <- 0 to length-1) {
                val newBlop = FileTools.getBlopContent(diff._4(blop), rootPath)
                val oldBlop = FileTools.getBlopContent(diff._5(blop), rootPath)
                val lcs = LCS.lcsDP(oldBlop, newBlop)
                val linesAdded = oldBlop.diff(lcs)
                val linesDeleted = newBlop.diff(lcs)
                printSimpleMessage(Console.GREEN, "   File : ", diff._4(blop).filePath)
                printDiffMessage(Console.BLUE, "   Lines added :", linesAdded, "+")
                printDiffMessage(Console.RED, "   Lines deleted :", linesDeleted, "-")
              }
            }
            //printable(Console.YELLOW, "   Modified Files:", diff._4)
            printable(Console.GREEN, "   New Files:", diff._3)
            printable(Console.RED, "   Deleted Files:", diff._2)
            //println(color + fathervalue.print)
            println(" ")
            //println(fathervalue.print)
            logP(color, Some(fathervalue), rootPath)
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
      //println(Console.WHITE + label)
      println(label)
      list.foreach(e => println(s"      ${e.printFile}") /*println(s"${color}   ${e.printFile}")*/)
    }
  }

}
