package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile, BufferedWriter, BufferedReader, FileInputStream, FileOutputStream, FileWriter, FileReader}
import scala.io.Source
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
        val file : File = name
            .toFile
            .createIfNotExists(isDirectory)
    }

    def writeFile(nameFile: String, content: String): Unit = {
        val file = File(nameFile).overwrite(content)
    }

    def readFile(nameFile: String): String = {
        scala.io.Source.fromFile(nameFile).mkString
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


    ///////

    
    def createBlop(file :JavaFile, repository: JavaFile) : String = {
        val src = file.getCanonicalFile()
        val dest = repository.getCanonicalPath() + "/objects/" + getSHA1(file)
        var inputChannel = new FileInputStream(src).getChannel();
        var outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        inputChannel.close();
        outputChannel.close();
        return "blop " + dest + " " + file.getCanonicalPath() + "\n"
    }

    def addToStage(line: String, repository: JavaFile) : Unit = {
        val STAGE = new JavaFile(repository.getCanonicalPath()+"/STAGE")
        val fw = new FileWriter(STAGE,true)
        fw.write(line)
        fw.close()
    }
    
    def recursiveListFiles(f: JavaFile): Seq[JavaFile] = {
        val these = f.listFiles
        these ++ these.filter( f => f.isDirectory).flatMap( d => recursiveListFiles(d))
    }

    def isInStage(file: JavaFile, repository: JavaFile) : Boolean = {
        for (line <- Source.fromFile(repository.getCanonicalPath() + "/STAGE").getLines) {
            val fileSHA1Stage = line.split(" ")(1)
            val fileNameStage = line.split(" ")(2)
            val fileSHA1 = repository.getCanonicalPath() + "/objects/" + getSHA1(file)
            val fileName = file.getCanonicalPath()
            if ((fileName == fileNameStage) || (fileSHA1 == fileSHA1Stage)) {
                return true
            }
        }
        return false
    }

    def wasModified(file: JavaFile, repository: JavaFile) : Boolean = {
        for (line <- Source.fromFile(repository.getCanonicalPath() + "/STAGE").getLines) {
            val fileSHA1Stage = line.split(" ")(1)
            val fileNameStage = line.split(" ")(2)
            val fileSHA1 = repository.getCanonicalPath() + "/objects/" + getSHA1(file)
            val fileName = file.getCanonicalPath()
            if ( ( (fileName == fileNameStage) && (fileSHA1 != fileSHA1Stage) ) || ( (fileName != fileNameStage) && (fileSHA1 == fileSHA1Stage) ) ){
                return true
            }
        }
        return false
    }

    def updateStage(file: JavaFile, repository: JavaFile) : Unit = {
        var contentCopy: String = ""
        for (line <- Source.fromFile(repository.getCanonicalPath() + "/STAGE").getLines) {
            val fileSHA1Stage = line.split(" ")(1)
            val fileNameStage = line.split(" ")(2)
            val fileSHA1 = repository.getCanonicalPath() + "/objects/" + getSHA1(file)
            val fileName = file.getCanonicalPath()
            if ( ( (fileName == fileNameStage) && (fileSHA1 != fileSHA1Stage) ) || ( (fileName != fileNameStage) && (fileSHA1 == fileSHA1Stage) ) ){
                contentCopy = contentCopy.concat("blop " + fileSHA1 + " " + fileName + "\n")
            }
            else {
                contentCopy = contentCopy.concat("blop " + fileSHA1Stage + " " + fileNameStage + "\n")
            }
        }
        new FileWriter(repository.getCanonicalPath() + "/STAGE", false).close()
        addToStage(contentCopy,repository)
    }

    def getSHA1(file: JavaFile) : String = {
        val fileContent = scala.io.Source.fromFile(file.getCanonicalPath()).mkString
        return DigestUtils.sha1Hex(fileContent)
    }

    def getFilesInRepo(files: Seq[JavaFile], repository: JavaFile) : Seq[JavaFile] = {
        val home = repository.getParentFile().getCanonicalPath()
        files.foreach( f => println(f.getName()))
        val filesInRepo = files.filter( f => f.getCanonicalPath().contains(home))
        return filesInRepo
    }

    def getDeletedFiles(files: Seq[JavaFile], repository: JavaFile) : Seq[JavaFile] = {
        var deletedFiles = Seq()
        for (line <- Source.fromFile(repository.getCanonicalPath() + "/STAGE").getLines) {
            val file = new JavaFile(line.split(" ")(2))
            if (!files.contains(file)) {
                deletedFiles.:+(file)

            }
        }
        return deletedFiles
    }

    def deletInStage(file: JavaFile, repository: JavaFile): Unit = {
        var contentCopy: String = ""
        for (line <- Source.fromFile(repository.getCanonicalPath() + "/STAGE").getLines) {
            val fileSHA1Stage = line.split(" ")(1)
            val fileNameStage = line.split(" ")(2)
            val fileSHA1 = repository.getCanonicalPath() + "/objects/" + getSHA1(file)
            val fileName = file.getCanonicalPath()
            if ( (fileName == fileNameStage) && (fileSHA1 == fileSHA1Stage) ){
                contentCopy = contentCopy.concat("")
            }
            else {
                contentCopy = contentCopy.concat("blop " + fileSHA1Stage + " " + fileNameStage + "\n")
            }
        }
        new FileWriter(repository.getCanonicalPath() + "/STAGE", false).close()
        addToStage(contentCopy,repository)
    }

}