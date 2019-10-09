package sgit

import java.io.File

class Blop (filePath: String, rootPath: String, sha1: String) {

    def getContent : String = {
        FileTools.readFile(rootPath + ".sgit/objects/" + sha1)
    }
    
}