package sgit

import java.io.File

object Repository {
    
    def getRoot : Option[File] = {
        val currentDir = new File(new File(".").getCanonicalPath())
        FileTools.fileExploration(currentDir, ".sgit")
    }

}