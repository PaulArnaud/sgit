package sgit

import java.io.File
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

object Command {
    
    def init() : Unit = {
        FileTools.createRepo()
    }

    def status(root: File, wd: WorkingDirectory) : Unit = {
        MessagePrinter.printFiles(Console.RED, "Deleted Files", wd.getDeletedFiles)
        MessagePrinter.printFiles(Console.YELLOW, "Untracked Files", wd.getUntrackedFiles)
        MessagePrinter.printFiles(Console.BLUE, "Modified Files", wd.getModifiedAndUnmodifiedFiles._2)
    }

    def diff() : Unit = {

    }

    def add(root: File, strings: Array[String], wd: WorkingDirectory) : Unit = {
        /*
        pour chaque fichier : on regarde 

        _ si il est situé dans le dossier de travail
            _ si il est traqué 
                _ si a pas été modifé remettre tout le stage
                _ si il a été modifié recréer tout le stage  
            _ si il est pas traqué  (remettre tout le stage + le fichier)



        
        val (files, directorys) = strings.map( s => new File(s)).partition( f => f.isFile() )

        val allFiles = files ++ directorys.flatMap( d => FileTools.listFilesInDirectory(d) )

        val (inDirectory, outDirectory) = wd.contains(allFiles)
        MessagePrinter.printFiles(Console.MAGENTA, "fatal : out of repository", outDirectory)

        val untrackedFiles = wd.getUntrackedFiles
        val filesToAdd = untrackedFiles.filter( f => untrackedFiles.contains(f) )
        */
    }

    def commit(root: File, wd: WorkingDirectory, commitName : String) : Unit = {
        val rootPath = root.getCanonicalPath()
        val stageContent = FileTools.readFile(rootPath + "/.sgit/STAGE")
        val sha1stage = DigestUtils.sha1Hex(stageContent)
        val fileName = rootPath + "/.sgit/objects/" + sha1stage
        FileTools.createFileOrDirectory(fileName, false)
        FileTools.writeFile(fileName, commitName + "\n" + stageContent)
        /*
        Update des branches
        Liaison avec le commit précédent
        */

    }

    def log() : Unit = {

    }

    def newBranch(root: File, branchName: String) : Unit = {
        /*
        create a new file with the name of the branch in the branch directory and a reference to the last commit
        */
        val newBranchFile = root.getCanonicalPath() + "/.sgit/branch/" + branchName
        FileTools.createFileOrDirectory(newBranchFile, false)
        val lastCommit = FileTools.readFile(root.getCanonicalPath() + "/.sgit/REF")
        FileTools.writeFile(newBranchFile, lastCommit)
    }

    def checkout(root: File, name: String) : Unit = {
        /*
        rebuild th working directory 
        */
        val commit = FileTools.findCommit(root, name)
        commit match {
            case Some(commit) => {
                FileUtils.cleanDirectory(root); 
                FileTools.checkoutCommit(root, commit)
            }
            case None => println(Console.RED + "No tag/branch/commit with this name : " + name )
        }

    }

    def tag(root: File, tagName: String) : Unit = {
        val newTagFile = root.getCanonicalPath() + "/.sgit/tag/" + tagName
        FileTools.createFileOrDirectory(newTagFile, false)
        val lastCommit = FileTools.readFile(root.getCanonicalPath() + "/.sgit/REF")
        FileTools.writeFile(newTagFile, lastCommit)
    }

    def merge() : Unit = {

    }

    def rebase() : Unit = {

    }

}