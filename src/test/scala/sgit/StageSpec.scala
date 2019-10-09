package sgit

import sgit.FileTools
import org.scalatest.FunSuite
import java.io.File
    
class StageTest extends FunSuite {

    test("getStatus should return the content of the stage file "){
        FileTools.createRepo()
        val root = Repository.getRoot.get
    
        val stage = new Stage(root)

        assert(stage.getStatus == "")
        new File(".sgit").delete()
    }

}
