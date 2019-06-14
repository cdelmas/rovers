package rovers.io
import java.nio.file.Path

import cats.effect._

class FileInterpreter[F[_]: Effect] extends FileAlgebra[F]{

  override def read(f: Path): F[String] = {
    val F = Effect[F]
    F.bracket(F.delay(scala.io.Source.fromFile(f.toFile)))(src => F.delay(src.mkString))(src => F.delay(src.close))
  }
}
