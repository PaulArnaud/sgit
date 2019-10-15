package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile, BufferedWriter, BufferedReader, FileInputStream, FileOutputStream, FileWriter, FileReader}
import org.apache.commons.codec.digest.DigestUtils

object FileTools {
    
    def createRepo : Unit = {
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
        val root = directory.listFiles.find( f => f.getName == research )
        if (root.isDefined) {
            root
        }
        else {
            if (directory.getParentFile == null) {
                None
            }
            else {
                fileExploration(directory.getParentFile, research)
            }
        }
    }

    def listFilesInDirectory(root: JavaFile): Array[JavaFile] = {
        val (directory, files) = root.listFiles.partition( e => e.isDirectory )
        return files ++ directory.filter( c => c.getName != ".sgit").flatMap( d => listFilesInDirectory(d))
    }

    def filesFromStage(rootPath: String) : Array[JavaFile] = {
        val stageContent = FileTools
            .readFile(rootPath+"/.sgit/STAGE")
            .split("\n")
        if (stageContent(0) != "") {
            stageContent.map( s => new JavaFile(s.split(" ")(1)))
        }
        else {
            Array()
        }
    }

    def sha1FromStage(rootPath: String, file: JavaFile) : String = {
        val filePath = file.getCanonicalPath
        val stageContent = FileTools.readFile(rootPath +"/.sgit/STAGE")
        val line = stageContent.split("\n").find( s => s.split(" ")(1) == filePath)
        line.get.split(" ")(0)
    }

    def getSHA1(file: JavaFile) : String = {
        val fileContent = scala.io.Source.fromFile(file.getCanonicalPath).mkString
        return DigestUtils.sha1Hex(fileContent)
    }

    def findCommit(rootPath: String, name: String) : Option[JavaFile] = {
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

    def checkoutFromCommit(rootPath: String, commit: JavaFile): Unit = {
        val commitContent = readFile(commit.getCanonicalPath)
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

    def listBranchs(rootPath: String) : Array[JavaFile] = {
        new JavaFile(rootPath + "/.sgit/branchs").listFiles
    }

    def listTags(rootPath: String) : Array[JavaFile] = {
        new JavaFile(rootPath + "/.sgit/tags").listFiles
    }

    def createBlop(rootPath: String, blopName: String, blopContent: String): Unit = {
        val blopFullPath = rootPath + "/.sgit/objects/" + blopName
        createFileOrDirectory(blopFullPath, false)
        writeFile(blopFullPath, blopContent)
    }
}