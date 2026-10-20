error id: CAA5B7A6837BB0496E0D3ED3BBB2B346
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/07-prop/Exercises.scala
### java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing()" because the return value of "dotty.tools.dotc.parsing.Scanners$Indented.outer()" is null

occurred in the presentation compiler.



action parameters:
offset: 1880
uri: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/07-prop/Exercises.scala
text:
```scala
// Advanced Programming, A. Wąsowski, IT University of Copenhagen
// Based on Functional Programming in Scala, 2nd Edition

package adpro.prop

import adpro.state.*
import scala.annotation.static

val TODO = 42

// Exercise 1

lazy val rng1: RNG = RNG.Simple(TODO)

// Exercise 2

lazy val (x, rng2): (Int, RNG) = rng1.nextInt
lazy val y = rng2.nextInt._1

// Exercise 3

object Exercise_3:

  import org.scalacheck.Prop.{forAll, forAllNoShrink}

  // Use this generator explicitly in the two properties
  val intList =
    org.scalacheck.Gen.listOf(org.scalacheck.Gen.choose(0, 100))
      .suchThat (_.nonEmpty)

  // The properties are put into a function taking `minimum` as argument to
  // allow the teachers to test them with different mutants of `minimum`.

  def p1Min (minimum: List[Int] => Int): org.scalacheck.Prop =
    org.scalacheck.Prop.forAll(intList){l => l.forall(a => a >= minimum(l))}

  def p2Min (minimum: List[Int] => Int): org.scalacheck.Prop =
    org.scalacheck.Prop.forAll(intList){l => l.exists(a => a == minimum(l))}

end Exercise_3

// Exercise 4

object Exercise_4:

  // This implementation of Prop is only used in Exercise 4
  trait Prop:
    self => // TODO remove
    def check: Boolean

    infix def && (that: Prop): Prop =
      new Prop: 
        def check: Boolean = 
          self.check && that.check
      

end Exercise_4

opaque type Gen[+A] = State[RNG, A]

object Gen:

  extension [A](g: Gen[A])
    // Let's convert generator to streams of generators
    def toLazyList (rng: RNG): LazyList[A] =
      LazyList.unfold[A,RNG](rng)(rng => Some(g.run(rng)))

  // Exercise 5
  def choose (start: Int, stopExclusive: Int): Gen[Int] =
    State.apply(RNG.nonNegativeInt).map(n => (n % (stopExclusive - start)) + start)

  // Exercise 6

  def unit[A] (a: =>A): Gen[A] =
    State.unit(a)

  def boolean: Gen[Boolean] =
    choose(0,1),@@

  def double: Gen[Double] =
    ???

  // Exercise 7

  extension [A](self: Gen[A])

    def listOfN(n: Int): Gen[List[A]] =
      ???

  // Exercise 8

  // Write here ... ???

  // Exercise 9

  extension [A](self: Gen[A])

    def flatMap[B](f: A => Gen[B]): Gen[B] =
      State.flatMap(self)(f)

    // It will be convenient to also have map (uses flatMap)
    def map[B](f: A => B): Gen[B] =
      self.flatMap { a => unit(f(a)) }


  // Exercise 10

  extension [A](self: Gen[A])
    def listOf(size: Gen[Int]): Gen[List[A]] =
      ???


  // Exercise 11

  extension [A](self: Gen[A])
    def union (that: Gen[A]): Gen[A] =
      ???

end Gen

import Gen.*

object Exercise_12:

  // Exercise 12 (type classes, givens, using, summon)

  // Choose one of the following templates, but note only listOfN and
  // listOf are tested (so rename if you use another than the first)

  def listOfN[A: Gen](n: Int): Gen[List[A]] =
    ???

  def listOfN_[A: Gen]: Int => Gen[List[A]] =
    ???

  def listOfN__ [A](n: Int)(using genA: Gen[A]): Gen[List[A]] =
    ???

  // Choose one of the following templates, but note only listOfN and
  // listOf are tested (so rename if you use another than the first)

  def listOf[A: Gen](using genInt: Gen[Int]): Gen[List[A]] =
    ???

  def listOf_[A] (using genInt: Gen[Int], genA: Gen[A]): Gen[List[A]] =
    ???

end Exercise_12


// (Exercise 13)
// Read the code below (and/or Section 8.1.6). You will find the
// exercise in the bottom of this fragment, marked with ??? as usual.

opaque type TestCases = Int
opaque type FailedCase = String
opaque type SuccessCount = Int
opaque type MaxSize = Int

def TestCases(n: Int): TestCases = n
def FailedCase(s: String): FailedCase = s
def SuccessCount(n: Int): SuccessCount = n
def MaxSize(n: Int): MaxSize = n

/** The type of results returned by property testing. */
enum Result:
  case Passed
  case Falsified(failure: FailedCase, successes: SuccessCount)

  def isFalsified: Boolean = this match
    case Passed => false
    case Falsified(_, _) => true

end Result

import Result.{Passed, Falsified}

opaque type Prop = (MaxSize, TestCases, RNG) => Result

def randomLazyList[A](g: Gen[A])(rng: RNG): LazyList[A] =
  LazyList.unfold(rng)(rng => Some(g.run(rng)))

def buildMsg[A](s: A, e: Exception): String =
  s"test case: $s\n" +
  s"generated an exception: ${e.getMessage}\n" +
  s"stack trace:\n ${e.getStackTrace.mkString("\n")}"

def forAll[A](as: Gen[A])(f: A => Boolean): Prop =
  (max, n, rng) =>
    randomLazyList(as)(rng)
      .zip(LazyList.from(0))
      .take(n)
      .map { (a, i) =>
             try if f(a) then Passed else Falsified(a.toString, i)
             catch case e: Exception => Falsified(buildMsg(a, e), i) }
      .find(_.isFalsified)
      .getOrElse(Passed)

def forAllNotSized[A] = forAll[A]

extension (self: Prop)
  infix def && (that: Prop): Prop = (maxSize, tcs, rng) =>
    ???

  infix def || (that: Prop): Prop = (maxSize, tcs, rng) =>
    ???

  def apply(maxSize: MaxSize, tcs: TestCases, rng: RNG): Result =
    self(maxSize, tcs, rng)

// Exercise 14

/** The type of generators bounded by size */
opaque type SGen[+A] = Int => Gen[A]

extension [A](self: SGen[A])
  def apply(n: Int): Gen[A] = self(n)

extension [A](self: Gen[A])
  def unsized: SGen[A] =
    ???

// Exercise 15

extension [A](self: Gen[A])
  def list: SGen[List[A]] =
    ???

// A sized implementation of prop, takes MaxSize to generate
// test cases of given size.
//
// The code below also contains the `run` method for Prop - which
// provides a simple way to execute tests. Needed in the final
// exercise below.

object SGen:

  object Prop:

    def forAll[A](g: SGen[A])(f: A => Boolean): Prop =
      (max, n, rng) =>
        val casesPerSize = (n.toInt - 1)/max.toInt + 1
        val props: LazyList[Prop] =
          LazyList.from(0)
            .take((n.toInt min max.toInt) + 1)
            .map { i => forAllNotSized(g(i))(f) }
        val prop: Prop =
          props.map[Prop](p => (max, n, rng) =>
            p(max, casesPerSize, rng)).toList.reduce(_ && _)
        prop(max, n, rng)

  extension (self: Prop)
    def run(
      maxSize: MaxSize = 100,
      testCases: TestCases = 100,
      rng: RNG = RNG.Simple(System.currentTimeMillis)): Boolean =

      self(maxSize, testCases, rng) match
      case Result.Falsified(msg, n) =>
        println(s"\u001b[34m! Falsified after $n passed tests:\n $msg [message from our Prop framework]")
        false
      case Result.Passed =>
        println(s"\u001b[34m+ OK, passed $testCases tests. [message from our Prop framework]")
        true

end SGen

// Exercise 16

// Use this generator explicitly in the two properties
val nonEmptyList: SGen[List[Int]] =
  (n: Int) => Gen.choose(-100, 100).listOfN(n max 1)

object Exercise_16:

  import SGen.*

  // The properties are put into a function taking `minimum` as argument to
  // allow the teachers to test them with different mutants of `minimum`.

  def p1Min(minimum: List[Int] => Int): Prop =
    ???

  def p2Min(minimum: List[Int] => Int): Prop =
    ???

end Exercise_16

// vim:cc=80:conceallevel=1

```


