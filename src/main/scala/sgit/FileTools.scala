package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile}
import java.io.BufferedWriter
import java.io.FileWriter

object FileTools {
    
    def createRepo() : Unit = {
        val sgit: File = ".sgit"
            .toFile
            .createIfNotExists(true)

        val tags: File = ".sgit/tags"
            .toFile
            .createIfNotExists(true)

        val commits: File = ".sgit/commits"
            .toFile
            .createIfNotExists(true)

        val trees: File = ".sgit/trees"
            .toFile
            .createIfNotExists(true)

        val blobs: File = ".sgit/blobs"
            .toFile
            .createIfNotExists(true)

        val branches: File = ".sgit/branches"
            .toFile
            .createIfNotExists(true)

        val HEAD: File = ".sgit/HEAD"
            .toFile
            .createIfNotExists()
        
        val STAGE: File = ".sgit/STAGE"
            .toFile
            .createIfNotExists()
    }

    def createBlop() : Unit = {

    }

    def createTree() : Unit = {

    }

    def createCommit() : Unit = {

    }

    def addToStage(file: JavaFile) : Unit = {
        val STAGE = new JavaFile(".sgit/STAGE")
        val fw = new FileWriter(STAGE,true)
        fw.write(file.getAbsolutePath() + "\n")
        fw.close()
    }
}