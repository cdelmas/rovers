package rovers.io

import cats.effect._

class ConsoleTerminal[F[_] : Sync] extends Terminal[F] {
  override def putStrLn(s: String): F[Unit] = Sync[F].delay(println(s))
}
