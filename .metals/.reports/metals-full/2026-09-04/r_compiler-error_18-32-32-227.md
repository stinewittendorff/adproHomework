error id: BB8C0E78496539AAB5B969BF142EBD93
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/02-adt/Exercises.scala
### java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.commasExpectedInEnclosing()" because the return value of "dotty.tools.dotc.parsing.Scanners$Indented.outer()" is null

occurred in the presentation compiler.



action parameters:
offset: 2608
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
    case Cons(h,t) => List.foldLeft(t,f(h,z)@@),f)
  
    

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
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:43)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:463)
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