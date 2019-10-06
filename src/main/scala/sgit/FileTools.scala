package sgit

import better.files._
import better.files.File._

object FileTools {
    
    def createRepo() : Unit = {
        val sgit: File = ".sgit"
            .toFile
            .createIfNotExists(true)

        val tags: File = ".sgit/tags"
            .toFile
            .createIfNotExists(true)

        val commits: File = ".sgit/commits"
            .toFile
            .createIfNotExists(true)

        val trees: File = ".sgit/trees"
            .toFile
            .createIfNotExists(true)

        val blobs: File = ".sgit/blobs"
            .toFile
            .createIfNotExists(true)

        val branches: File = ".sgit/branches"
            .toFile
            .createIfNotExists(true)

        val HEAD: File = ".sgit/HEAD"
            .toFile
            .createIfNotExists()
        
        val STAGE: File = ".sgit/STAGE"
            .toFile
            .createIfNotExists()
    }
}