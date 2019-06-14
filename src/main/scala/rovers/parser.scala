package rovers

import atto._
import Atto._
import rovers.domain._

object parser {

  /**
    * Parse the message from the NASA.
    * @param input The message
    * @return Either the settings (plateau, and rovers' positions and instructions), or an error.
    */
  def parseMessage(input: String): Either[String, (Plateau, List[(RoverPosition, List[Direction])])] = (world parseOnly input).either

  private val plateau: Parser[Plateau] = pairBy(int, whitespace, int) -| { case (w,h) => Plateau(w+1,h+1) }

  private val roverPosition: Parser[RoverPosition] = for {
    x <- int
    _ <- whitespace
    y <- int
    _ <- whitespace
    h <- oneOf("NSWE") -| toHeading
  } yield RoverPosition(x,y,h)

  private val moves: Parser[List[Direction]] = many(oneOf("MRL") -| toDirection)

  private val rover: Parser[(RoverPosition, List[Direction])] = pairBy(roverPosition, char('\n'), moves)

  private val rovers: Parser[List[(RoverPosition, List[Direction])]] = rover sepBy char('\n')

  private val world: Parser[(Plateau, List[(RoverPosition, List[Direction])])] = for {
    p <- plateau
    _ <- char('\n')
    r <- rovers
  } yield (p, r)

  private def toHeading(h: Char): Heading = h match {
    case 'N' => North
    case 'S' => South
    case 'W' => West
    case 'E' => East
    case _ => North // cannot happen, parser would fail before
  }


  private def toDirection(d: Char): Direction = d match {
    case 'M' => Forward
    case 'L' => Left
    case 'R' => Right
    case _ => Forward // cannot happen, parser would fail before
  }
}
