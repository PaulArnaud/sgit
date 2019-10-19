package sgit

import java.io.File
import org.apache.commons.codec.digest.DigestUtils
import sgit.objects._
import java.time.LocalDate

object Command {

  def init: Unit = {
    FileTools.createRepo
  }

  def status(repository: Repository): Unit = {
    /*
    _ Les fichiers qui sont dans le stage mais qui ne sont pas dans le working directory
    sont les fichiers qui ont été supprimés.
    _ Les fichiers qui sont dans le stage et dans le working directory sont les fichiers
    qui sont trackés et non modifiés
    _ Les fichiers qui ne sont pas dans le stage mais présent dans le working directory
    sont les fichiers non trackés et les fichiers modifiés
    _ Pour trouver les fichiers modifiés, il faut regarder les blops commun entre supprimés et non trackés
    selon le critère path
     */
    MessagePrinter.things(Console.RED, "Modified Files:", repository.modified)
    MessagePrinter.things(Console.RED, "Untracked Files:", repository.untracked)
    MessagePrinter.things(Console.RED, "Deleted Files:", repository.delete)
  }

  def diff(repository: Repository): Unit = {
    if (repository.modified.size != 0) {
      repository.getDiff.foreach(d => {
        println("File : " + d._1)
        println("Line Added : " + d._2.toString)
        println("Line Deleted : " + d._3.toString)
      })
    }
  }

  def add(repository: Repository, strings: Seq[String]): Unit = {
    // On vérifie si les paramètres entrés sont des fichiers existants
    val trueFile = strings.filter(s => new File(s).exists)

    // On sépare les fichiers des dossiers
    val (files, directorys) =
      trueFile.map(s => new File(s)).partition(f => f.isFile)

    // On va chercher tous les fichiers dans les dossiers
    val allFiles = files ++ directorys.flatMap(
      d => FileTools.listFilesInDirectory(d)
    )

    // On regarde si les fichiers sont bien dans le dépot git
    val (inRepo, outRepo) = allFiles.partition(file => {
      repository.workingDirectory
        .map(blop => blop.filePath)
        .contains(file.getCanonicalPath)
    })

    // On crée les blops qui correspondent aux fichiers
    val blops = inRepo.map(file => {
      val filePath = file.getCanonicalPath
      val fileContent = FileManager.readFile(filePath)
      new Blop(DigestUtils.sha1Hex(fileContent), filePath)
    })

    // On crée le nouveau STAGE
    val newStageContent = blops.foldLeft(repository.common)(
      (list, blop) => FileTools.func(repository, list, blop)
    )
    val sha1Stage =
      DigestUtils.sha1Hex(newStageContent.map(b => b.print).mkString("\n"))

    // On met à jour le répertoire complet
    new Repository(
      repository.rootPath,
      repository.workingDirectory,
      new Stage(sha1Stage, newStageContent),
      repository.head,
      repository.lastCommit,
      repository.branchs,
      repository.tags
    ).save(repository.rootPath)
  }

  def commit(repository: Repository, message: String): Unit = {
    var newCommit: Option[Commit] = null
    repository.lastCommit match {
      case None => {
        newCommit = Some(
          new Commit(
            repository.rootPath,
            repository.stage.sha1,
            message,
            LocalDate.now.toString,
            None,
            repository.stage.blops
          )
        )
      }
      case Some(value) => {
        if (value.name != repository.stage.sha1) {
          newCommit = Some(
            new Commit(
              repository.rootPath,
              repository.stage.sha1,
              message,
              LocalDate.now.toString,
              Some(value),
              repository.stage.blops
            )
          )
        }
      }
    }

    // On met à jour le répertoire complet
    new Repository(
      repository.rootPath,
      repository.workingDirectory,
      repository.stage,
      repository.head,
      if (newCommit == null) repository.lastCommit else newCommit,
      repository.branchs,
      repository.tags
    ).save(repository.rootPath)
  }

  /*
  def log(rootPath: String, p: Boolean, stat: Boolean): Unit = {
    if (p) {} else if (stat) {} else {
      val logs = FileManager.readFile(s"${rootPath}${sep}.sgit${sep}LOGS")
      MessagePrinter.printlog(Console.BLUE_B, logs)
    }
  }

  def newBranch(rootPath: String, branchName: String): Unit = {
    val newBranchFile = s"${rootPath}${sep}.sgit${sep}branchs${sep}" + branchName
    if (new File(newBranchFile).exists()) {
      MessagePrinter.printSimpleMessage(Console.RED, "Branch already exists")
    } else {
      FileManager.createFileOrDirectory(newBranchFile, false)
      val lastCommit = FileManager.readFile(s"${rootPath}${sep}.sgit${sep}REF")
      FileManager.writeFile(newBranchFile, lastCommit)
      FileManager.writeFile(s"${rootPath}${sep}.sgit${sep}HEAD", branchName)
    }
  }

  def checkout(rootPath: String, name: String, wd: WorkingDirectory): Unit = {
    val commit = FileTools.findCommit(rootPath, name)
    commit match {
      case Some(commit) => {
        val sha1Commit = commit.getName
        val sha1Stage =
          DigestUtils.sha1Hex(
            FileManager.readFile(s"${rootPath}${sep}.sgit${sep}STAGE")
          )
        if (sha1Stage == sha1Commit) {
          val untrackedFiles = wd.getUntrackedFiles
          FileManager.cleanDirectory(rootPath);
          FileTools.checkoutFromCommit(rootPath, commit)
          FileManager.writeFile(s"${rootPath}${sep}.sgit${sep}REF", sha1Commit)
          untrackedFiles.foreach(
            f => FileManager.createFileOrDirectory(f.getCanonicalPath, false)
          )
        } else {
          MessagePrinter.printSimpleMessage(
            Console.RED,
            "Stage and last commit different, please commit before checkout"
          )
        }
      }
      case None =>
        MessagePrinter.printSimpleMessage(
          Console.RED,
          s"No tag/branch/commit with this name : ${name}"
        )
    }
  }

  def newTag(rootPath: String, tagName: String): Unit = {
    val newTagFile = s"${rootPath}${sep}.sgit${sep}tags${sep}${tagName}"
    if (new File(newTagFile).exists()) {
      MessagePrinter.printSimpleMessage(Console.RED, "Tag already exists")
    } else {
      FileManager.createFileOrDirectory(newTagFile, false)
      val lastCommit = FileManager.readFile(s"${rootPath}${sep}.sgit${sep}REF")
      FileManager.writeFile(newTagFile, lastCommit)
    }
  }

  def merge: Unit = {}

  def rebase: Unit = {}

  def listTagsAndBranch(rootPath: String): Unit = {
    MessagePrinter.printNameFile(
      Console.WHITE,
      "Tags",
      FileTools.listTags(rootPath)
    )
    MessagePrinter.printNameFile(
      Console.BLUE,
      "Branchs",
      FileTools.listBranchs(rootPath)
    )
  }
 */
}
