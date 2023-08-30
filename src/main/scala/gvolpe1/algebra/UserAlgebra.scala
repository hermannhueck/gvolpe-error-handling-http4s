package gvolpe1.algebra

import gvolpe1.model._

trait UserAlgebra[F[_]] {
  def list: F[UserList]
  def find(username: String): F[Option[User]]
  def delete(username: String): F[Unit]
  def save(user: User): F[Unit]
  def updateAge(username: String, age: Int): F[Unit]
}
