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

    def createBlop(file :JavaFile) : Unit = {
        // Content of a file
        /*
        creation du sha1 avec le nom du fichier
        creation d'un fichier de nom (clé en sha1) et de contenu lefichier d'entrée
        */

    }

    def createTree() : Unit = {
        // one or several reference to blop OR one or several tree
        /*  
        De la forme : TYPE SHA1 nomFICHIER ||  Exemple : blob cf59e02c3d2a2413e2da9e535d3c116af1077906 README.md


        */

    }

    def createCommit() : Unit = {
        // one or many tree, one or many parent (commit) 
        /*  
        De la forme : 
        tree f89e64bdfcc08a8b371ee76a74775cfe096655ce
        author zspajich <zspajich@gmail.com> 1516710703 +0100
        committer zspajich <zspajich@gmail.com> 1516710703 +0100Initial Commit

        */
    }

    def addToStage(file: JavaFile) : Unit = {
        val STAGE = new JavaFile(".sgit/STAGE")
        val fw = new FileWriter(STAGE,true)
        fw.write(file.getAbsolutePath() + "\n")
        fw.close()
    }
    
    def findHome() : JavaFile = {
        return new JavaFile("")
    }

    def hasFindRepo() : Boolean = {
        return true
    }

    def recursiveListFiles(f: JavaFile): Array[JavaFile] = {
        val these = f.listFiles
        these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
    }

    def isTracked(f: JavaFile) : Boolean = {
        return true
    }

    def isModified(f: JavaFile) : Boolean = {
        val blop = findTheCorrespondingBlop()
        val hasBeenModified = false

        /* if (blop.length == f.length) {
            for i in 0, blop.length {
                if (blop[i] != f[i]) {
                    hasBeenModified = true
                }
            }
        }
        else {
            hasBeenModified = true
        }

        */
        return true
    }

    def findTheCorrespondingBlop() : JavaFile = {
        return new JavaFile("")
    }
}