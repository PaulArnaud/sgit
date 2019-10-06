package sgit

import sgit.FileTools
import java.io.File

object Options {

    def init() : Unit = {
        FileTools.createRepo()
    }

    def status() : Unit = {

    }

    def diff() : Unit = {

    }

    def add(files: Seq[File]) : Unit = {
        files.map(x => FileTools.addToStage(x))
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