package sgit

case class Blop(sha1: String, filePath: String) {

  def save(rootPath: String): Unit = {
    FileTools.saveBlop(rootPath, sha1, filePath)
  }
}
