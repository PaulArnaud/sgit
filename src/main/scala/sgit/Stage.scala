package sgit 

import java.io.File

class Stage (root: File) {

    var blops : Array[Blop] = null

    def getStatus : String = {
        val stageContent = FileTools.readFile(root + "/STAGE")
        this.blops = stageContent
            .split("\n")
            .map( line => new Blop(line.split(" ")(0), root.getCanonicalPath(), line.split(" ")(1)))
        return stageContent
    }
    
    def buildTrees : Tree = {
        return new Tree
    }
}