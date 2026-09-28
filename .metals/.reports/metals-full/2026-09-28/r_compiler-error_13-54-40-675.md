error id: 228A24B28ED17952718C0C64AD0DA098
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/05-state/Exercises.scala
### java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing()" because the return value of "dotty.tools.dotc.parsing.Scanners$Indented.outer()" is null

occurred in the presentation compiler.



action parameters:
uri: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/05-state/Exercises.scala
text:
```scala
// Advanced Programming, A. Wąsowski, IT University of Copenhagen
// Based on Functional Programming in Scala, 2nd Edition

package adpro.state

import adpro.lazyList.LazyList
import adpro.lazyList.LazyList.*


trait RNG:
  /** Generate a random `Int`. We define other functions using `nextInt`. */
  def nextInt: (Int, RNG) 

object RNG:

  case class SimpleRNG(seed: Long) extends RNG:
    def nextInt: (Int, RNG) =
      // `&` is bitwise AND. We use the current seed to generate a new seed.
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL 
      // The next state, which is an `RNG` instance created from the new seed. 
      val nextRNG = SimpleRNG(newSeed)
      // `>>>` is right binary shift with zero fill. 
      // The value `n` is our new pseudo-random integer.
      val n = (newSeed >>> 16).toInt 
      // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
      (n, nextRNG) 


  // Exercise 1

  def nonNegativeInt(rng: RNG): (Int, RNG) =
    val (it, rnd) = rng.nextInt
    (if it < 0 then -(it + 1) else it, rnd)

  // Exercise 2

  def double(rng: RNG): (Double, RNG) = 
    val (it, rnd) = nonNegativeInt(rng)
    (it / (Int.MaxValue.toDouble + 1), rnd)

  // Exercise 3
  
  // The return type is broken and needs to be fixed
  def intDouble(rng: RNG): ((Int, Double), RNG) = 
    val (it, rnd) = nonNegativeInt(rng)
    val (dob, rnd1) = double(rnd)
    ((it, dob), rnd1) 

  // The return type is broken and needs to be fixed
  def doubleInt(rng: RNG): ((Double, Int), RNG) = 
    val (it, rnd) = nonNegativeInt(rng)
    val (dob, rnd1) = double(rnd)
    ((dob, it), rnd1)

  // Exercise 4

  // The return type is broken and needs to be fixed
  def ints(size: Int)(rng: RNG): (List[Int], RNG) = 
    val list = Nil
    if (size <= 0) then
      (list, rng)
    else
      val (it ,rnd) = int(rng)
      val (list1, rnd1) = ints(size - 1)(rnd)
      val list2 = it :: list1
      (list2, rnd1)



  type Rand[+A] = RNG => (A, RNG)

  lazy val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def nonNegativeEven: Rand[Int] = map(nonNegativeInt) { i => i - i % 2 }

  // Exercise 5

  lazy val double2: Rand[Double] = 
    map(nonNegativeInt)(it => it / (Int.MaxValue.toDouble + 1))

  // Exercise 6

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = 
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a,b), rng3)
    }

  // Exercise 7  aos.foldRight(Some(List()))((ao, acc) => map2(ao, acc)((element, list) => element :: list))


  def sequence[A](ras: List[Rand[A]]): Rand[List[A]] =
    ras.foldRight(unit(List.empty[A]))((ra, acc) => map2(ra, acc)((element, list) => element :: list))

  def ints2(size: Int): Rand[List[Int]] =
    sequence(List.fill(size)(int))

  // Exercise 8

  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] =
    rng => {
      val(a, rng2) = f(rng)
      val h = g(a)
      h(rng2)
    }

  def nonNegativeLessThan(bound: Int): Rand[Int] =
    flatMap(nonNegativeInt) : i =>
      val mod = i % bound
      if i + (bound - 1) - mod >= 0 then unit(mod) else nonNegativeLessThan(bound)

end RNG

import State.*

case class State[S, +A](run: S => (A, S)):

  // Exercise 9 (methods in class State)
  // Search for the second part (sequence) below
  
  def flatMap[B](f: A => State[S, B]): State[S, B] = 
    State { s =>
      val (a, sa) = run(s)
      val h = f(a)
      h.run(sa)
      }

  def map[B](f: A => B): State[S, B] = 
    State { s =>
      val (a, sa) = run(s)
      (f(a), sa)
      }

  def map2[B,C](sb: State[S, B])(f: (A, B) => C): State[S, C] = 
    State { s =>
      val (a, sa) = run(s)
      val (b, sab) =  sb.run(sa)
      (f(a,b), sab)
      }


object State:

  def unit[S, A](a: A): State[S, A] =
    State { s => (a, s) }

  def modify[S](f: S => S): State[S, Unit] = for
    s <- get // Gets the current state and assigns it to `s`.
    _ <- set(f(s)) // Sets the new state to `f` applied to `s`.
  yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

  // Now Rand can be redefined like this (we keep it here in the State object,
  // to avoid conflict with the other Rand in RNG).
  type Rand[A] = State[RNG, A]

  // Exercise 9 (sequence, continued)
 
  def sequence[S,A](sas: List[State[S, A]]): State[S, List[A]] =
    sas.foldRight(unit[S, List[A]](Nil))((sa, acc) => sa.map2(acc)((element, list) => element :: list))


  import adpro.lazyList.LazyList

  // Exercise 10 (stateToLazyList)
  
  // def unfold[A,S](z: S)(f: S => Option[(A, S)]): LazyList[A] =
    //f(z).map((a, b) => cons(a, unfold(b)(f))).getOrElse(empty)
  def stateToLazyList[S, A](s: State[S,A])(initial: S): LazyList[A] =
    s,

  // Exercise 11 (lazyInts out of stateToLazyList)
  
  def lazyInts(rng: RNG): LazyList[Int] = 
    ???

  lazy val tenStrictInts: List[Int] = 
    ???

end State

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
	dotty.tools.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:28)
	dotty.tools.pc.ScalaPresentationCompiler.semanticdbTextDocument$$anonfun$1(ScalaPresentationCompiler.scala:303)
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