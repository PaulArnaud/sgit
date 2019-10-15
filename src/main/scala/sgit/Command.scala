package sgit

import java.io.File
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.time.Instant

object Command {
    
    def init : Unit = {
        FileTools.createRepo
    }

    def status(wd: WorkingDirectory) : Unit = {
        MessagePrinter.printFiles(Console.RED, "Deleted Files", wd.getDeletedFiles)
        MessagePrinter.printFiles(Console.YELLOW, "Untracked Files", wd.getUntrackedFiles)
        MessagePrinter.printFiles(Console.BLUE, "Modified Files", wd.getModifiedAndUnmodifiedFiles._2)
    }

    def diff : Unit = {

    }

    def add(rootPath: String, strings: Array[String], wd: WorkingDirectory) : Unit = {
        val (files, directorys) = strings.map( s => new File(s)).partition( f => f.isFile )

        val allFiles = files ++ directorys.flatMap( d => FileTools.listFilesInDirectory(d) )

        val (inDirectory, outDirectory) = wd.contains(allFiles)
        MessagePrinter.printFiles(Console.MAGENTA, "Fatal : out of repository", outDirectory)

        inDirectory.foreach( f => {
            val stageContent = FileTools.readFile(rootPath+"/.sgit/STAGE")
            if (stageContent.contains(f.getCanonicalPath)) {
                Stage.update(rootPath, f)
            }
            else {
                Stage.add(rootPath, f)
            }
        })

    }

    def commit(rootPath: String, wd: WorkingDirectory, commitName : String) : Unit = {
        val stageContent = FileTools.readFile(rootPath + "/.sgit/STAGE")
        val sha1stage = DigestUtils.sha1Hex(stageContent).toString
        val fileName = rootPath + "/.sgit/objects/" + sha1stage
        val lastCommit = FileTools.readFile(rootPath+"/.sgit/REF")
        val commitFirstLine = sha1stage + " " + commitName + " " + Instant.now.toString +" "+ lastCommit + "\n"
        /* Création du commit : la première ligne est composée du nom du commit , de la date 
        et du commit précédent */

        FileTools.createFileOrDirectory(fileName, false)
        FileTools.writeFile(fileName, commitFirstLine + stageContent)

        /* Mise à jour de la référence de la branche qui pointait sur le dernier commit, du dernier commit
        qui devient celui-ci et rajout de la ligne dans les logs */

        FileTools.writeFile(rootPath+"/.sgit/REF", sha1stage)
        FileTools.addLineInFile(rootPath+"/.sgit/LOGS", commitFirstLine )
        val actualBranch = FileTools.readFile(rootPath+"/.sgit/HEAD")
        FileTools.writeFile(rootPath+"/.sgit/branch/"+ actualBranch, sha1stage)
    }

    def log(rootPath: String, p: Boolean, stat: Boolean) : Unit = {
        if (p) {

        }
        else if (stat){

        }
        else {
            val logs = FileTools.readFile(rootPath + "/.sgit/LOGS")
            println(logs)
        }

    }

    def newBranch(rootPath: String, branchName: String) : Unit = {
        val newBranchFile = rootPath + "/.sgit/branchs/" + branchName
        FileTools.createFileOrDirectory(newBranchFile, false)
        val lastCommit = FileTools.readFile(rootPath + "/.sgit/REF")
        FileTools.writeFile(newBranchFile, lastCommit)
        FileTools.writeFile(rootPath + "/.sgit/HEAD", branchName)
    }

    def checkout(rootPath: String, name: String) : Unit = {
        val commit = FileTools.findCommit(rootPath, name)
        commit match {
            case Some(commit) => {
                FileUtils.cleanDirectory(new File(rootPath)); 
                FileTools.checkoutFromCommit(rootPath, commit)
                FileTools.writeFile(rootPath + "/.sgit/REF", commit.getName)
            }
            case None => println(Console.RED + "No tag/branch/commit with this name : " + name )
        }

    }

    def newTag(rootPath: String, tagName: String) : Unit = {
        val newTagFile = rootPath + "/.sgit/tags/" + tagName
        FileTools.createFileOrDirectory(newTagFile, false)
        val lastCommit = FileTools.readFile(rootPath + "/.sgit/REF")
        FileTools.writeFile(newTagFile, lastCommit)
    }

    def merge : Unit = {

    }

    def rebase : Unit = {

    }

    def listTagsAndBranch(rootPath: String) : Unit = {
        MessagePrinter.printNameFile(Console.WHITE, "Tags", FileTools.listTags(rootPath))
        MessagePrinter.printNameFile(Console.BLUE, "Branchs", FileTools.listBranchs(rootPath))
    }

}