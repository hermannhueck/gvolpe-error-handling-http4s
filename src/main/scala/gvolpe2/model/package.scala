package gvolpe2

package object model {

  case class User(username: String, age: Int)
  case class UserList(users: List[User])
  case class UserUpdateAge(age: Int)

  sealed trait UserError extends Exception
  object UserError {
    case class UserAlreadyExists(username: String) extends UserError
    case class UserNotFound(username: String)      extends UserError
    case class InvalidUserAge(age: Int)            extends UserError
  }
}
