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
    MessagePrinter.printable(Console.RED, "Modified Files:", repository.modified)
    MessagePrinter.printable(Console.RED, "Untracked Files:", repository.untracked)
    MessagePrinter.printable(Console.RED, "Deleted Files:", repository.delete)
  }

  def diff(repository: Repository): Unit = {
    if (repository.modified.size != 0) {
      repository.getDiff.foreach(d => {
        MessagePrinter.printSimpleMessage(Console.GREEN, "File : ", d._1)
        MessagePrinter.printDiffMessage(Console.BLUE,"Lines added :", d._2, "+")
        MessagePrinter.printDiffMessage(Console.RED,"Lines deleted :", d._3, "-")
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

  
  def log(repository: Repository, p: Boolean, stat: Boolean): Unit = {
    if (p) {
      MessagePrinter.logP(Console.YELLOW, repository.lastCommit)
    } else if (stat) {} else {
      MessagePrinter.log(Console.YELLOW, repository.lastCommit)
    }
  }

  
  def newBranch(repository: Repository, branchName: String): Unit = {
    new Repository(
      repository.rootPath,
      repository.workingDirectory,
      repository.stage,
      repository.head,
      repository.lastCommit,
      repository.branchs :+ new Branch(repository.rootPath, branchName, repository.lastCommit),
      repository.tags
    ).save(repository.rootPath)
  }

  def newTag(repository: Repository, tagName: String): Unit = {
    new Repository(
      repository.rootPath,
      repository.workingDirectory,
      repository.stage,
      repository.head,
      repository.lastCommit,
      repository.branchs,
      repository.tags :+ new Tag(repository.rootPath, tagName, repository.lastCommit)
    ).save(repository.rootPath)
  }

  def listTagsAndBranchs(repository: Repository): Unit = {
    MessagePrinter.printable(Console.BLUE, "Branchs :", repository.branchs)
    MessagePrinter.printable(Console.MAGENTA_B, "Tags :", repository.tags)
  }

  /*
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

  
  def merge: Unit = {}

  def rebase: Unit = {}
 */
}
