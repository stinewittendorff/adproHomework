// Advanced Programming, A. Wąsowski, IT University of Copenhagen Based on Functional Programming in Scala, 2nd Edition

package adpro.intro

import scala.annotation.tailrec

object MyModule:

  def abs(n: Int): Int =
    if n < 0 then -n else n

  // Exercise 1

  def square(n: Int): Int =
    n * n

  private def formatAbs(x: Int): String =
    s"The absolute value of ${x} is ${abs(x)}"

  val magic: Int = 42
  var result: Option[Int] = None

  @main def printAbs: Unit =
    assert(magic - 84 == magic.-(84))
    println(formatAbs(magic - 100))
    println(square(magic))

end MyModule

// Exercise 2 requires no programming

// Exercise 3

def fib(n: Int): Int =
    @annotation.tailrec 
    def loop (n: Int, a: Int, b: Int): Int =
      if n <= 0 then a
      else loop(n - 1, b, a + b)
    loop(n - 1, 0, 1)

// Exercise 4

def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean =
  @annotation.tailrec
  def loop(i: Int) : Boolean =
    if i + 1 >= as.length then true
    else if !ordered(as(i), as(i + 1)) then false
    else loop(i + 1)
  loop(0)

// Exercise 5

def curry[A, B, C](f: (A, B) => C): A => (B => C) =
  a => b => f(a,b)

def isSortedCurried[A]: Array[A] => ((A, A) => Boolean) => Boolean =
  curry(isSorted)

// Exercise 6

def uncurry[A, B, C](f: A => B => C): (A, B) => C =
  (a,b) => f (a) (b)

def isSortedCurriedUncurried[A]: (Array[A], (A, A) => Boolean) => Boolean =
  uncurry(isSortedCurried)

// Exercise 7

def compose[A, B, C](f: B => C, g: A => B): A => C =
  x => f(g(x))
