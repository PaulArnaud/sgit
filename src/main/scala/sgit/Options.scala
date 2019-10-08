package sgit

import sgit.FileTools
import sgit.Information
import java.io.File
import java.nio.file.Files

object Options {

    def init() : Unit = {
        FileTools.createRepo()
    }

    def status(repository: File) : Unit = {
        val files = FileTools.recursiveListFiles(repository.getParentFile())
            .filter( f => !f.getCanonicalPath().contains("/.sgit"))
        val (untracked, unmodified, modified, deleted) = getStatusWithFiles(files, repository)
        Information.showUntrackedFiles(untracked)
        Information.showModifiedFiles(modified)
        Information.showDeletedFiles(deleted)
    }

    def diff() : Unit = {

    }

    def add(files: Seq[String], repository: File) : Unit = {
        val allFiles = files
            .map( s => new File(s))/*
            .flatMap( e => FileTools.recursiveListFiles(e) )
            .filter( e => !e.getCanonicalPath().contains(".sgit") )
            .filter( e => !e.isDirectory() )*/
        
        val (untracked, unmodified, modified, deleted) = getStatusWithFiles(allFiles, repository) 
        untracked.foreach( f => FileTools.addToStage(FileTools.createBlop(f, repository), repository) )
        modified.foreach( f => FileTools.updateStage(f, repository))
        deleted.foreach( f => FileTools.deletInStage(f, repository))
    }

    def commit() : Unit = {

    }

    def log() : Unit = {

    }

    def branch() : Unit = {

    }

    def checkout() : Unit = {

    }

    def tag() : Unit = {

    }

    def merge() : Unit = {

    }

    def rebase() : Unit = {

    }

    def getStatusWithFiles(files: Seq[File], repository: File) : (Seq[File], Seq[File], Seq[File], Seq[File]) = {
        val deleted = FileTools.getDeletedFiles(files, repository)
        val (tracked, untracked) = files.partition( f => FileTools.isInStage(f, repository) )
        val (modified, unmodified) = tracked.partition( f => FileTools.wasModified(f, repository))
        return (untracked, unmodified, modified, deleted)
    }
}