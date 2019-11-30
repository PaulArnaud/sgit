package sgit

import sgit.objects._
import sgit.filer._
import java.time.LocalDate
import org.apache.commons.codec.digest.DigestUtils
import java.io.File

object Command {

  def init: Unit = {
    FileTools.createRepo
    val path = new File(".").getCanonicalPath
    MessagePrinter.printMessage(Console.WHITE, s"Initialized empty Sgit repository in ${path}")
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

    MessagePrinter.printMessage(Console.WHITE, s"On branch : ${repository.head}") 
    if (repository.toCommit.length ==0 ) {
      MessagePrinter.printMessage(Console.WHITE, "Nothing to commit")
    } else {
      MessagePrinter.printable(
        Console.BLUE,
        "Files to be commited:",
        repository.toCommit
      )
    }

    MessagePrinter.printable(
      Console.YELLOW,
      "Modified Files:",
      repository.modified
    )
    MessagePrinter.printable(
      Console.GREEN,
      "Untracked Files:",
      repository.untracked
    )
    MessagePrinter.printable(Console.RED, "Deleted Files:", repository.delete)
  }

  def diff(repository: Repository): Unit = {
    if (repository.modified.size != 0) {
      repository.getDiff.foreach(d => {
        MessagePrinter.printSimpleMessage(Console.GREEN, "File : ", d._1)
        MessagePrinter
          .printDiffMessage(Console.BLUE, "   Lines added :", d._2, "+")
        MessagePrinter
          .printDiffMessage(Console.RED, "   Lines deleted :", d._3, "-")
      })
    }
  }

  def add(repository: Repository, strings: Seq[String]): Unit = {
    // On vérifie si les paramètres entrés sont des fichiers existants
    val existingFiles = FileTools.checkIfExisting(strings)

    // On trouve tous les fichiers si il y a des dossiers
    val allFiles = FileTools.getAllFiles(existingFiles)

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
    val newStageContent = FileTools.computeNewStage(repository, blops)

    // On met à jour le stage
    new Stage(newStageContent).save(repository.rootPath)
  }

  def commit(repository: Repository, message: String): Unit = {
    var newCommit: Option[Commit] = null

    if (!(repository.lastCommit.isDefined) || (repository.lastCommit.get.name != repository.stage.sha1)) {
      newCommit = Some(
        new Commit(
          repository.stage.sha1,
          message.replace(" ","_"),
          LocalDate.now.toString,
          repository.lastCommit,
          repository.stage.blops
        )
      )
    } else {
      newCommit = repository.lastCommit
    }

    // On met à jour le répertoire complet
    new Repository(
      repository.rootPath,
      repository.workingDirectory,
      repository.stage,
      repository.head,
      newCommit,
      repository.branchs,
      repository.tags
    ).save(repository.rootPath)
  }

  def log(repository: Repository, p: Boolean, stat: Boolean): Unit = {
    if (p) {
      logP(repository.lastCommit, repository.rootPath)
    } else if (stat) {
      MessagePrinter.printMessage(Console.RED, "Not implemented yet")
    } else {
      MessagePrinter.log(Console.YELLOW, repository.lastCommit)
    }
  }

  def logP(commit: Option[Commit], rootPath: String): Unit = {
    commit match {
      case None => MessagePrinter.printMessage(Console.WHITE, s"Initial Commit")
      case Some(value) => {
        value.father match {
          case None => {
            val diff = Utils.getCDUM(value.blops, Seq())
            //println(color + value.print)
            MessagePrinter.printMessage(Console.WHITE, value.print)
            MessagePrinter.printable(Console.GREEN,  "   New Files:", diff._3)
            MessagePrinter.printMessage(Console.WHITE, " ")
            MessagePrinter.printMessage(Console.WHITE, s"Initial Commit")
          }
          case Some(fathervalue) => {
            val diff = Utils.getCDUM(value.blops, fathervalue.blops)
            //println(color + value.print)
            MessagePrinter.printMessage(Console.WHITE, value.print)
            if (diff._4.size > 0) {
              MessagePrinter.printMessage(Console.WHITE, "   Diff :")
              val length = diff._4.size
              for ( blop <- 0 to length-1) {
                val newBlop = FileTools.getBlopContent(diff._4(blop), rootPath)
                val oldBlop = FileTools.getBlopContent(diff._5(blop), rootPath)
                val lcs = LCS.lcsDP(oldBlop, newBlop)
                val linesAdded = oldBlop.diff(lcs)
                val linesDeleted = newBlop.diff(lcs)
                MessagePrinter.printSimpleMessage(Console.GREEN, "      File : ", diff._4(blop).filePath)
                MessagePrinter.printDiffMessage(Console.BLUE, "      Lines added :", linesAdded, "+")
                MessagePrinter.printDiffMessage(Console.RED, "      Lines deleted :", linesDeleted, "-")
              }
            }
            //printable(Console.YELLOW, "   Modified Files:", diff._4)
            MessagePrinter.printable(Console.GREEN, "   New Files:", diff._3)
            MessagePrinter.printable(Console.RED, "   Deleted Files:", diff._2)
            //println(color + fathervalue.print)
            MessagePrinter.printMessage(Console.WHITE, " ")
            //println(fathervalue.print)
            logP(Some(fathervalue), rootPath)
          }
        }
      }
    }
  }

  def newBranch(repository: Repository, branchName: String): Unit = {
    repository.lastCommit match {
      case None => MessagePrinter.printMessage(Console.RED, "No commit")
      case Some(value) => {
        if (!repository.branchs.map(branch => branch.name).contains(branchName)) {
          new Branch(branchName, value.name).save(repository.rootPath)
        } else {
          MessagePrinter.printMessage(Console.YELLOW, s"Branch ${branchName} already exists")
        }
      }
    }
  }

  def newTag(repository: Repository, tagName: String): Unit = {
    repository.lastCommit match {
      case None => MessagePrinter.printMessage(Console.RED, "No commit")
      case Some(value) => {
        if (!repository.tags.map(tag => tag.name).contains(tagName)) {
          new Tag(tagName, value.name).save(repository.rootPath)
        } else {
          MessagePrinter.printMessage(Console.YELLOW, s"Tag ${tagName} already exists")
        }
      }
    }
  }

  def listTagsAndBranchs(repository: Repository): Unit = {
    MessagePrinter.printable(Console.BLUE, "Branchs :", repository.branchs)
    MessagePrinter.printable(Console.YELLOW, "Tags :", repository.tags)
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
