package rovers

import cats.{Foldable, Show}
import cats.implicits._
import rovers.domain.RoverPosition

/**
  * This object contains all the domain model and behaviour of the application.
  *
  * The domain functions return an Either, to encode the failure and success cases in the type. These are all pure functions.
  */
object domain {

  type Size = Int

  /**
    * A plateau is a rectangle. Please note that Size is an integer, and can be negative. But in the application, it
    * should not be possible as the parser doesn't validate negative numbers.
    * @param width
    * @param height
    */
  final case class Plateau(width: Size, height: Size)

  /**
    * The header, as an enum.
    */
  sealed trait Heading

  case object North extends Heading
  case object South extends Heading
  case object West extends Heading
  case object East extends Heading

  type Coordinate = Int
  final case class RoverPosition(x: Coordinate, y: Coordinate, heading: Heading)

  /**
    * The direction, as an enum.
    */
  sealed trait Direction
  case object Forward extends Direction
  case object Left extends Direction
  case object Right extends Direction

  sealed trait RoverMoveError
  final case object RoverLostInSpace extends RoverMoveError

  /**
    * Move a rover. This is the most important function of the application (thus the most tested); all other business
    * functions use it directly or indirectly.
    * @param plateau The plateau
    * @param roverPosition The initial position
    * @param to The direction of the move
    * @return Either the new position of the rover, or an error, which indicates that the rover went out of the plateau.
    */
  def move(plateau: Plateau)(roverPosition: RoverPosition, to: Direction): Either[RoverMoveError, RoverPosition] = (roverPosition, to) match {
    case (RoverPosition(x, y, North), Forward) if y < plateau.height - 1 => RoverPosition(x, y + 1, North).asRight
    case (RoverPosition(x, y, West), Forward) if x > 0 => RoverPosition(x - 1, y, West).asRight
    case (RoverPosition(x, y, South), Forward) if y > 0 => RoverPosition(x, y - 1, South).asRight
    case (RoverPosition(x, y, East), Forward) if x < plateau.width - 1 => RoverPosition(x + 1, y, East).asRight
    case (RoverPosition(x, y, North), Left) => RoverPosition(x, y, West).asRight
    case (RoverPosition(x, y, West), Left) => RoverPosition(x, y, South).asRight
    case (RoverPosition(x, y, South), Left) => RoverPosition(x, y, East).asRight
    case (RoverPosition(x, y, East), Left) => RoverPosition(x, y, North).asRight
    case (RoverPosition(x, y, North), Right) => RoverPosition(x, y, East).asRight
    case (RoverPosition(x, y, West), Right) => RoverPosition(x, y, North).asRight
    case (RoverPosition(x, y, South), Right) => RoverPosition(x, y, West).asRight
    case (RoverPosition(x, y, East), Right) => RoverPosition(x, y, South).asRight
    case _ => RoverLostInSpace.asLeft
  }

  /**
    * Makes a rover explore a plateau, i.e. apply many moves to a rover.
    * @param plateau The plateau to explorer
    * @param roverPosition The initial position
    * @param moves The instructions for moving
    * @return Either the position after the exploration, or an error, typically if the rover went out of the plateau
    */
  def explore(plateau: Plateau)(roverPosition: RoverPosition, moves: List[Direction]): Either[RoverMoveError, RoverPosition] =
    Foldable[List].foldM(moves, roverPosition)((p: RoverPosition, to: Direction) => {
      move(plateau)(p, to)
    })

  /**
    * Make all rovers explore the plateau.
    * @param plateau The plateau to explore.
    * @param rovers Each Rover, along its move instructions
    * @return Either the list of final positions, or an error
    */
  def runAllRovers(plateau: Plateau)(rovers: List[(RoverPosition, List[Direction])]): Either[RoverMoveError, List[RoverPosition]] =
    rovers.traverse(Function.tupled(explore(plateau)))

}

object implicits {
  implicit val showRoverPosition: Show[RoverPosition] = (roverPosition: RoverPosition) => s"${roverPosition.x} ${roverPosition.y} ${roverPosition.heading.toString.head}"
}
