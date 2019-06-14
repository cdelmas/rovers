# Rovers app

## How to run?

You'll need [sbt](www.scala-sbt.org). Then:

```sbt "run-main rovers.RoversApp input.txt"```

You can run the tests with:

```sbt test```

## Design

Most of the domain model is in the object `domain`. Design choices are mostly documented there. The main test efforts are
for this module, as it is the business module. I used property based testing to test the moves.

To parse the input in a functional style, I used [atto](http://tpolecat.github.io/atto/). I designed the `parser` using 
the REPL, so unlike the domain model, there are few tests because there is only one function in this module.

Finally, there is the `io` package. This package contains file reading and console writing stuff: algebras along with the
corresponding interpreters. I chose to not write any test for this module, as implementation is straightforward.

The program itself is in `RoversApp`. It is designed using the tagless final encoding, allowing efficient effect handling, 
and can be tested -- see this as an integration test -- using stub interpreters (typically using `Id` as `F`).
