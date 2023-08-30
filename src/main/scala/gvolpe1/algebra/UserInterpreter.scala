package gvolpe1.algebra

import cats.effect.kernel.Ref
import cats.effect.Sync
import cats.syntax.all._

import gvolpe1.model._
import gvolpe1.model.UserError._
import gvolpe1.algebra.UserAlgebra

object UserInterpreter {

  def create[F[_]](implicit F: Sync[F]): F[UserAlgebra[F]] =
    Ref.of[F, Map[String, User]](Map.empty).map { state =>
      new UserAlgebra[F] {

        private def validateAge(age: Int): F[Unit] =
          if (age <= 0) F.raiseError(InvalidUserAge(age)) else F.unit

        override def list: F[UserList] =
          state.get.map(m => UserList(m.values.toList))

        override def find(username: String): F[Option[User]] =
          state.get.map(_.get(username))

        override def delete(username: String): F[Unit] =
          find(username).flatMap {
            case Some(_) =>
              state.update(_ - username)
            case None    =>
              F.raiseError(UserNotFound(username))
          }

        override def save(user: User): F[Unit] =
          validateAge(user.age) *>
            find(user.username).flatMap {
              case Some(_) =>
                F.raiseError(UserAlreadyExists(user.username))
              case None    =>
                state.update(_.updated(user.username, user))
            }

        override def updateAge(username: String, age: Int): F[Unit] =
          validateAge(age) *>
            find(username).flatMap {
              case Some(user) =>
                state.update(_.updated(username, user.copy(age = age)))
              case None       =>
                F.raiseError(UserNotFound(username))
            }
      }
    }
}
