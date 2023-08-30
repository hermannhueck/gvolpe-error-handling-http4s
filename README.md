# Tinkering with the source of Gabriel Volpe's blog post on error handling with *http4s*

## My changes to gvolpe's example:

- port to Scala 2.13.x
- port to cats-effect 3.x
- use *cats.effect.Concurrent* from CE3 instead of *cats.effect.Sync* from CE2
- using current versions of http4s, circe, and other dependencies (as of 2023-08-25)
- use the Ember server instead of the Blaze server
- extend the algebra to include 'list' and 'delete' operations

```scala
trait UserAlgebra[F[_]] {
  def list: F[UserList]
  def find(username: String): F[Option[User]]
  def delete(username: String): F[Unit]
  def save(user: User): F[Unit]
  def updateAge(username: String, age: Int): F[Unit]
}
```

- add new example 'UserRoutesAlt2' and 'UserRoutesAlt3' which are compromise solutions between gvolpe's
 'UserRoutesAlt' and 'UserRoutesMTL' examples. Both also concentrates error handling in one handler, but doesn't use meow-mtl.
  Therefore the handlers of 'UserRoutesAlt2' and 'UserRoutesAlt3' have to pattern match over all *Throwable*s by adding a default case,
  not over the *UserError*s ADT.

## Resources:

- [Blog Post #1: Error handling in Http4s with classy optics](https://typelevel.org/blog/2018/08/25/http4s-error-handling-mtl.html)
- [Gist for Blog Post #1](https://gist.github.com/gvolpe/3fa32dd1b6abce2a5466efbf0eca9e94)
- [Talk at ScalaMatsuri in 2018](https://www.youtube.com/watch?v=pGfj_l-h3M8&t=887s)

<br/>

- [Blog Post #2: Error handling in Http4s with classy optics – Part 2](https://typelevel.org/blog/2018/11/28/http4s-error-handling-mtl-2.html)
- [Github Repo for Blog Post #2](https://github.com/gvolpe/classy-optics)
- [Talk at Scale By the Bay in 2018](https://www.youtube.com/watch?v=UUX5KvPgejM)
- [Talk at Scalar Conference in 2019](https://www.youtube.com/watch?v=gYnbOUGpWK0)
