package sgit

import java.io.File
import org.scalatest._

class SgitTest extends FlatSpec with Matchers {
  "Directory" should "be create" in {
    new File(".sgit")
    assert(new File(".sgit").exists())
  }
}