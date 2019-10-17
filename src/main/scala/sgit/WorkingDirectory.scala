package sgit
import java.io.File

class WorkingDirectory(rootPath: String) {

  val wdfiles: Array[File] = FileTools.listFilesInDirectory(new File(rootPath))
  val filesStaged: Array[File] = FileTools.filesFromStage(rootPath)

  def getDeletedFiles: Array[File] = {
    return filesStaged.diff(wdfiles)
  }

  def getUntrackedFiles: Array[File] = {
    return wdfiles.diff(filesStaged)
  }

  def getModifiedAndUnmodifiedFiles: (Array[File], Array[File]) = {
    wdfiles
      .intersect(filesStaged)
      .partition(f => {
        FileTools.sha1FromStage(rootPath, f) == FileTools.getSHA1(f)
      })
  }

  def contains(files: Array[File]): (Array[File], Array[File]) = {
    files.partition(f => f.getCanonicalPath.contains(rootPath))
  }

}
