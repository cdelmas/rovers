package rovers

import java.nio.file.Path

import cats._
import org.scalatest._
import rovers.io._

import scala.collection.mutable

class RoversAppSpec extends FlatSpec with Matchers {

  val collector: mutable.MutableList[String] = mutable.MutableList()

  implicit val term = new Terminal[Id] {
    override def putStrLn(s: String): Id[Unit] = collector += s
  }

  implicit val fileAlgebra = new FileAlgebra[Id] {
    override def read(f: Path): Id[String] = "5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM"
  }

  "Application" should "report the expected report" in {

    Application[Id](term, fileAlgebra).program(null)

    collector should contain ("\n1 3 N\n5 1 E")
  }

}
