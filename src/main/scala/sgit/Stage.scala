package sgit 

import java.io.File
import org.apache.commons.codec.digest.DigestUtils

object Stage{
    
    def update(rootPath: String, modifiedFile : File): Unit = {
        val filePath = modifiedFile.getCanonicalPath
        val content = FileTools.readFile(filePath)
        val sha1File = DigestUtils.sha1Hex(content)
        FileTools.createBlop(rootPath, sha1File, content)
        val stageContent = FileTools.readFile(rootPath + "/.sgit/STAGE" )
        val newStage = stageContent
            .split("\n")
            .foldLeft("")( (res,line) => {
                if (filePath == line.split(" ")(1)){
                    addString(res, sha1File + " " + filePath + "\n")
                }
                else {
                    addString(res, line + "\n")
                }
            })
        FileTools.writeFile(rootPath + "/.sgit/STAGE", newStage)
    }

    def add(rootPath: String, newFile : File): Unit = {
        val filePath = newFile.getCanonicalPath
        val content = FileTools.readFile(filePath)
        val sha1File = DigestUtils.sha1Hex(content)
        FileTools.createBlop(rootPath, sha1File, content)
        FileTools.addLineInFile(rootPath + "/.sgit/STAGE", sha1File + " " + filePath)
    }

    def addString(res: String, x: String) = { 
        res + x
    }
    
}