error id: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/04-lazy-list/Exercises.scala:
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/04-lazy-list/Exercises.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -LazyList.B#
	 -B#
	 -scala/Predef.B#
offset: 7698
uri: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/04-lazy-list/Exercises.scala
text:
```scala
// Advanced Programming, A. Wąsowski, IT University of Copenhagen
// Based on Functional Programming in Scala, 2nd Edition

package adpro.lazyList

// Note: we are using our own lazy lists, not the standard library

enum LazyList[+A]:
  case Empty
  case Cons(h: () => A, t: () => LazyList[A])

  import LazyList.*

  def headOption: Option[A] = this match
    case Empty => None
    case Cons(h,t) => Some(h())

  def tail: LazyList[A] = this match
    case Empty => Empty
    case Cons(h,t) => t()

  /* Note 1. f can return without forcing the tail
   *
   * Note 2. this is not tail recursive (stack-safe) It uses a lot of stack if
   * f requires to go deeply into the lazy list. So folds sometimes may be less
   * useful than in the strict case
   *
   * Note 3. We added the type C to the signature. This allows to start with a
   * seed that is a subtype of what the folded operator returns.
   * This helps the type checker to infer types when the seed is a subtype, for 
   * instance, when we construct a list:
   *
   * o.foldRight (Nil) ((a,z) => a:: z)
   *
   * The above works with this generalized trick. Without the C generalization
   * the compiler infers B to be List[Nothing] (the type of Nil) and reports
   * a conflict with the operator.  Then we need to help it like that:
   *
   * o.foldRight[List[Int]] (Nil) ((a,z) => a:: z)
   *
   * With the C type trick, this is not neccessary. As it hints the type
   * checker to search for generalizations of B.
   *
   * I kept the foldLeft type below in a classic design, so that you can
   * appreciate the difference. Of course, the same trick could've been
   * applied to foldLeft.
   */
  def foldRight[B, C >: B](z: => B)(f: (A, => C) => C): C = this match
    case Empty => z
    case Cons(h, t) => f(h(), t().foldRight(z)(f))

  /* Note 1. Eager; cannot be used to work with infinite lazy lists. So
   * foldRight is more useful with lazy lists (somewhat opposite to strict lists)
   * Note 2. Even if f does not force z, foldLeft will continue to recurse.
   */
  def foldLeft[B](z: => B)(f :(A, => B) => B): B = this match
    case Empty => z
    case Cons(h, t) => t().foldLeft(f(h(), z))(f)

  // Note: Do you know why we can implement find with filter for lazy lists but
  // would not do that for regular lists?
  def find(p: A => Boolean) = 
    this.filter(p).headOption

  // Exercise 2

  def toList: List[A] = this match
    case Empty => Nil
    case Cons(h,t) => h() :: t().toList
  

  // Test in the REPL, for instance: LazyList(1,2,3).toList 
  // (and see what list is constructed)

  // Exercise 3

  def take(n: Int): LazyList[A] = this match
    case Cons(h,t) if n > 0 => cons(h(), t().take(n-1))
    case _ => empty
  

  def drop(n: Int): LazyList[A] = this match
    case Cons(h,t) if n > 0 => t().drop(n-1)
    case Cons(h,t) if n <= 0 => this
    case Empty => empty
  

  /*
    scala> naturals.take(1000000000).drop(41).take(10).toList
    val res1: List[Int] = List(42, 43, 44, 45, 46, 47, 48, 49, 50, 51)

    The reason that the code handles the exampe above in the repl is because lazy lists postpones the calculation until it is actually 
    needed. .take(1000000000) is actually not an action that 'creates' 1000000000 numbers but it only a description on how you would be 
    able to get them. The reason taht is doesnt actually build anything is because the 'take' function is building the result with
    'cons(h(), t().take(n-1)) and in the definition of cons, the second argument is (=> LazyList[A]). Which means that the call actually 
    only creates one Cons at a time with a head and then how to do the rest. The recursive part is not actually calculated before someone
    specifically asks for it 
    It is only the 51 elements that the .drop(41) and the last .take(10) that actually gets calculated as seen above. Where the 41 of them
    are the ones who needs to be 'dropped' and the last 10 is the ones we need to keep and return. And it is the .toList in the end of the
    code that isn't actually lazy and is the one actively asking for elements.

    So the reason that is terminates without any exception is because it doesn't actualy create/computes 1000000000 numbers.
  */

  // Exercise 4

  def takeWhile(p: A => Boolean): LazyList[A] = this match
    case Cons(h,t) if p(h()) => cons(h(), t().takeWhile(p))
    case Cons(h,t) if !(p(h())) => empty
    case _ => empty

  /*
    naturals.takeWhile { _< 1000000000 }.drop(100).take(50).toList
    The reason that this code terminates fast is because even the boolean predicate could allow up to 1000000000 elements ot pass through 
    takeWhile, it doesnt actually generate or test the code that many times. And that is becuase takeWhile builds up the result lazily 
    one Cons at a time with cons as described above, and it is only .toList that actually forces the code to do the calculations and then 
    stops as soon as .drop(100).take(50). has what it needs and that is after only 150 elements. So the 1000000000 is basically irrelevant 
    for this, as we never come near the boolean boundary, so what actually matters is the .drop(100).take(50) that decide how many 
    elements are needed/run through.
  */

  // Exercise 5
  
  def forAll(p: A => Boolean): Boolean = this match
    case Empty => true
    case Cons(h,t) if p(h()) => t().forAll(p)
    case Cons(h,t) if !p(h()) => false
  
 
  // Note 1. lazy; tail is never forced if satisfying element found this is
  // because || is non-strict
  // Note 2. this is also tail recursive (because of the special semantics
  // of ||)
  def exists(p: A => Boolean): Boolean = this match
    case Empty => false
    case Cons(h,t) => p(h()) || t().exists(p)
  

  /*
    The reason that naturals.forAll { _< 0 } is working is because natural numbers start from 1, so the predicate is immediately false  as
    1 < 0 does not hold, and therefore hits this 'case Cons(h,t) if !p(h()) => false' and returns false. Whereas naturals.forAll { _>=0 }
    would run infinetly as all natural numbers are >=0 so the 'case Cons(h,t) if p(h()) => t().forAll(p)' would hit all elements and therefore
    the code would never stop running because the naturals number does not have an 'end', so there would never be an element that would make
    the predicate false. This would eventually end in a stack overflow and make the program crash.

    And the reason that forAll and exists are fine to use on finite lazy lists is because on a finite list you are guaranteed at some point
    to hit the empty at some point no matter what the predicate is. So even if all, none or some of the elements fulfills/hits the predicate
    p, the recursion will always have a place it will end. Either because it finds the predicate that makes it stops underway due to hitting
    one of the false predicates or because it hits the empty base case. There is always a guaranteed way to a stop. 
    If it is an infinite list then it will never hit the empty base case, so the only way for forAll or exists to stop doing the execution
    if it finds an element in the list somewhere that hits the criteria to make it stop, but there is no guarantee that such element exists.
    As in the example with naturals.forAll { _>=0 } there is no element that would ever make the recursion stop, it would run forever. 
  */

  // Exercise 6
  
  def takeWhile1(p: A => Boolean): LazyList[A] =
    foldRight(empty)((h,t) => if p(h) then cons(h,t) else empty)

  // Exercise 7
  
  def headOption1: Option[A] = 
    foldRight(None)((h,t) => Some(h))

  // Exercise 8
  
  // Note: The type is incorrect, you need to fix it
  def map(f: A => B): LazyList[B] = 
    foldRight(empty[@@B])((a, acc) => cons(f(a), acc))

  // Note: The type is incorrect, you need to fix it
  def filter(p: Any): LazyList[Any] = 
    ???

  /* Note: The type is given correctly for append, because it is more complex.
   * Try to understand the type. The contsraint 'B >: A' requires that B is a
   * supertype of A. The signature of append allows to concatenate a list of
   * supertype elements, and creates a list of supertype elements.  We could have
   * writte just the following:
   *
   * def append(that: => LazyList[A]): LazyList[A]
   *
   * but this would not allow adding a list of doubles to a list of integers
   * (creating a list of numbers).  Compare this with the definition of
   * getOrElse last week, and the type of foldRight this week.
   */
  def append[B >: A](that: => LazyList[B]): LazyList[B] = 
    ???

  // Note: The type is incorrect, you need to fix it
  def flatMap(f: Any): LazyList[Any] = 
    ???

  // Exercise 9
  // Type answer here
  //
  // ...
  //
  // Scroll down to Exercise 10 in the companion object below

  // Exercise 13

  def mapUnfold[B](f: A => B): LazyList[B] =
    ???

  def takeUnfold(n: Int): LazyList[A] =
    ???

  def takeWhileUnfold(p: A => Boolean): LazyList[A] =
    ???

  def zipWith[B >: A, C](ope: (=> B, => B) => C)(bs: LazyList[B]): LazyList[C] =
    ???

end LazyList // enum ADT



// The companion object for lazy lists ('static methods')

object LazyList:

  def empty[A]: LazyList[A] = Empty

  def cons[A](hd: => A, tl: => LazyList[A]): LazyList[A] =
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)

  def apply[A](as: A*): LazyList[A] =
    if as.isEmpty 
    then empty
    else cons(as.head, apply(as.tail*))

  // Exercise 1

  def from(n: Int): LazyList[Int] =
    cons(n, from(n + 1))

  def to(n: Int): LazyList[Int] =
    cons(n, to(n - 1))

  lazy val naturals: LazyList[Int] =
    from(1)

  // Scroll up to Exercise 2 to the enum LazyList definition 
  
  // Exercise 10

  // Note: The type is incorrect, you need to fix it
  lazy val fibs: Any = 
    ???

  // Exercise 11

  def unfold[A,S](z: S)(f: S => Option[(A, S)]): LazyList[A] =
    ???

  // Exercise 12

  // Note: The type is incorrect, you need to fix it
  lazy val fibsUnfold: Any = ???

  // Scroll up for Exercise 13 to the enum

end LazyList // companion object

```


#### Short summary: 

empty definition using pc, found symbol in pc: 