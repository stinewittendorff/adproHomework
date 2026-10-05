// Advanced Programming, A. Wąsowski, IT University of Copenhagen
// Based on Functional Programming in Scala, 2nd Edition

package adpro.lazyList

import org.scalacheck.*
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.arbitrary

//import lazyList00.* // uncomment to test the book laziness solution implementation
//import lazyList01.* // uncomment to test the broken headOption implementation
import lazyList02.* // uncomment to test another version

import LazyList.*
import java.util.random.RandomGenerator.ArbitrarilyJumpableGenerator
import java.util.random.RandomGenerator.ArbitrarilyJumpableGenerator

object LazyListSpec
  extends org.scalacheck.Properties("testing"):

  /* Generators and helper functions */

  /** Convert a strict list to a lazy-list */
  def list2lazyList[A](la: List[A]): LazyList[A] =
    LazyList(la*)

  /** Generate finite non-empty lazy lists */
  def genNonEmptyLazyList[A](using Arbitrary[A]): Gen[LazyList[A]] =
    for la <- arbitrary[List[A]].suchThat { _.nonEmpty }
    yield list2lazyList(la)

  /** Generate an infinite lazy list of A values.
    *
    * This lazy list is infinite if the implicit generator for A never fails. The
    * code is ugly-imperative, but it avoids stack overflow (as Gen.flatMap is
    * not tail recursive)
    */
  def infiniteLazyList[A: Arbitrary]: Gen[LazyList[A]] =
    def loop: LazyList[A] =
      summon[Arbitrary[A]].arbitrary.sample match
        case Some(a) => cons(a, loop)
        case None => empty
    Gen.const(loop)

  /* The test suite */

