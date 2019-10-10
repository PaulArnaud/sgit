package sgit 

import java.io.File

object Stage{
    
    def update( modifiedAndUnmodifiedFiles: (Array[File], Array[File]), newFiles: Array[File]): Unit ={
        var stageContent : String = ""
        modifiedAndUnmodifiedFiles._1.foreach( f => stageContent + f.getName )
        (modifiedAndUnmodifiedFiles._2 ++ newFiles).foreach( f => stageContent + f.getName )
        println(stageContent)
    }
}