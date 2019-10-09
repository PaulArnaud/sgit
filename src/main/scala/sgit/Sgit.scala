package sgit 

import sgit.Options
import sgit.FileTools

import scopt.OParser
import java.io.{File => JavaFile}
import java.rmi.activation.ActivationGroupDesc.CommandEnvironment

object Sgit {
    def main(args: Array[String]): Unit = {

    case class Config(
        command: String = "", 
        files: Seq[String] = Seq(), 
        p: Boolean = false,
        stats: Boolean = false,
        branch_tag_commit: String = "",
        a: Boolean = false,
        v: Boolean = false
    )

    val builder = OParser.builder[Config]

    val parser1 = {
        import builder._
        OParser.sequence(
            programName("sgit"),
            head("scopt", "4.x"),
            help("help").text("List explicit of all usages"),
            // Init option
            cmd("init").text("Initialize a repository").action((_, c) => c.copy(command = "init")),
            // Status option
            cmd("status").text("Show the working tree status").action((_, c) => c.copy(command = "status")),
            // Add option
            cmd("add")
                .text("Add file contents to the index")
                .action((_, c) => c.copy(command = "add"))
                .children(
                    arg[String]("<file>...")
                    .unbounded()
                    .required()
                    .action((x, c) => c.copy(files = c.files :+ x))
                    .text("files to add")),
            // Diff option
            cmd("diff").text("Show changes between commits, commit and working tree, etc").action((_, c) => c.copy(command = "diff")),
            // Commit option
            cmd("commit").text("Record changes to the repository").action((_, c) => c.copy(command = "commit")),
            // Log option
            cmd("log")
                .text("Show commit logs")
                .action((_, c) => c.copy(command = "log"))
                .children(
                    opt[Unit]("p")
                        .text("Show changes overtime")
                        .action((_, c) => c.copy(p = true)),
                    opt[Unit]("stats")
                        .text(
                        "show the stats of insertion and deletion of each commited file"
                        )
                        .action((_, c) => c.copy(stats = true))
                ),
            // Branch option
            cmd("branch")
                .text("List, create, or delete branches")
                .action((_, c) => c.copy(command = "branch"))
                .children(
                    arg[String]("name")
                        .required()
                        .action((x, c) => c.copy(branch_tag_commit = x))
                        .text("name of the branch"),
                    opt[Unit]("a")
                        .action((_, c) => c.copy(a = true))
                        .text("display all branches"),
                    opt[Unit]("v")
                        .action((_, c) => c.copy(v = true))
                        .text("show hash and commit subject line for each branch's head")
                ),
            // Checkout option
            cmd("checkout").text("Switch branches or restore working tree files").action((_, c) => c.copy(command = "checkout")),
            // Tag option
            cmd("tag").text("Initialize a repository").action((_, c) => c.copy(command = "tag")),
            // Merge option
            cmd("merge").text("Join two or more development histories together").action((_, c) => c.copy(command = "merge")),
            // Rebase option
            cmd("rebase").text("Reapply commits on top of another base tip").action((_, c) => c.copy(command = "rebase")),
        )
    }

    OParser.parse(parser1, args, Config()) match {
        case Some(config) => {
            config.command match {
                case "init" => {
                    Command.init()
                }
                case _ => {
                    Repository.getRoot match {
                        case Some(root) => {
                            config.command match {
                                case "add" => {
                                    Command.add(root, config.files)
                                }
                                case "status" => {
                                }
                            }
                        }
                        case None => {
                            println("No repository found. Please try to initialize one \n--> sgit init")
                        }
                    }   
                }
            }
        }
        case _ => println("Error : no valids arguments, please read the help \n-> sgit --help")
    }

    }
}