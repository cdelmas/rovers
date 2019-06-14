package rovers

import java.nio.file.Path

import domain._
import rovers.implicits._
import parser._
import cats._
import cats.implicits._
import cats.effect.IO
import com.monovore.decline._
import _root_.rovers.io.{ConsoleTerminal, FileAlgebra, FileInterpreter, Terminal}

class Application[F[_] : Monad](term: Terminal[F], file: FileAlgebra[F]) {

  val program: Path => F[Unit] = (path: Path) => for {
    _ <- term.putStrLn("Running")
    msg <- file.read(path)
    w = parseMessage(msg)
    roversMoveReport = w flatMap { case (plateau, mvs) =>
      runAllRovers(plateau)(mvs)
    } map displayPositions
    _ <- roversMoveReport.fold(err => term.putStrLn(s"Error occurred: $err"), term.putStrLn)
    _ <- term.putStrLn("The end")
  } yield ()

  private def displayPositions(positions: List[RoverPosition]): String =
    positions.foldLeft("")((acc, pos) => acc + '\n' + Show[RoverPosition].show(pos))
}

object Application {
  def apply[F[_] : Monad](term: Terminal[F], file: FileAlgebra[F]): Application[F] = new Application(term, file)
}

object RoversApp extends CommandApp(name = "Rovers", header = "*** Rovers ***", main = {

  val program = Application[IO](new ConsoleTerminal[IO], new FileInterpreter[IO]).program

  Opts.argument[Path](metavar = "file") map program map {
    _.unsafeRunSync
  }
}
)
