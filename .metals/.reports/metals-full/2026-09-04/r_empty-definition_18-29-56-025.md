error id: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/02-adt/Exercises.scala:
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/02-adt/Exercises.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -foldLeft.
	 -foldLeft#
	 -foldLeft().
	 -scala/Predef.foldLeft.
	 -scala/Predef.foldLeft#
	 -scala/Predef.foldLeft().
offset: 2595
uri: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/02-adt/Exercises.scala
text:
```scala
// Advanced Programming, A. Wąsowski, IT University of Copenhagen
// Based on Functional Programming in Scala, 2nd Edition

package adpro.adt

import java.util.NoSuchElementException

enum List[+A]:
  case Nil
  case Cons(head: A, tail: List[A])


object List: 

  def head[A] (l: List[A]): A = l match
    case Nil => throw NoSuchElementException() 
    case Cons(h, _) => h                                                                                                                                                                                                                                       
  
  def apply[A] (as: A*): List[A] =
    if as.isEmpty then Nil
    else Cons(as.head, apply(as.tail*))

  def append[A] (l1: List[A], l2: List[A]): List[A] =
    l1 match
      case Nil => l2
      case Cons(h, t) => Cons(h, append(t, l2)) 

  def foldRight[A, B] (l: List[A], z: B, f: (A, B) => B): B = l match
    case Nil => z
    case Cons(a, as) => f(a, foldRight(as, z, f))
    
  def map[A, B] (l: List[A], f: A => B): List[B] =
    foldRight[A, List[B]] (l, Nil, (a, z) => Cons(f(a), z))

  // Exercise 1 (is to be solved without programming)

  // The value of the following match expression would be 3, because it would match the case '
  // case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y

  // Exercise 2

  def tail[A] (l: List[A]): List[A] = l match
    case Nil => throw NoSuchElementException()
    case Cons(_, t) => t

  // Exercise 3
  
  def drop[A] (l: List[A], n: Int): List[A] = n match
    case n if n <= 0 => l
    case n => drop (tail(l), (n-1))

  // Exercise 4

  def dropWhile[A] (l: List[A], p: A => Boolean): List[A] = l match
    case Nil => l
    case Cons(h, t) => if p(h) then dropWhile(t, p) else l

  // Exercise 5
 
  def init[A] (l: List[A]): List[A] = l match
    case Nil => throw NoSuchElementException()
    case Cons(_,Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))

    //This function is linear in both time and space because to drop the last element of the list, you have to walk all the way to the end
    //to find where the list terminates.
    // So time O(n) because the list has to go through the entire list to hit the Cons(_,Nil). And then space O(n) because it allocates a 
    // brand new Cons(h,..) cell for every element except the last, so the output itself is O(n) new memory.
  

  // Exercise 6

  def length[A] (l: List[A]): Int = 
    List.foldRight(l,0,(_, acc) => 1 + acc)

  // Exercise 7

  def foldLeft[A, B] (l: List[A], z: B, f: (B, A) => B): B = l match
    case Nil => z
    case Cons(h,t) => f(h,z) t.@@foldLeft
  
    

  // Exercise 8

  def product (as: List[Int]): Int = ???

  def length1[A] (as: List[A]): Int = ???

  // Exercise 9

  def reverse[A] (l: List[A]): List[A] = ???
 
  // Exercise 10

  def foldRight1[A, B] (l: List[A], z: B, f: (A, B) => B): B = ???

  // Exercise 11

  def foldLeft1[A, B] (l: List[A], z: B, f: (B, A) => B): B = ???
 
  // Exercise 12

  def concat[A] (l: List[List[A]]): List[A] = ???
  
  // Exercise 13

  def filter[A] (l: List[A], p: A => Boolean): List[A] = ???
 
  // Exercise 14

  def flatMap[A,B] (l: List[A], f: A => List[B]): List[B] = ???

  // Exercise 15

  def filter1[A] (l: List[A], p: A => Boolean): List[A] = ???

  // Exercise 16

  def addPairwise (l: List[Int], r: List[Int]): List[Int] = ???

  // Exercise 17

  def zipWith[A, B, C] (l: List[A], r: List[B], f: (A,B) => C): List[C] = ???

  // Exercise 18

  def hasSubsequence[A] (sup: List[A], sub: List[A]): Boolean = ???

```


#### Short summary: 

empty definition using pc, found symbol in pc: 