package gvolpe1.routes.errorhandling2

import cats.data.{Kleisli, OptionT}
import cats.syntax.all._
import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import org.http4s.circe.CirceEntityDecoder._

import gvolpe1.model._
import gvolpe1.algebra.UserAlgebra

// use constraint 'Sync' in CE2
class UserRoutesAlt2[F[_]: Concurrent](userAlgebra: UserAlgebra[F]) extends Http4sDsl[F] {

  import gvolpe1.model.UserError._

  private val handler: Throwable => F[Response[F]] = {
    case InvalidUserAge(age)         => BadRequest(s"Invalid age $age".asJson)
    case UserAlreadyExists(username) => Conflict(username.asJson)
    case UserNotFound(username)      => NotFound(username.asJson)
    case _                           => InternalServerError("Something went wrong on the server".asJson)
  }

  val routes: HttpRoutes[F] = Kleisli { req =>
    OptionT {
      httpRoutes
        .run(req)
        .value
        .handleErrorWith(e => handler(e).map(Option(_)))
    }
  }

  private def httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    // get all users
    case GET -> Root / "users"                  =>
      userAlgebra
        .list
        .flatMap { users =>
          Ok(users.asJson)
        }

    // get user by username
    case GET -> Root / "users" / username       =>
      userAlgebra
        .find(username)
        .flatMap {
          case Some(user) => Ok(user.asJson)
          case None       => NotFound(s"User '$username' not found".asJson)
        }

    // delete user by username
    case DELETE -> Root / "users" / username    =>
      userAlgebra
        .delete(username) *>
        Ok(s"User '$username' deleted".asJson)

    // create new user
    case req @ POST -> Root / "users"           =>
      req
        .as[User]
        .flatMap { user =>
          userAlgebra.save(user) *>
            Created(s"User '${user.username}' created".asJson)
        }

    // update user age by username
    case req @ PUT -> Root / "users" / username =>
      req
        .as[UserUpdateAge]
        .flatMap { userUpdate =>
          userAlgebra.updateAge(username, userUpdate.age) *>
            Ok(s"User '$username' updated age to ${userUpdate.age}".asJson)
        }
  }
}
