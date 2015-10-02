package abt

/**
 *
 * @tparam Variable Type of Variables in the ADT (kind of like identifier)
 * @tparam Operator The underlying AST
 */
sealed trait View[Variable, Operator, A]

object View {
  /** A node that’s a variable reference */
  final case class Var[V, O, A](v: V)               extends View[V, O, A]
  /** Abstraction is a node that binds variables */
  final case class Abs[V, O, A](vs: List[V], a: A)  extends View[V, O, A]
  /** A standard term in the underlying AST */
  final case class App[V, O, A](op: O, as: List[A]) extends View[V, O, A]

  implicit def viewFunctor[V, O]: Functor[View[V, O, ?]] =
    new Functor[View[V, O, ?]] {
      def map[A, B](va: View[V, O, A])(f: A => B) = va match {
        case Var(_)      => va
        case Abs(vs, a)  => Abs(vs, f(a))
        case App(op, as) => App(op, as map f)
      }
    }
}
