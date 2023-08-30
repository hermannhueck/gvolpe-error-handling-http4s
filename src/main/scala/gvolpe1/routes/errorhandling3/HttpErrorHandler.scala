package gvolpe1.routes.errorhandling3

import cats.ApplicativeThrow
import cats.data.{Kleisli, OptionT}
import cats.syntax.all._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import gvolpe1.model.UserError._

trait HttpErrorHandler[F[_]] {
  def handle(routes: HttpRoutes[F]): HttpRoutes[F]
}
object HttpErrorHandler      {
  def apply[F[_]](implicit ev: HttpErrorHandler[F]) = ev
}

class UserHttpErrorHandler[F[_]: ApplicativeThrow[*[_]]] extends HttpErrorHandler[F] with Http4sDsl[F] {

  // 'UserRoutesAlt2' is a compromise between gvolpe's 'UserRoutesAlt' and his 'UserRoutesMTL' examples.
  // 'UserRoutesAlt2' also concentrates error handling in one handler, but doesn't use meow-mtl. Therefore this handler
  // has to pattern match over all *Throwable*s by adding a default case, not over *UserError*s.
  private val handler: Throwable => F[Response[F]] = {
    case InvalidUserAge(age)         => BadRequest(s"Invalid age $age".asJson)
    case UserAlreadyExists(username) => Conflict(username.asJson)
    case UserNotFound(username)      => NotFound(username.asJson)
    case _                           => InternalServerError("Something went wrong on the server".asJson)
  }

  object RoutesHttpErrorHandler {
    def apply[F[_]: ApplicativeThrow[*[_]]](
        routes: HttpRoutes[F]
    )(handler: Throwable => F[Response[F]]): HttpRoutes[F] =
      Kleisli { req =>
        OptionT {
          routes
            .run(req)
            .value
            .handleErrorWith(e => handler(e).map(Option(_)))
        }
      }
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)
}