//helper functions
  def errorList: LazyList[Int] = LazyList.cons(throw RuntimeException("fail"), errorList)

  def buildFailList(n: Int, otherList: LazyList[Int]): LazyList[Int] =
    if n == 0 then otherList
    else cons(throw RuntimeException("fail"), buildFailList(n - 1, otherList))

  // Exercise 1

  property("Ex01.01: headOption returns None on an empty LazyList") =
    empty.headOption == None

  property("Ex01.02: headOption returns the head of the stream packaged in Some") =

    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll { (n: Int) => cons(n,empty).headOption == Some(n) } :| "singleton" &&
    forAll { (s: LazyList[Int]) => s.headOption != None }      :| "random"

  // Exercise 2

  property("Ex02.01: headOption doesn't touch the tail") =
    //val list = cons(5, throw RuntimeException("failed"))
    cons(5, throw RuntimeException("failed")).headOption == Some(5)
  
  property("Ex02.02: headOption doesn't touch tail for list") =
    forAll{(n: Int) => cons(n, throw RuntimeException("failed")).headOption == Some(n)}

  // Exercise 3
  property("Ex03.01: Testing that take doesnøt force head or tail") =
    forAll(Gen.choose(0,1000)){(n: Int) => cons(throw RuntimeException("failed head"), throw RuntimeException("failed tail")).take(n)
      true
    } 
  
  property("Ex03.02: Take n on errorList (inspired by ones in 04 Lazy List test file)") = 
    forAll(Gen.choose(0,1000)) {n => errorList.take(n)
      true
    }
  
  property("Ex03.03: take n and m still doesn't force head or tail") =
    forAll(Gen.choose(0,1000), Gen.choose(0,1000)) {(n, m) => errorList.take(n).take(m)
      true
    }

  // Exercise 4

  property("Ex04.01: take 5 elements of list and element 6 is exception") = 
    cons(1, cons(2, cons(3, cons(4, cons(5, errorList))))).take(5).toList == List(1, 2, 3, 4, 5)

  property("Ex04.02: take n elements from list with n elements") = 
    forAll(Gen.choose(0,1000)){n => list2lazyList(List.fill(n)(4)).append(errorList).take(n).toList == List.fill(n)(4)}

  property("Ex04.03: take(0) should return empty") = 
    errorList.take(0).toList == List()

  // Exercise 5
  property("Ex05.01: testing that take works on nonEmpty List") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,1000)) {(n) => l.take(n).take(n).toList == l.take(n).toList}}
  
  property("Ex05.02: testing that it works on infinite list") =
    given Arbitrary[LazyList[Int]] = Arbitrary(infiniteLazyList[Int])
    
    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,1000)) {n => l.take(n).take(n).toList == l.take(n).toList}}

  property("Ex05.03: Testing take with a smaller n") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,100)) {n => l.take(n).take(n).toList == l.take(n).toList}}

  property("Ex05.04: take(3) on a fixed list gives the first 3 elements") =
      list2lazyList(List(1, 2, 3, 4, 5, 6)).take(3).take(3).toList == List(1, 2, 3)
  
  property("Ex05.05: take(3) on an empty list") =
    list2lazyList(List()).take(3).take(3).toList == List()

  property("Ex05.06: take(0)") = 
    list2lazyList(List(1,2,3)).take(0).take(0).toList == List()

  property("Ex05.07: testing with Lists own take") = 
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,100)) {n => l.take(n).toList == l.toList.take(n)}}


  // Exercise 6
  property("Ex06.01: l.drop(n).drop(m) = l.drop(n+m) works for any n,m") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])
    forAll{(l: LazyList[Int]) => 
      forAll(Gen.choose(0,1000), Gen.choose(0,1000)) {(n, m) => l.drop(n).drop(m).toList == l.drop(n+m).toList}}

  property("Ex06.02: l.drop(n).drop(m) = l.drop(n+m) with infinite lists") =
    given Arbitrary[LazyList[Int]] = Arbitrary(infiniteLazyList[Int])
    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,1000), Gen.choose(0,1000)) {(n,m) => l.drop(n).drop(m).take(100).toList == l.drop(n+m).take(100).toList}}

  property("Ex06.03: scenario test for drop") =
    list2lazyList(List(1, 2, 3, 4, 5, 6, 7, 8)).drop(2).drop(3).toList == List(6, 7, 8)

  property("Ex06.04: testing with Lists own drop") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) =>
      forAll(Gen.choose(0,20), Gen.choose(0,20)) {(n,m) => l.drop(n).drop(m).toList == l.toList.drop(n+m)}}
  
  property("Ex06.05: drop on empty list") =
    list2lazyList(List()).drop(2).toList == List()

  // Exercise 7
  property("Ex07.01: drop 3 does not force any of the elements in head") =
    val list1 = cons(throw RuntimeException("fail"), cons(throw RuntimeException("fail2"), cons(throw RuntimeException("fail3"), cons (1, cons(2, cons(3, empty))))))
    list1.drop(3).toList == List(1, 2, 3)
  
  property("Ex07.02: drop with arbitrary n of errorlist") = 
    forAll(Gen.choose(0,1000)) {n => buildFailList(n, list2lazyList(List(1, 2, 3, 4, 5))).drop(n).toList == List(1, 2, 3, 4, 5)}
  
  // Exercise 8
  property("Ex08.01: a simple sceanrio test identity") = 
    list2lazyList(List(1, 2, 3, 4)).map(identity[Int]).toList == List(1, 2, 3, 4)

  property("Ex08.02: Identity on finite lists") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) => l.map(identity[Int]).toList == l.toList}

  property("Ex08.03: Identity on infinite lists (but only 100 elements to keep it from running forever)") = 
    given Arbitrary[LazyList[Int]] = Arbitrary(infiniteLazyList[Int])

    forAll{(l: LazyList[Int]) => l.map(identity[Int]).take(100).toList == l.take(100).toList}

  property("Ex08.04: map on an empty list") =
    list2lazyList(List()).map(identity[Int]).toList == List()
  
  property("Ex08.05: testing") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int], f: Int => Int) => l.map(f).toList == l.toList.map(f)}


  // Exercise 9
  property("Ex09.01: Map terminates on infinite lazy list lists") = 
    given Arbitrary[LazyList[Int]] = Arbitrary(infiniteLazyList[Int])

    forAll{(l: LazyList[Int], f: Int => Int) => l.take(1000).map(f).toList == l.map(f).take(1000).toList}


  // Exercise 10

  property("Ex10.01: Empty.append(l) should give l") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) => empty.append(l).toList == l.toList}
  
  property("Ex10.02: l.append(empty) should give l") = 
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int]) => l.append(empty).toList == l.toList}

  property("Ex10.03: ++ two list together should be same as l.append(l2)") =
    given Arbitrary[LazyList[Int]] = Arbitrary(genNonEmptyLazyList[Int])

    forAll{(l: LazyList[Int], l2: LazyList[Int]) => l.append(l2).toList == l.toList ++ l2.toList}
  
  property("Ex10.04: Appending 2 lists") =
    list2lazyList(List(1, 2, 3, 4)).append(list2lazyList(List(5, 6, 7, 8))).toList == List(1, 2, 3, 4, 5, 6, 7, 8)

  property("Ex10.05: Append on infinite lists should terminate and never force second list") = 
    given Arbitrary[LazyList[Int]] = Arbitrary(infiniteLazyList[Int])

    forAll{(l: LazyList[Int]) => l.append(errorList).take(500).toList == l.take(500).toList}

  property("Ex10.06: Append on finite list, take first list only touches first list") =
    list2lazyList(List(1, 2, 3, 4, 5)).append(errorList).take(5).toList == List(1, 2, 3, 4, 5)