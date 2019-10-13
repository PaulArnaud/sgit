package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile, BufferedWriter, BufferedReader, FileInputStream, FileOutputStream, FileWriter, FileReader}
import org.apache.commons.codec.digest.DigestUtils

object FileTools {
    
    def createRepo() : Unit = {
        createFileOrDirectory(".sgit", true)
        createFileOrDirectory(".sgit/tags", true)
        createFileOrDirectory(".sgit/branchs", true)
        createFileOrDirectory(".sgit/objects", true)
        createFileOrDirectory(".sgit/STAGE", false)
        createFileOrDirectory(".sgit/HEAD", false)
        createFileOrDirectory(".sgit/LOGS", false)
        createFileOrDirectory(".sgit/REF", false)
        writeFile(".sgit/HEAD", "master")
        createFileOrDirectory(".sgit/branchs/master", false)
        writeFile(".sgit/branchs/master", "INITIAL COMMIT")
    }

    def createFileOrDirectory(name: String, isDirectory: Boolean) : Unit = {
        name.toFile.createIfNotExists(isDirectory)
    }

    def writeFile(nameFile: String, content: String): Unit = {
        nameFile.toFile.overwrite(content)
    }

    def addLineInFile(nameFile: String, newLine: String): Unit = {
        nameFile.toFile.appendLines(newLine)
    }

    def readFile(nameFile: String): String = {
       nameFile.toFile.contentAsString
    }

    def fileExploration(directory: JavaFile, research: String) : Option[JavaFile] = {
        val root = directory.listFiles().find( f => f.getName() == research )
        if (root.isDefined) {
            root
        }
        else {
            if (directory.getParentFile() == null) {
                None
            }
            else {
                fileExploration(directory.getParentFile(), research)
            }
        }
    }

    def listFilesInDirectory(root: JavaFile): Array[JavaFile] = {
        val (directory, files) = root.listFiles.partition( e => e.isDirectory() )
        return files ++ directory.filter( c => c.getName() != ".sgit").flatMap( d => listFilesInDirectory(d))
    }

    def filesFromStage(root: JavaFile) : Array[JavaFile] = {
        val rootPath = root.getAbsolutePath 
        val stageContent = FileTools.readFile(rootPath+"/.sgit/STAGE")
        return stageContent.split("\n").map( s => new JavaFile(rootPath + s.split(" ")(1)))
    }

    def sha1FromStage(root: JavaFile, file: JavaFile) : String = {
        val rootPath = root.getAbsolutePath 
        val stageContent = FileTools.readFile(root.getCanonicalPath() +"/.sgit/STAGE")
        val line = stageContent.split("\n").find( s => new JavaFile(rootPath + s.split(" ")(1)) == file)
        line.get.split(" ")(0)
    }

    def getSHA1(file: JavaFile) : String = {
        val fileContent = scala.io.Source.fromFile(file.getCanonicalPath()).mkString
        return DigestUtils.sha1Hex(fileContent)
    }

    def findCommit(root: JavaFile, name: String) : Option[JavaFile] = {
        val rootPath = root.getCanonicalPath
        if ((rootPath + "/.sgit/objects/" + name).toFile.exists) {
            Some(new JavaFile(rootPath + "/.sgit/objects/" + name))
        }
        else if ((rootPath + "/.sgit/branch/" + name).toFile.exists) {
            val commitName = readFile(rootPath + "/.sgit/branch/" + name)
            Some(new JavaFile(rootPath + "/.sgit/objects/" + commitName))
        }
        else if ((rootPath + "/.sgit/tag/" + name).toFile.exists) {
            val commitName = readFile(rootPath + "/.sgit/tag/" + name)
            Some(new JavaFile(rootPath + "/.sgit/objects/" + commitName))
        } 
        else {
            None
        }
    }

    def checkoutFromCommit(root: JavaFile, commit: JavaFile): Unit = {
        val rootPath = root.getCanonicalPath()
        val commitContent = readFile(commit.getCanonicalPath())
        val files = commitContent
            .split("\n")
            .tail
            .foreach( l => {
                val linesplit = l.split(" ")
                val fileContent = readFile(rootPath + "/.sgit/objects" + linesplit(0))
                val fileName = rootPath + linesplit(1)
                createFileOrDirectory(fileName, false)
                writeFile(fileName, fileContent)
            })
    }

    def listBranchs(root: JavaFile) : Array[JavaFile] = {
        val rootPath = root.getCanonicalPath() 
        new JavaFile(rootPath + "/.sgit/branchs").listFiles()
    }

    def listTags(root: JavaFile) : Array[JavaFile] = {
        val rootPath = root.getCanonicalPath() 
        new JavaFile(rootPath + "/.sgit/tags").listFiles()
    }
}