package sgit

import better.files._
import better.files.File._
import java.io.{File => JavaFile, BufferedWriter, FileInputStream, FileOutputStream, FileWriter}
import scala.io.Source
import org.apache.commons.codec.digest.DigestUtils

object FileTools {
    
    def createRepo() : Unit = {
        val sgit: File = ".sgit"
            .toFile
            .createIfNotExists(true)

        val tags: File = ".sgit/tags"
            .toFile
            .createIfNotExists(true)

        val refs: File = ".sgit/refs"
            .toFile
            .createIfNotExists(true)

        val objects: File = ".sgit/objects"
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

    def createBlop(file :JavaFile, repository: JavaFile) : String = {
        val fileContent = scala.io.Source.fromFile(file.getAbsolutePath()).mkString
        val src = file.getAbsoluteFile()
        val dest = repository.getAbsolutePath() + "/objects/" + DigestUtils.sha1Hex(fileContent)
        var inputChannel = new FileInputStream(src).getChannel();
        var outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        inputChannel.close();
        outputChannel.close();
        return "blop " + dest + " " +file.getAbsolutePath()
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

    def addToStage(line: String, repository: JavaFile) : Unit = {
        val STAGE = new JavaFile(repository.getAbsolutePath()+"/STAGE")
        val fw = new FileWriter(STAGE,true)
        fw.write(line)
        fw.close()
    }
    
    def findNearestRepo(file: JavaFile) : Option[JavaFile] = {
        if (file.isDirectory) {
            val sibblings = file.listFiles()
            val sgit = sibblings.find(x => x.getName()==".sgit")
            if (sgit.isDefined){
                sgit 
            }
            else {
                findNearestRepo(file.getParentFile())
            } 
        }
        else {
            if (file.getName() == ".sgit"){
                Some(file)
            }
            else {
                if (file.getParentFile() == null) {
                    None
                }
                else {
                    findNearestRepo(file.getParentFile())
                }
            }
        }
    }

    def recursiveListFiles(f: JavaFile): Array[JavaFile] = {
        val these = f.listFiles
        these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
    }
}