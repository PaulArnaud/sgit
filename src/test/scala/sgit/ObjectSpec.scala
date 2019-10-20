package sgit

import org.scalatest.FunSuite
import objects._

class ObjectTest extends FunSuite {

    test("Blop Test") {
        pending
    }

    test("Commit Test") {
        val a = new Blop("a","a")
        val b = new Blop("b","p")
        val c = new Blop("b","u")
        val d = new Blop("b","y")
        val e = new Blop("c","t")
        val f = new Blop("d","r")
        val g = new Blop("e","a")

        val A = Seq(a,b,c,d,e)
        val B = Seq(b,c,d,e,f,g)

        val fc = new Commit("sha1", "a", "date 1", None, A)
        val sc = new Commit("sha2", "b", "date 2", Some(fc), B)

        assert(sc.father.get.name == "sha1")
    }

    test("Stage Test") {
        pending
    }

    test("Tag Test") {
        pending
    }

    test("Branch Test") {
        pending
    }

    test("Repository Test") {
        pending
    }

}