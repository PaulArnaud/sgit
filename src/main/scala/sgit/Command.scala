package sgit

import java.io.File
import org.apache.commons.codec.digest.DigestUtils
import java.time.Instant

object Command {

  def init: Unit = {
    FileTools.createRepo
  }

  def status(wd: WorkingDirectory): Unit = {
    MessagePrinter.printFiles(
      Console.RED,
      "Deleted Files",
      wd.getDeletedFiles
    )
    MessagePrinter.printFiles(
      Console.YELLOW,
      "Untracked Files",
      wd.getUntrackedFiles
    )
    MessagePrinter.printFiles(
      Console.BLUE,
      "Modified Files",
      wd.getModifiedAndUnmodifiedFiles._2
    )
  }

  def diff: Unit = {}

  def add(
      rootPath: String,
      strings: Array[String],
      wd: WorkingDirectory
  ): Unit = {
    val (files, directorys) =
      strings.map(s => new File(s)).partition(f => f.isFile)

    val allFiles = files ++ directorys.flatMap(
      d => FileTools.listFilesInDirectory(d)
    )

    val (inDirectory, outDirectory) = wd.contains(allFiles)
    MessagePrinter.printFiles(
      Console.MAGENTA,
      "Fatal : out of repository",
      outDirectory
    )

    inDirectory.foreach(f => {
      val stageContent = FileManager.readFile(rootPath + "/.sgit/STAGE")
      if (stageContent.contains(f.getCanonicalPath)) {
        Stage.update(rootPath, f)
      } else {
        Stage.add(rootPath, f)
      }
    })
  }

  def commit(
      rootPath: String,
      wd: WorkingDirectory,
      commitName: String
  ): Unit = {
    val stageContent = FileManager.readFile(rootPath + "/.sgit/STAGE")
    val sha1stage = DigestUtils.sha1Hex(stageContent).toString
    val lastCommit = FileManager.readFile(rootPath + "/.sgit/REF")
    /*val lastCommit = FileManager.readFile(
      rootPath + "/.sgit/branchs/" + FileManager
        .readFile(rootPath + "/.sgit/HEAD")
    )*/

    if (sha1stage == lastCommit) {
      MessagePrinter.printSimpleMessage(Console.WHITE, "Nothing to commit")
    } else {
      val fileName = rootPath + "/.sgit/objects/" + sha1stage
      val commitFirstLine = sha1stage + " " + commitName + " " + Instant.now.toString + " " + lastCommit + "\n"
      val actualBranch = FileTools.getBranch(rootPath)
      actualBranch match {
        case Some(value) => {
          FileManager.writeFile(
            rootPath + "/.sgit/branchs/" + actualBranch,
            sha1stage
          ) //mise à jour de la branche
        }
        case None => {
          FileManager.writeFile(
            rootPath + "./sgit/HEAD",
            sha1stage
          )
        }
      }

      FileManager.createFileOrDirectory(fileName, false) //creation de l'object commit
      FileManager.writeFile(fileName, commitFirstLine + stageContent) //application du contenu dans ce commit
      FileManager.writeFile(rootPath + "/.sgit/REF", sha1stage) //mise à jour de la référence du dernier commit
      FileManager.addLineInFile(rootPath + "/.sgit/LOGS", commitFirstLine) //mise à jour des logs
    }
  }

  def log(rootPath: String, p: Boolean, stat: Boolean): Unit = {
    if (p) {} else if (stat) {} else {
      val logs = FileManager.readFile(rootPath + "/.sgit/LOGS")
      MessagePrinter.printlog(Console.BLUE_B, logs)
    }
  }

  def newBranch(rootPath: String, branchName: String): Unit = {
    val newBranchFile = rootPath + "/.sgit/branchs/" + branchName
    if (new File(newBranchFile).exists()) {
      MessagePrinter.printSimpleMessage(Console.RED, "Branch already exists")
    } else {
      FileManager.createFileOrDirectory(newBranchFile, false)
      val lastCommit = FileManager.readFile(rootPath + "/.sgit/REF")
      FileManager.writeFile(newBranchFile, lastCommit)
      FileManager.writeFile(rootPath + "/.sgit/HEAD", branchName)
    }
  }

  def checkout(rootPath: String, name: String, wd: WorkingDirectory): Unit = {
    val commit = FileTools.findCommit(rootPath, name)
    commit match {
      case Some(commit) => {
        val sha1Commit = commit.getName
        val sha1Stage =
          DigestUtils.sha1Hex(FileManager.readFile(rootPath + "/.sgit/STAGE"))
        if (sha1Stage == sha1Commit) {
          val untrackedFiles = wd.getUntrackedFiles
          FileManager.cleanDirectory(rootPath);
          FileTools.checkoutFromCommit(rootPath, commit)
          FileManager.writeFile(rootPath + "/.sgit/REF", sha1Commit)
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
    val newTagFile = rootPath + "/.sgit/tags/" + tagName
    if (new File(newTagFile).exists()) {
      MessagePrinter.printSimpleMessage(Console.RED, "Tag already exists")
    } else {
      FileManager.createFileOrDirectory(newTagFile, false)
      val lastCommit = FileManager.readFile(rootPath + "/.sgit/REF")
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

}
