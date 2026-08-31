error id: E3B73D9A59608594A59CF3FE70378FAE
file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/01-intro/Exercises.scala
### java.lang.StringIndexOutOfBoundsException: Index 0 out of bounds for length 0

occurred in the presentation compiler.



action parameters:
offset: 1141
uri: file://<HOME>/Desktop/Kandidat%20ITU/Advanced%20Programming/adproHomework/01-intro/Exercises.scala
text:
```scala
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
      if n == 0 then a
      else loop(n - 1, b, a + b)
    loop(n, 0, 1)

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
  f@@

def isSortedCurried[A]: Array[A] => ((A, A) => Boolean) => Boolean =
  ???

// Exercise 6

def uncurry[A, B, C](f: A => B => C): (A, B) => C =
  ???

def isSortedCurriedUncurried[A]: (Array[A], (A, A) => Boolean) => Boolean =
  ???

// Exercise 7

def compose[A, B, C](f: B => C, g: A => B): A => C =
  ???

```


presentation compiler configuration:
Scala version: 3.8.4-bin-nonbootstrapped
Classpath:
<WORKSPACE>/01-intro/.scala-build/01-intro_94cb20a2e3/classes/main [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.4/scala3-library_3-3.8.4.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.4/scala-library-3.8.4.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.12.3/semanticdb-javac-0.12.3.jar [exists ], <WORKSPACE>/01-intro/.scala-build/01-intro_94cb20a2e3/classes/main/META-INF/best-effort [missing ]
Options:
-Werror -deprecation -feature -source:future -language:adhocExtensions -Xsemanticdb -sourceroot <WORKSPACE>/01-intro -Ywith-best-effort-tasty




#### Error stacktrace:

```
java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:55)
	java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:52)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:213)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:210)
	java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:98)
	java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
	java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
	java.base/java.lang.String.checkIndex(String.java:4904)
	java.base/java.lang.StringLatin1.charAt(StringLatin1.java:45)
	java.base/java.lang.String.charAt(String.java:1624)
	scala.meta.internal.metals.Fuzzy.matchesName(Fuzzy.scala:293)
	scala.meta.internal.metals.Fuzzy.$anonfun$2(Fuzzy.scala:76)
	scala.meta.internal.metals.Fuzzy.$anonfun$adapted$2(Fuzzy.scala:76)
	scala.meta.internal.metals.Fuzzy.loopDelimiters$1(Fuzzy.scala:100)
	scala.meta.internal.metals.Fuzzy.genericMatches(Fuzzy.scala:127)
	scala.meta.internal.metals.Fuzzy.matches(Fuzzy.scala:77)
	dotty.tools.pc.completions.Completions.fuzzyMatcher$lzyINIT1$$anonfun$1(Completions.scala:123)
	dotty.tools.dotc.interactive.Completion$Completer.dotty$tools$dotc$interactive$Completion$Completer$$include(Completion.scala:674)
	dotty.tools.dotc.interactive.Completion$Completer$$anon$5.applyOrElse(Completion.scala:705)
	dotty.tools.dotc.interactive.Completion$Completer$$anon$5.applyOrElse(Completion.scala:704)
	scala.collection.immutable.List.collect(List.scala:261)
	scala.collection.immutable.List.collect(List.scala:254)
	dotty.tools.dotc.interactive.Completion$Completer.accessibleMembers(Completion.scala:706)
	dotty.tools.dotc.interactive.Completion$Completer.scopeCompletions$lzyINIT1$$anonfun$1(Completion.scala:424)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:632)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:336)
	dotty.tools.dotc.core.Contexts$Context$$anon$2.foreach(Contexts.scala:136)
	dotty.tools.dotc.interactive.Completion$Completer.scopeCompletions$lzyINIT1(Completion.scala:414)
	dotty.tools.dotc.interactive.Completion$Completer.scopeCompletions(Completion.scala:404)
	dotty.tools.dotc.interactive.Completion$.computeCompletions(Completion.scala:259)
	dotty.tools.dotc.interactive.Completion$.rawCompletions(Completion.scala:93)
	dotty.tools.pc.completions.Completions.enrichedCompilerCompletions(Completions.scala:137)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:179)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:149)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:214)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: Index 0 out of bounds for length 0