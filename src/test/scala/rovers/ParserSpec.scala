package rovers

import org.scalatest._
import rovers.domain._
import rovers.parser.parseMessage

class ParserSpec extends FlatSpec with Matchers with EitherValues {

  "A parser" should "parse properly" in {

    val input = "5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM" // for some reason, multiline literal string doesn't work...

    val world = parseMessage(input)

    world should be ('right)
    val w = world.right.value
    w._1 should be(Plateau(6,6))
    w._2 should have size 2
    w._2.head should be((RoverPosition(1,2,North), List(Left,Forward,Left,Forward,Left,Forward,Left,Forward,Forward)))
    w._2(1) should be((RoverPosition(3,3,East), List(Forward,Forward,Right,Forward,Forward,Right,Forward,Right,Right,Forward)))
  }

}
