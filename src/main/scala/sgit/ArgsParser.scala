package sgit

import scopt.OParser

case class Config(command: String = "", name: String = "")
object ArgsParser {
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
            cmd("add").text("Add file contents to the index").action((_, c) => c.copy(command = "add")),
            // Diff option
            cmd("diff").text("Show changes between commits, commit and working tree, etc").action((_, c) => c.copy(command = "diff")),
            // Commit option
            cmd("commit").text("Record changes to the repository").action((_, c) => c.copy(command = "commit")),
            // Log option
            cmd("log").text("Show commit logs").action((_, c) => c.copy(command = "log")),
            // Branch option
            cmd("branch").text("List, create, or delete branches").action((_, c) => c.copy(command = "branch")),
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
}