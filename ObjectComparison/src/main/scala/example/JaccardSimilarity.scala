package example
import scala.io.Source
import java.io.FileNotFoundException
import scala.io.Codec
import stopwords.StopWords.stopwords_pl
import scala.util.hashing.MurmurHash3

class JaccardSimilarity(File1: String, File2: String, k: Int, h: Int){

    def readFile(fileName: String, norm: String => String): String = {
        val bufferSource = Source.fromFile(fileName)(Codec("UTF-8"))
        val text = norm(bufferSource.getLines.mkString(" "))
        bufferSource.close
        text
    }

    def normalize(content: String): String = {
        content
        .toLowerCase
        .replaceAll(",|\\.|!|\\?|\\n", " ")
        .split("\\s+").filterNot(stopwords_pl.contains(_)).mkString(" ")
    }

    val text1 = readFile(File1, normalize)
    val text2 = readFile(File2, normalize)

    def make_k_shingle(text: String, k: Int): Seq[String] = {
            for (i <- text.indices if i < (text.length - k)) yield text.slice(i,i+k)
    }

    val text1_shingle = make_k_shingle(text1, k).toSet
    val text2_shingle = make_k_shingle(text2, k).toSet

    def jaccard_sim(set1: Set[String], set2: Set[String]): Double = {
        (set1 intersect set2).size / (set1 union set2).size.toDouble
    }



    val h1: MurmurHash3.type = MurmurHash3
    // List of all distinct words
    val rows = (text1.split("\\s+").toSet union text2.split("\\s+").toSet).toSeq.sorted
    // var minHash1: Array[Int] = Array.fill(h)(Int.MaxValue)
    // var minHash2: Array[Int] = Array.fill(h)(Int.MaxValue)
    val text1_splited = text1.split("\\s+").toSet
    val text2_splited = text2.split("\\s+").toSet
    // for (i <- 0 until h){
    //     //calculate has values fo the rows for the i-th hash function
    //     val hashed1 = rows.map(
    //         x => if ( h1.stringHash(x, i) % rows.size >= 0 ) h1.stringHash(x, i) % rows.size
    //                 else h1.stringHash(x, i) % rows.size + rows.size)
    //     // get indexes from rows, fot the values of text1 and text2
    //     val indexes1 = for (w: String<- text1_splited) yield rows.indexOf(w)
    //     val indexes2 = for (w: String<- text2_splited) yield rows.indexOf(w)

    //     minHash1(i) = hashed1.zipWithIndex.filter(x => indexes1 contains x._2).minBy(x => x._1)._1
    //     minHash2(i) = hashed1.zipWithIndex.filter(x => indexes2 contains x._2).minBy(x => x._1)._1
    // }
    // val minhashed_value = (minHash1 zip minHash2).filter( x => x._1 == x._2).length / minHash1.length.toDouble
    // println(hashed1.zipWithIndex.filter(x => indexes contains x._2))
    // println(hashed1.take(100))
    // println(rows.size)
    // println(rows.take(100))

    def genHashes(h: Int, rows: Seq[String]) = {
        (0 until h).map(
            x => x -> rows.map(
            y => if ( h1.stringHash(y, x) % rows.size >= 0 ) h1.stringHash(y, x) % rows.size
                    else h1.stringHash(y, x) % rows.size + rows.size
            )
        )
    }

    def getSignature(indexes: Seq[Int], hashes: IndexedSeq[(Int, Seq[Int])]): Seq[Int] = {
        hashes.map( x =>
            x._2.filter( y => indexes contains x._2.indexOf(y)).min
        )
    }

    val hashes: IndexedSeq[(Int, Seq[Int])] = genHashes(h, rows)
    val minHash1 = getSignature(text1_splited.map(x => rows.indexOf(x)).toSeq, hashes)
    val minHash2 = getSignature(text2_splited.map(x => rows.indexOf(x)).toSeq, hashes)
    val minhashed_value = (minHash1 zip minHash2).filter( x => x._1 == x._2).length / minHash1.length.toDouble

    override def toString(): String = {
        s"Jaccard shignle: ${jaccard_sim(text1_shingle, text2_shingle)}" + "\n" +
        s"MinHash for h=$h: $minhashed_value"
    }
}