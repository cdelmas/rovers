package rovers.io

import java.nio.file.Path

trait FileAlgebra[F[_]] {

  def read(f: Path): F[String]
}

object FileAlgebra {
  def apply[F[_]](implicit F: FileAlgebra[F]): FileAlgebra[F]= F
}
