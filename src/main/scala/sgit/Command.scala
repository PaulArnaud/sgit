package sgit

import java.io.File

object Command {
    
    def init() : Unit = {
        FileTools.createRepo()
    }

    def status(root: File) : Unit = {
        val stage = new Stage(root)
        val status = stage.getStatus
        println("Content of stage : " + status)
    }

    def diff() : Unit = {

    }

    def add(root: File, files: Seq[String]) : Unit = {
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