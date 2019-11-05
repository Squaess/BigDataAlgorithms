package problem3

object Problem3 extends App {
  val text = Iterator.
    continually(scala.io.StdIn.readLine).
    takeWhile(_ != "END").
    mkString("\n")

  println(text)

}