presentation compiler configuration:
Scala version: 3.8.4-bin-nonbootstrapped
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.4/scala3-library_3-3.8.4.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.4/scala-library-3.8.4.jar [exists ]
Options:





#### Error stacktrace:

```
dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing(Scanners.scala:1666)
	dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing(Scanners.scala:1666)
	dotty.tools.dotc.parsing.Scanners$Scanner.observeOutdented(Scanners.scala:727)
	dotty.tools.dotc.parsing.Parsers$Parser.statSepOrEnd(Parsers.scala:393)
	dotty.tools.dotc.parsing.Parsers$Parser.blockStatSeq$$anonfun$1(Parsers.scala:5043)
	dotty.tools.dotc.parsing.Parsers$Parser.checkNoEscapingPlaceholders(Parsers.scala:559)
	dotty.tools.dotc.parsing.Parsers$Parser.blockStatSeq(Parsers.scala:5046)
	dotty.tools.dotc.parsing.Parsers$Parser.block(Parsers.scala:3065)
	dotty.tools.dotc.parsing.Parsers$Parser.blockExpr$$anonfun$1(Parsers.scala:3057)
	dotty.tools.dotc.parsing.Parsers$Parser.enclosed(Parsers.scala:623)
	dotty.tools.dotc.parsing.Parsers$Parser.inBracesOrIndented(Parsers.scala:653)
	dotty.tools.dotc.parsing.Parsers$Parser.inDefScopeBraces(Parsers.scala:659)
	dotty.tools.dotc.parsing.Parsers$Parser.blockExpr(Parsers.scala:3055)
	dotty.tools.dotc.parsing.Parsers$Parser.simpleExpr(Parsers.scala:2874)
	dotty.tools.dotc.parsing.Parsers$Parser.$init$$$anonfun$10(Parsers.scala:2825)
	dotty.tools.dotc.parsing.Parsers$Parser.postfixExpr(Parsers.scala:2801)
	dotty.tools.dotc.parsing.Parsers$Parser.expr1(Parsers.scala:2602)
	dotty.tools.dotc.parsing.Parsers$Parser.expr(Parsers.scala:2489)
	dotty.tools.dotc.parsing.Parsers$Parser.$init$$$anonfun$9(Parsers.scala:2460)
	dotty.tools.dotc.parsing.Parsers$Parser.subPart(Parsers.scala:723)
	dotty.tools.dotc.parsing.Parsers$Parser.subExpr(Parsers.scala:2462)
	dotty.tools.dotc.parsing.Parsers$Parser.defDefOrDcl(Parsers.scala:4208)
	dotty.tools.dotc.parsing.Parsers$Parser.defOrDcl(Parsers.scala:4089)
	dotty.tools.dotc.parsing.Parsers$Parser.templateStatSeq$$anonfun$1(Parsers.scala:4946)
	dotty.tools.dotc.parsing.Parsers$Parser.checkNoEscapingPlaceholders(Parsers.scala:559)
	dotty.tools.dotc.parsing.Parsers$Parser.templateStatSeq(Parsers.scala:4954)
	dotty.tools.dotc.parsing.Parsers$Parser.$anonfun$48(Parsers.scala:4822)
	dotty.tools.dotc.parsing.Parsers$Parser.enclosed(Parsers.scala:623)
	dotty.tools.dotc.parsing.Parsers$Parser.inBracesOrIndented(Parsers.scala:653)
	dotty.tools.dotc.parsing.Parsers$Parser.inDefScopeBraces(Parsers.scala:659)
	dotty.tools.dotc.parsing.Parsers$Parser.templateBody(Parsers.scala:4822)
	dotty.tools.dotc.parsing.Parsers$Parser.templateBodyOpt(Parsers.scala:4815)
	dotty.tools.dotc.parsing.Parsers$Parser.template(Parsers.scala:4792)
	dotty.tools.dotc.parsing.Parsers$Parser.templateOpt(Parsers.scala:4804)
	dotty.tools.dotc.parsing.Parsers$Parser.objectDef(Parsers.scala:4379)
	dotty.tools.dotc.parsing.Parsers$Parser.tmplDef(Parsers.scala:4335)
	dotty.tools.dotc.parsing.Parsers$Parser.defOrDcl(Parsers.scala:4095)
	dotty.tools.dotc.parsing.Parsers$Parser.topStatSeq(Parsers.scala:4886)
	dotty.tools.dotc.parsing.Parsers$Parser.topstats$1(Parsers.scala:5082)
	dotty.tools.dotc.parsing.Parsers$Parser.topstats$1(Parsers.scala:5076)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit$$anonfun$1(Parsers.scala:5087)
	dotty.tools.dotc.parsing.Parsers$Parser.checkNoEscapingPlaceholders(Parsers.scala:559)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit(Parsers.scala:5092)
	dotty.tools.dotc.parsing.Parsers$Parser.parse(Parsers.scala:207)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$1(ParserPhase.scala:32)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:539)
	dotty.tools.dotc.parsing.Parser.parse(ParserPhase.scala:40)
	dotty.tools.dotc.parsing.Parser.$anonfun$2(ParserPhase.scala:52)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:495)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:597)
	scala.collection.immutable.List.prependedAll(List.scala:156)
	scala.collection.immutable.List$.from(List.scala:681)
	scala.collection.immutable.List$.from(List.scala:681)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:906)
	dotty.tools.dotc.parsing.Parser.runOn(ParserPhase.scala:51)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:380)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1324)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:373)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$2(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$adapted$1(Run.scala:420)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.Run.showProgress(Run.scala:482)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:432)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:432)
	dotty.tools.dotc.Run.compileSources(Run.scala:319)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:180)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:56)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:32)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:523)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing()" because the return value of "dotty.tools.dotc.parsing.Scanners$Indented.outer()" is null