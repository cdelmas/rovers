package rovers

import org.scalacheck._
import org.scalatest._
import org.scalatest.prop._
import rovers.domain._

class RoversSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers with EitherValues {

  // heading to the north, move forward

  property("Given a plateau, moving forward while heading to the north inside the plateau increments the y axis") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4), Gen.choose(0, 3)) { (x: Int, y: Int) =>
      val initialPosition = RoverPosition(x, y, North)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('right) // cannot be otherwise as generators did choose valid values
    val pos = finalPosition.right.value
      pos should equal(RoverPosition(x, y + 1, North))
    }

  }

  property("Given a plateau, moving forward while heading to the north from the upper edge of the plateau leads to Rover going outside") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4)) { x: Int =>
      val initialPosition = RoverPosition(x, 4, North)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('left)
    }

  }

  // heading to the west, move forward

  property("Given a plateau, moving forward while heading to the west inside the plateau decrements the x axis") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(1, 4), Gen.choose(0, 4)) { (x: Int, y: Int) =>
      val initialPosition = RoverPosition(x, y, West)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('right) // cannot be otherwise as generators did choose valid values
    val pos = finalPosition.right.value
      pos should equal(RoverPosition(x - 1, y, West))
    }

  }

  property("Given a plateau, moving forward while heading to the west from the upper edge of the plateau leads to Rover going outside") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4)) { y: Int =>
      val initialPosition = RoverPosition(0, y, West)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('left)
    }

  }

  // heading to the south, move forward

  property("Given a plateau, moving forward while heading to the south inside the plateau decrements the y axis") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4), Gen.choose(1, 4)) { (x: Int, y: Int) =>
      val initialPosition = RoverPosition(x, y, South)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('right) // cannot be otherwise as generators did choose valid values
    val pos = finalPosition.right.value
      pos should equal(RoverPosition(x, y - 1, South))
    }

  }

  property("Given a plateau, moving forward while heading to the south from the upper edge of the plateau leads to Rover going outside") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4)) { x: Int =>
      val initialPosition = RoverPosition(x, 0, South)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('left)
    }

  }

  // heading to the east, move forward

  property("Given a plateau, moving forward while heading to the east inside the plateau increments the x axis") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 3), Gen.choose(0, 4)) { (x: Int, y: Int) =>
      val initialPosition = RoverPosition(x, y, East)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('right) // cannot be otherwise as generators did choose valid values
    val pos = finalPosition.right.value
      pos should equal(RoverPosition(x + 1, y, East))
    }

  }

  property("Given a plateau, moving forward while heading to the east from the upper edge of the plateau leads to Rover going outside") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4)) { y: Int =>
      val initialPosition = RoverPosition(4, y, East)
      val finalPosition = move(plateau)(initialPosition, Forward)
      finalPosition should be('left)
    }

  }

  // turn right and left

  property("Given a plateau, moving to the left or right leaves the position unchanged, only heading changes") {
    val plateau = Plateau(5, 5)

    forAll(Gen.choose(0, 4), Gen.choose(0, 4), Gen.oneOf(Left, Right), Gen.oneOf(North, East, South, West)) { (x: Int, y: Int, direction, heading) =>
      val initialPosition = RoverPosition(x, y, heading)
      val finalPosition = move(plateau)(initialPosition, direction)
      finalPosition should be('right) // cannot be otherwise as generators did choose valid values
      val pos = finalPosition.right.value
      pos.x should be(x)
      pos.y should be(y)
      pos.heading should not be heading
    }
  }
}

/**
  * This class implements the tests to verify the examples
  */
class ComplexRoverMove extends FlatSpec with Matchers with EitherValues {

  "The first rover" should "move" in {

    val plateau = Plateau(6, 6)
    val initialPosition = RoverPosition(1, 2, North)
    val moves = List(Left,Forward,Left,Forward,Left,Forward,Left,Forward,Forward)

    val finalPosition = explore(plateau)(initialPosition, moves)

    finalPosition should be('right)
    finalPosition.right.value should be(RoverPosition(1, 3, North))
  }

  "The second rover" should "move" in {

    val plateau = Plateau(6, 6)
    val initialPosition = RoverPosition(3, 3, East)
    val moves = List(Forward,Forward,Right,Forward,Forward,Right,Forward,Right,Right,Forward)

    val finalPosition = explore(plateau)(initialPosition, moves)

    finalPosition should be('right)
    finalPosition.right.value should be(RoverPosition(5, 1, East))
  }

}
