package sgit 

import org.scalatest.FunSuite

class LCSTest extends FunSuite {

    test("REC Compute the LCS on simple value : abc & aaaabbbbcccc"){
       val a = "abc"
       val b = "aaaabbbbcccc"
       val testedValue = LCS.lcsRec(a,b).toArray

       assert(testedValue === Array('a', 'b', 'c'))
    }

    test("REC Compute the LCS on complex value : Array(String) & Array(String)"){
        val a = Array("php File", "def function a ", "}")
        val b = Array("php File", "}", "end of file ")

        val testedValue = LCS.lcsRec(a,b).toArray

        assert(testedValue === Array("php File","}"))
    }

    test("DP Compute the LCS on simple value : Array(String) & Array(String)"){
        val a = Seq("php File", "def function a ", "}")
        val b = Seq("php File", "}", "end of file ")

        val testedValue = LCS.lcsDP(a, b)

        assert(testedValue == Seq("php File", "}"))
    }

    test("DP Compute the LCS on complex value : Array(String) & Array(String)"){
        val a = Seq(
            "iqxjonzaykqkddtnweyc",
            "tlqundzprfuprqoixjed",
            "qbctalsjhvojzwodwmpd",
            "zfgwzhansjrfaubnbtdt",
            "nbeeeidptbcqupjkaidn",
            "rdfcepdprgjbgchktmds",
            "acrirhdykzcwlmsijpea",
            "cdzgwjpjdzxybzzatwve",
            "tjshrbttlsdgvcqvtxvb",
            "hoexpsmrgjwkhaugityg",
            "mmtwdadlpphgqhngpyaz",
            "ybosylmggtnaqucwnikf",
            "lzhzaucxaxrkeckeywjf",
            "wxltuigjzkndtydzjmpa",
            "yajwjnidpxrrhkwaknti")

        val b = Seq(
            "iqxjonzaykqkddtnweyc",
            "fxppsiyxiexvzbjcmqtv",
            "dirgqwuvrdkrpzntjvkh",
            "zpcwvuptrxonoblxzyaa",
            "wxwmxtvuuqagdqlyppdb",
            "pwtuqjjmjaftagsoecvu",
            "mmtwdadlpphgqhngpyaz",
            "uacamlntwcqodzcogtcs",
            "etyursbuxhbvbozyqqik",
            "filvbsuymytoqppeiqhy",
            "tbwxdtuidkmoyqpwxkxq",
            "msaqulbsypozdpzbkeal",
            "exdkebofglvlcganvrzx",
            "yajwjnidpxrrhkwaknti",
            "kvvjradvevnmlwjdrsvi")

        val testedValue = LCS.lcsDP(a, b)

        assert(testedValue == Seq("iqxjonzaykqkddtnweyc", "mmtwdadlpphgqhngpyaz", "yajwjnidpxrrhkwaknti"))
    }

}