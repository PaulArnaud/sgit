package sgit

import scopt.OParser
import sgit.objects._

object Sgit {
  def main(args: Array[String]): Unit = {

    case class Config(
        command: String = "",
        files: Seq[String] = Seq(),
        p: Boolean = false,
        stat: Boolean = false,
        branch_tag_commit: String = "",
        av: Boolean = false
    )

    val builder = OParser.builder[Config]

    val parser1 = {
      import builder._
      OParser.sequence(
        programName("sgit"),
        head("scopt", "4.x"),
        help("help").text("List explicit of all usages"),
        // Init option
        cmd("init")
          .text("Initialize a repository")
          .action((_, c) => c.copy(command = "init")),
        // Status option
        cmd("status")
          .text("Show the working tree status")
          .action((_, c) => c.copy(command = "status")),
        // Add option
        cmd("add")
          .text("Add file contents to the index")
          .action((_, c) => c.copy(command = "add"))
          .children(
            arg[String]("<file>...").unbounded.required
              .action((x, c) => c.copy(files = c.files :+ x))
              .text("files to add")
          ),
        // Diff option
        cmd("diff")
          .text("Show changes between commits, commit and working tree, etc")
          .action((_, c) => c.copy(command = "diff")),
        // Commit option
        cmd("commit")
          .text("Record changes to the repository")
          .action((_, c) => c.copy(command = "commit"))
          .children(
            arg[String]("name")
              .required()
              .action((x, c) => c.copy(branch_tag_commit = x))
              .text("name of the commit")
          ),
        // Log option
        cmd("log")
          .text("Show commit logs")
          .action((_, c) => c.copy(command = "log"))
          .children(
            opt[Unit]("p")
              .text("Show changes overtime")
              .action((_, c) => c.copy(p = true)),
            opt[Unit]("stat")
              .text(
                "show the stats of insertion and deletion of each commited file"
              )
              .action((_, c) => c.copy(stat = true))
          ),
        // Branch option
        cmd("branch")
          .text("List, create, or delete branches")
          .action((_, c) => c.copy(command = "branch"))
          .children(
            arg[String]("name")
              .optional()
              .action((x, c) => c.copy(branch_tag_commit = x))
              .text("name of the branch"),
            opt[Unit]("av")
              .action((_, c) => c.copy(av = true))
              .text("display all branches")
          ),
        // Checkout option
        cmd("checkout")
          .text("Switch branches or restore working tree files")
          .action((_, c) => c.copy(command = "checkout")),
        // Tag option
        cmd("tag")
          .text("Initialize a repository")
          .action((_, c) => c.copy(command = "tag"))
          .children(
            arg[String]("name").required
              .action((x, c) => c.copy(branch_tag_commit = x))
              .text("name of the tag")
          ),
        // Merge option
        cmd("merge")
          .text("Join two or more development histories together")
          .action((_, c) => c.copy(command = "merge")),
        // Rebase option
        cmd("rebase")
          .text("Reapply commits on top of another base tip")
          .action((_, c) => c.copy(command = "rebase"))
      )
    }

    OParser.parse(parser1, args, Config()) match {
      case Some(config) => {
        config.command match {
          case "init" => {
            Command.init
          }
          case _ => {
            Repository.getRoot match {
              case Some(root) => {
                val rootPath = root.getParentFile.getCanonicalPath
                val workingDirectory: Seq[Blop] =
                  Initialization.importBlopsInDirectory(rootPath)
                val stage: Stage = Initialization.importStage(rootPath)
                val head: String = Initialization.getHead(rootPath)
                val lastCommit: Option[Commit] =
                  Initialization.getLastCommit(rootPath)
                val branchs: Seq[Branch] = Initialization.getBranchs(rootPath)
                val tags: Seq[Tag] = Initialization.getTags(rootPath)
                val repository = new Repository(
                  rootPath,
                  workingDirectory,
                  stage,
                  head,
                  lastCommit,
                  branchs,
                  tags
                )
                config.command match {
                  case "diff" => {
                    Command.diff(repository)
                  }
                  case "add" => {
                    Command.add(repository, config.files)
                  }
                  case "status" => {
                    Command.status(repository)
                  }
                  case "log" => {
                    Command.log(repository, config.p, config.stat)
                  }
                  case "branch" => {
                    if (config.av) {
                      Command.listTagsAndBranchs(repository)
                    } else {
                      Command.newBranch(repository, config.branch_tag_commit)
                    }
                  }
                  case "tag" => {
                    Command.newTag(repository, config.branch_tag_commit)
                  }
                  case "commit" => {
                    Command.commit(repository, config.branch_tag_commit)
                  }
                  case _ => {
                    println("Error : command not found")
                  }
                }
              }
              case None => {
                println(
                  MessagePrinter.printMessage(
                    Console.RED,
                    "No repository found. Please try to initialize one \n--> sgit init"
                  )
                )
              }
            }
          }
        }
      }
      case _ => MessagePrinter.printMessage(Console.RED, "Sorry")
    }
  }
}
