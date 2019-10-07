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

    def add(files: Seq[File], repository: File) : Unit = {
        val (tracked, untracked) = files.partition( f => FileTools.isInStage(f, repository) )
        untracked.foreach( f => FileTools.addToStage(FileTools.createBlop(f, repository), repository) )
        val (modified, unmodified) = tracked.partition( f => FileTools.wasModified(f, repository))
        modified.foreach( f => println(f.getName()))
        modified.foreach( f => FileTools.updateStage(f, repository))
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