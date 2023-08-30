package gvolpe2.server

import cats.effect.IO
import cats.effect.IOApp
import gvolpe2.algebra.UserInterpreter

object UserServer extends IOApp.Simple {

  import com.comcast.ip4s._
  import org.http4s.ember.server._

  def run: IO[Unit] = {

    UserInterpreter
      .create[IO]
      .flatMap { userAlgebra =>
        {
          // 1.
          // import gvolpe.routes.nohandling._
          // val userRoutes = new UserRoutes[IO](userAlgebra)

          // 2.
          // import gvolpe.routes.errorhandling._
          // val userRoutes = new UserRoutesAlt[IO](userAlgebra)

          // 3.
          // import gvolpe2.routes.errorhandling2._
          // implicit val userHttpErrorHandler: HttpErrorHandler[IO] = new UserHttpErrorHandler[IO]
          // val userRoutes                                          = new UserRoutesAlt2[IO](userAlgebra)

          // 4.
          import com.olegpy.meow.hierarchy._
          import gvolpe2.model.UserError
          import gvolpe2.routes.mtlhandling._

          // meow-mtl derives a MonadError[F, UserError] instance from a MonadError[F, Throwable] instance for us.
          // This is possible because UserError is a subtype of Throwable.
          // The instance is passed implicitly to the UserHttpErrorHandler constructor.

          implicit val errorHandler: HttpErrorHandler[IO, UserError] = new UserHttpErrorHandler[IO]
          val userRoutes                                             = new PreUserRoutesMTL[IO](userAlgebra)
          //

          val httpApp = userRoutes.routes.orNotFound

          val server =
            EmberServerBuilder
              .default[IO]
              .withHost(ipv4"0.0.0.0")
              .withPort(port"8080")
              .withHttpApp(httpApp)
              .build

          server.use(_ => IO.never).void
        }
      }
  }
}
