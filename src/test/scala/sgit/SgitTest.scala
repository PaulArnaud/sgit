package sgit

import java.io.File
import org.scalatest._

class SgitTest extends FlatSpec with Matchers {
  "Directory" should "be create" in {
    val a = new File(".sgit")
    println(a)
    assert(File(".sgit").exists())
  }
}