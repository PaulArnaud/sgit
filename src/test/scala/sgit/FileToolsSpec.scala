package sgit 

import org.scalatest.FunSuite
import org.scalatest.Matchers._
import java.io.{File => JavaFile}
import better.files._
import better.files.File._

class FileToolsTest extends FunSuite {

    test("createFileOrDirectory should create a file or a directory"){
        FileTools.createFileOrDirectory("directory",true)
        FileTools.createFileOrDirectory("file",false)

        val directory = new JavaFile("directory")
        val file = new JavaFile("file")

        directory should exist
        file should exist

        directory.delete()
        file.delete()
    }

    test("writeFile should write on the file"){
        FileTools.createFileOrDirectory("file",false)

        FileTools.writeFile("file", "Test content")

        val file = File("file")

        file.contentAsString shouldEqual "Test content"
        new JavaFile("file").delete()
    }

    test("readFile should return the content of the file"){
        FileTools.createFileOrDirectory("file",false)
        FileTools.writeFile("file", "Test content")

        FileTools.readFile("file") shouldEqual "Test content"
        new JavaFile("file").delete()
    }

    test("createRepo should create a repository directory") {
        FileTools.createRepo()

        val sgit = new JavaFile(".sgit")
        val stage = new JavaFile(".sgit/STAGE")
        val head = new JavaFile(".sgit/HEAD")
        val tags = new JavaFile(".sgit/tags")
        val refs = new JavaFile(".sgit/REF")
        val logs = new JavaFile(".sgit/LOGS")
        val objects = new JavaFile(".sgit/objects")
        val branches = new JavaFile(".sgit/branchs")
    
        sgit should exist
        stage should exist
        head should exist
        tags should exist
        refs should exist
        objects should exist
        branches should exist   
        logs should exist
        
        sgit.delete()
    }

    test("createRepo should not modify an existing repository directory"){
        FileTools.createRepo()
        val sgit = File(".sgit")
        val stage = File(".sgit/STAGE")
        stage.overwrite("hello")
        
        FileTools.createRepo()

        stage.contentAsString shouldEqual "hello"
        sgit.delete()
    }

    test("fileExploration should return the researched directory in current or parents directory"){
        FileTools.createFileOrDirectory(".sgit", true)
        FileTools.createFileOrDirectory("../.anothersgit", true)

        val currentDir = new JavaFile(new JavaFile(".").getCanonicalPath())
        val sgit = FileTools.fileExploration(currentDir, ".sgit")   
        val anothersgit = FileTools.fileExploration(currentDir, ".anothersgit")

        sgit.get should exist
        anothersgit.get should exist
        new JavaFile(".sgit").delete()
        new JavaFile("../.anothersgit").delete()
    }
}