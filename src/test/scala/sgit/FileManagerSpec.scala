package sgit

import org.scalatest.FunSuite
import org.scalatest.Matchers._
import java.io.{File => JavaFile}
import better.files._

class FileManagerTest extends FunSuite {
    
    test("createFileOrDirectory should create a file or a directory"){
        FileManager.createFileOrDirectory("directory",true)
        FileManager.createFileOrDirectory("file",false)

        val directory = new JavaFile("directory")
        val file = new JavaFile("file")

        directory should exist
        file should exist

        directory.delete()
        file.delete()
    }

    test("writeFile should write on the file"){
        FileManager.createFileOrDirectory("file",false)

        FileManager.writeFile("file", "Test content")

        val file = File("file")

        file.contentAsString shouldEqual "Test content"
        new JavaFile("file").delete()
    }

    test("readFile should return the content of the file"){
        FileManager.createFileOrDirectory("file",false)
        FileManager.writeFile("file", "Test content")

        FileManager.readFile("file") shouldEqual "Test content"
        new JavaFile("file").delete()
    }

}
