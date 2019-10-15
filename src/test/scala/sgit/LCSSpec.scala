package sgit 

import org.scalatest.FunSuite

class LCSTest extends FunSuite {

    test("Compute the LCS on simple value : abc & aaaabbbbcccc"){
       val a = "abc"
       val b = "aaaabbbbcccc"
       val testedValue = LCS.lcsRec(a,b).toArray

       assert(testedValue === Array('a', 'b', 'c'))
    }

    test("Compute the LCS on complex value : Array(String) & Array(String)"){
        val a = Array("php File", "def function a ", "}")
        val b = Array("php File", "}", "end of file ")

        val testedValue = LCS.lcsRec(a,b).toArray

        assert(testedValue === Array("php File","}"))
    }

    test("Compute the LCS with long string"){
       val a = "AAAONFPD"
       val b = "AONIVIPK"
       val testedValue = LCS.lcsRec(a,b)
       println(testedValue)

       assert(testedValue.toArray.size != 0)
    }

}