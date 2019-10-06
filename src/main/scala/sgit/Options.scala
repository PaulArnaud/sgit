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
        if (FileTools.hasFindRepo()) {
            val homeDir = FileTools.findHome()
            val (tracked, untracked) = FileTools.recursiveListFiles(homeDir).span(x => FileTools.isTracked(x))
            val (modified, unmodified) = tracked.span(x => FileTools.isModified(x))

            untracked.foreach(x => println("The file : " + x.getAbsolutePath() + " is untracked"))
            modified.foreach(x => println("The file : " + x.getAbsolutePath() + " has been modified"))
        }
        else {
            println("No repo")
        }
        /*
        pour chaque élément au niveau du .sgit : 
            si élément = dossier alors :
                récursion sur le contenu du dossier
            si élément = fichier alors :
                si élément est tracké alors :
                    on regarde si il est modifié 
                sinon : 
                    on le track
        */
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