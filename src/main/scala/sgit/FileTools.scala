package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile, BufferedWriter, BufferedReader, FileInputStream, FileOutputStream, FileWriter, FileReader}
import org.apache.commons.codec.digest.DigestUtils

object FileTools {
    
    def createRepo() : Unit = {
        createFileOrDirectory(".sgit", true)
        createFileOrDirectory(".sgit/tags", true)
        createFileOrDirectory(".sgit/refs", true)
        createFileOrDirectory(".sgit/branches", true)
        createFileOrDirectory(".sgit/objects", true)
        createFileOrDirectory(".sgit/STAGE", false)
        createFileOrDirectory(".sgit/HEAD", false)
    }

    def createFileOrDirectory(name: String, isDirectory: Boolean) : Unit = {
        name.toFile.createIfNotExists(isDirectory)
    }

    def writeFile(nameFile: String, content: String): Unit = {
        File(nameFile).overwrite(content)
    }

    def readFile(nameFile: String): String = {
       File(nameFile).contentAsString
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

}