package gvolpe2.algebra

import gvolpe2.model._

@annotation.nowarn("cat=unused-params")
abstract class UserAlgebra[F[_]: ErrorChannel[*[_], E], E <: Throwable] {
  def list: F[UserList]
  def find(username: String): F[Option[User]]
  def delete(username: String): F[Unit]
  def save(user: User): F[Unit]
  def updateAge(username: String, age: Int): F[Unit]
}
