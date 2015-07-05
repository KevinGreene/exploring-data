# Exploring Data

The source code designed to accompany the workshop "Exploring Data With Clojure"
for LambdaJam 2015.

## Why

Clojure is a great tool for exploring and analyzing data

* Being based on the JVM, Clojure has access to a wealth of libraries
* Being based on the JVM, Clojure has access to a wealth of distributed
computing and tuning tools
* The REPL allows for testing code as it is written and exploring the data
* Boot allows you to create simple scripts


## Usage

As of right now, load up the files in your favorite Editor + REPL, and have fun.

I'd personally recommend Emacs, with a great start being available here:
http://www.braveclojure.com/basic-emacs/


# Alembic

For exploring data, I'd also recommend editing your Leiningen profile,
located at `~/.lein/profiles.clj` to include a dependency on `[alembic "0.3.2"]`

This way, if you're ever in a REPL and want to add another library, you can run

```
(use '[alembic.still])
(alembic.still/distill '[new.library "0.0.1"])
```

Or you can edit your project.clj file, and run

```
(use '[alembic.still])
(alembic.still/load-project)
```

to reload the Leiningen project

## Guide

Intro - Get acquainted with the very basics of Clojure, functions, and sequences

Getting Data - How to load data from files and the web

Math - Using core.matrix

Pictures - Using Incanter

Production - Using Storm / Onyx

Case Studies:

Real Estate

Divvy

Board Game Geek (Bayesian probability with board games)

## License

The MIT License (MIT)

Copyright (c) 2015 Kevin Greene

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
