package rovers.io

trait Terminal[F[_]] {

  def putStrLn(s: String): F[Unit]
}

object Terminal {
  def apply[F[_]](implicit F: Terminal[F]): Terminal[F] = F
}

