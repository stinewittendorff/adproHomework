error id: 5DBA5FE9272A756D3B49A6CA5C2F30B9
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/04-lazy-list/Exercises.scala
### java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing()" because the return value of "dotty.tools.dotc.parsing.Scanners$Indented.outer()" is null

occurred in the presentation compiler.



action parameters:
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
    case Cons(h,t) if n == 0 => h,t
    case _ => empty
  

  // Exercise 4

  def takeWhile(p: A => Boolean): LazyList[A] = 
    ???

  // Exercise 5
  
  def forAll(p: A => Boolean): Boolean =
    ???
 
  // Note 1. lazy; tail is never forced if satisfying element found this is
  // because || is non-strict
  // Note 2. this is also tail recursive (because of the special semantics
  // of ||)
  def exists(p: A => Boolean): Boolean = 
    ???

  // Exercise 6
  
  def takeWhile1(p: A => Boolean): LazyList[A] =
    ???

  // Exercise 7
  
  def headOption1: Option[A] = 
    ???

  // Exercise 8
  
  // Note: The type is incorrect, you need to fix it
  def map(f: Any): LazyList[Int] = 
    ???

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
	dotty.tools.dotc.parsing.Parsers$Parser.caseClause(Parsers.scala:3262)
	dotty.tools.dotc.parsing.Parsers$Parser.$anonfun$22$$anonfun$1(Parsers.scala:2712)
	dotty.tools.dotc.parsing.Parsers$Parser.caseClauses(Parsers.scala:3226)
	dotty.tools.dotc.parsing.Parsers$Parser.$anonfun$22(Parsers.scala:2712)
	dotty.tools.dotc.parsing.Parsers$Parser.enclosed(Parsers.scala:623)
	dotty.tools.dotc.parsing.Parsers$Parser.inBracesOrIndented(Parsers.scala:653)
	dotty.tools.dotc.parsing.Parsers$Parser.matchClause(Parsers.scala:2712)
	dotty.tools.dotc.parsing.Parsers$Parser.recur$4(Parsers.scala:1268)
	dotty.tools.dotc.parsing.Parsers$Parser.infixOps(Parsers.scala:1277)
	dotty.tools.dotc.parsing.Parsers$Parser.postfixExprRest(Parsers.scala:2810)
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
	dotty.tools.dotc.parsing.Parsers$Parser.$anonfun$47(Parsers.scala:4789)
	dotty.tools.dotc.parsing.Parsers$Parser.withinEnum(Parsers.scala:447)
	dotty.tools.dotc.parsing.Parsers$Parser.template(Parsers.scala:4789)
	dotty.tools.dotc.parsing.Parsers$Parser.enumDef(Parsers.scala:4403)
	dotty.tools.dotc.parsing.Parsers$Parser.tmplDef(Parsers.scala:4339)
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