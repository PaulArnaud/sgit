package sgit.objects

case class Tag(rootPath: String, commit: Option[Commit]) {

  def print: String = ???

  def save(rootPath: String) = ???

}
