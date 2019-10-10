package sgit

import java.io.File

object Command {
    
    def init() : Unit = {
        FileTools.createRepo()
    }

    def status(root: File, wd: WorkingDirectory) : Unit = {
        MessagePrinter.printFiles(Console.RED, "Deleted Files", wd.getDeletedFiles)
        MessagePrinter.printFiles(Console.YELLOW, "Untracked Files", wd.getUntrackedFiles)
        MessagePrinter.printFiles(Console.BLUE, "Modified Files", wd.getModifiedAndUnmodifiedFiles._2)
    }

    def diff() : Unit = {

    }

    def add(root: File, strings: Array[String], wd: WorkingDirectory) : Unit = {
        val files = strings.map( s => new File(s))
        val (inDirectory, outDirectory) = wd.contains(files)
        MessagePrinter.printFiles(Console.MAGENTA, "fatal : out of repository", outDirectory)
        Stage.update(wd.getModifiedAndUnmodifiedFiles, inDirectory)
    }

    def commit() : Unit = {

    }

    def log() : Unit = {

    }

    def branch() : Unit = {

    }

    def checkout() : Unit = {

    }

    def tag() : Unit = {

    }

    def merge() : Unit = {

    }

    def rebase() : Unit = {

    }
}