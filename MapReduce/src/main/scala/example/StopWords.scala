package example

import scala.io.{Source, Codec}

class StopWords {
  val stopwords_pl = getStopWords("stopwords_pl.txt")

  private def getStopWords(fileName:String):Array[String] ={
    Source.fromFile("E:/Documents/University/BigDataAlgorithms/MapReduce/src/main/resources/stopwords/stopwords_pl.txt".concat(fileName))(Codec("UTF-8"))
      .mkString
      .split("\\s+")
  }
}