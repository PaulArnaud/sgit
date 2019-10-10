package sgit
import java.io.File

class WorkingDirectory (root: File) {

    var wdfiles : Array[File] = FileTools.listFilesInDirectory(root)
    val filesStaged : Array[File] = FileTools.filesFromStage(root)

    def getDeletedFiles : Array[File] = {
        return filesStaged.diff(wdfiles)
    }

    def getUntrackedFiles : Array[File] = {
        return wdfiles.diff(filesStaged)
    }

    def getModifiedAndUnmodifiedFiles : (Array[File], Array[File]) = {
        wdfiles.intersect(filesStaged).partition( f => {
            FileTools.sha1FromStage(root, f) == FileTools.getSHA1(f)
        })
    }

    def contains(files: Array[File]) : (Array[File], Array[File]) = {
        files.partition( f => wdfiles.contains(f) )
    }

}