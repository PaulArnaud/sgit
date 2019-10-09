package sgit

import sgit.FileTools
import org.scalatest.FunSuite
import java.io.File
    
class StageTest extends FunSuite {

    test("getStatus should return the content of the stage file "){
        pending

        FileTools.createRepo()
        val root = Repository.getRoot.get
    
        val stage = new Stage(root)

        assert(stage.getStatus == "")
        new File(".sgit").delete()
    }

    test("getDeleted files return all deleted files in working directory"){
        pending
    }

    test("getModified files return all modified files in working directory"){
        pending
    }

    test("getUntracked files return all untracked files in working directory"){
        pending
    }

    test("belongsToWorkingDirectory should verified if the file is in the working directory"){
        pending
    }

}
