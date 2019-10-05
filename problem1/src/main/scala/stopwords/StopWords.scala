package stopwords

import scala.io.Source

object StopWords {
  private val stopWordsPath = getClass.getResource("/stopwords/stopwords_en.txt").getPath
  private val bufferSource = Source.fromFile(stopWordsPath, "UTF-8")
  val stopWords:Array[String] = bufferSource.mkString.split("\\s+")
  bufferSource.close
}
