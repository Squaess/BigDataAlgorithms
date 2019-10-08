package stopwords

import scala.io.Source
import scala.io.Codec

object StopWords {
  val stopWords_en = getStopWords("stopwords_en.txt")
  val stopwords_pl = getStopWords("stopwords_pl.txt")

  private def getStopWords(fileName:String):Array[String] ={
    Source.fromResource("stopwords/".concat(fileName))(Codec("UTF-8"))
      .mkString
      .split("\\s+")
  }
}
