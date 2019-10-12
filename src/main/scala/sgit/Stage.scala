package sgit 

import java.io.File

object Stage{
    
    def getNewStageContent( unmodifiedFiles: Array[File], newFiles: Array[File]): String ={
        var stageContent : String = ""
        unmodifiedFiles.foreach( f => stageContent ++ "\n" ++ f.getName )
        newFiles.foreach( f => stageContent ++ "\n" ++ f.getName )
        stageContent
    }
    
}