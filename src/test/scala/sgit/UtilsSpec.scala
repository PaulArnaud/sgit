package sgit

import org.scalatest.FunSuite
import objects._
    
class UtilsTest extends FunSuite {

    test("get the common part of seq"){
        val a = new Blop("a","a")
        val b = new Blop("b","p")
        val c = new Blop("b","u")
        val d = new Blop("b","y")
        val e = new Blop("c","t")
        val f = new Blop("d","r")
        val g = new Blop("e","a")

        val A = Seq(a,b,c,d,e)
        val B = Seq(b,c,d,e,f,g)

        assert(Utils.intersection(A,B) ==  Seq(b,c,d,e))
    }

    test("get the difference bewteen two list") {
        val a = new Blop("a","a")
        val b = new Blop("b","p")
        val c = new Blop("b","u")
        val d = new Blop("b","y")
        val e = new Blop("c","t")
        val f = new Blop("d","r")
        val g = new Blop("e","a")

        val A = Seq(a,b,c,d,e)
        val B = Seq(b,c,d,e,f,g)

        assert(Utils.difference(A, B) == Seq(a))
        assert(Utils.difference(B, A) == Seq(f, g))
    }

    test("pathCorrespondence should return blops wich have the good path criteri") {
        val a = new Blop("a","a")
        val b = new Blop("b","p")
        val c = new Blop("b","u")
        val d = new Blop("b","y")
        val e = new Blop("c","t")
        val f = new Blop("d","r")
        val g = new Blop("e","a")

        val A = Seq(a,b,c,d,e)
        val B = Seq(b,c,d,e,f,g)
        val A_B = Utils.difference(A, B)
        val B_A = Utils.difference(B ,A)

        assert(Utils.pathCorrespondence(A_B, B_A) == Seq(a))
        assert(Utils.pathCorrespondence(B_A, A_B) == Seq(g))
    }

}
// sbt "testOnly sgit.UtilsTest"
