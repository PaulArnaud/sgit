package sgit

import sgit.FileTools
import java.io.File
import java.nio.file.Files

object Options {

    def init() : Unit = {
        FileTools.createRepo()
    }

    def status() : Unit = {

    }

    def diff() : Unit = {

    }

    def add(files: Seq[File]) : Unit = {
        val repository = findRepo()
        if (repository.isDefined) {
            val repositoryDirectory = repository.get
            files.foreach( x => FileTools.addToStage(FileTools.createBlop(x,repositoryDirectory),repositoryDirectory))
        }
        else {
            noRepoFindMsg()
        }
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

    def findRepo() : Option[File] = {
        return FileTools.findNearestRepo(new File(".").getCanonicalFile())
    }

    def noRepoFindMsg() : Unit = {
        println("No repository found. Please try to initialize one \n--> sgit init")
    }
}