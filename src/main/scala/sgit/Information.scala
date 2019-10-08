package sgit
import java.io.File

object Information {

    def showUntrackedFiles(files: Seq[File]): Unit = {
        println("Files unstaged")
        files.foreach( f => println(f.getCanonicalPath()))
    }

    def showModifiedFiles(files: Seq[File]): Unit = {
        println("Files modified")
        files.foreach( f => println(f.getCanonicalPath()))
    }

    def showDeletedFiles(files: Seq[File]): Unit = {
        println("Files deleted")
        files.foreach( f => println(f.getCanonicalPath()))
    }
}