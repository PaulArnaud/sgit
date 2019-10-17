package sgit

case class Commit(
    rootPath: String,
    name: String,
    message: String,
    date: String,
    father: String,
    blops: Array[Blop]
) {
  def save(rootPath: String): Unit = {
    FileTools.saveCommit(rootPath, name, message, date, father, blops)
  }
}
