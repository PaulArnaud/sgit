package sgit

import java.io.File.{separator => sep}
import sgit.objects._

object Saver {

  def saveBlop(rootPath: String, sha1: String, filePath: String): Unit = {
    val fileContent = FileManager.readFile(filePath)
    val blopPath = s"${rootPath}${sep}.sgit${sep}objects${sep}${sha1}"
    FileManager.createFileOrDirectory(blopPath, false)
    FileManager.writeFile(blopPath, fileContent)
  }

  def saveCommit(
      rootPath: String,
      name: String,
      message: String,
      date: String,
      father: Option[Commit],
      blops: Seq[Blop]
  ): Unit = {
    val commitPath = s"${rootPath}${sep}.sgit${sep}objects${sep}${name}"
    val fatherName = if (father.isDefined) father.get.name else ""
    val firstLine = s"${name} ${message} ${date} ${fatherName}"
    val content = blops.map(blop => blop.print).mkString("\n")
    FileManager.createFileOrDirectory(commitPath, false)
    FileManager.writeFile(commitPath, s"${firstLine}\n${content}")
  }

  def saveStage(rootPath: String, blops: Seq[Blop]): Unit = {
    blops.foreach(blop => blop.save(rootPath))
    val stagePath = s"${rootPath}${sep}.sgit${sep}STAGE"
    val stageContent = blops.map(blop => blop.print).mkString("\n")
    FileManager.writeFile(stagePath, stageContent)
  }

  def saveTag(rootPath: String, name: String, commit: String): Unit = {
    val tagPath = s"${rootPath}${sep}.sgit${sep}tags${sep}${name}"
    FileManager.createFileOrDirectory(tagPath, false)
    FileManager.writeFile(tagPath, commit)
  }

  def saveBranch(
      rootPath: String,
      name: String,
      commit: String
  ): Unit = {
    val branchPath = s"${rootPath}${sep}.sgit${sep}branchs${sep}${name}"
    FileManager.createFileOrDirectory(branchPath, false)
    FileManager.writeFile(branchPath, commit)
  }

  def saveRepository(rootPath: String, repository: Repository): Unit = {
    repository.stage.save(repository.rootPath)
    FileManager.writeFile(s"${rootPath}${sep}.sgit${sep}HEAD", repository.head)
    repository.lastCommit match {
      case None =>
      case Some(value) => {
        value.save(repository.rootPath)
        FileManager.writeFile(s"${rootPath}${sep}.sgit${sep}REF", value.name)
        if (repository.head != "") { // si on est positionné sur une branche
          FileManager.writeFile(
            s"${rootPath}${sep}.sgit${sep}branchs${sep}${repository.head}",
            value.name
          )
        }
      }
    }
    repository.branchs.foreach(branch => branch.save(repository.rootPath))
    repository.tags.foreach(tag => tag.save(repository.rootPath))
  }
}
