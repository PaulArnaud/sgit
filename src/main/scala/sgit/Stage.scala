package sgit 

import java.io.File

class Stage (root: File) {

    def getStatus : String = {
        val stageContent = FileTools.readFile(root + "/STAGE")
        return stageContent
    }
    
}