package sgit

import org.scalatest._

class SgitTest extends FunSpec with FlatSpec with Matchers {
  "Directory" should "be create" in {
    new File(".sgit")
    assert(new File(".sgit").exists())
  }
}
}