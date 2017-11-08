name := "datastruc"

version := "1.0"

scalaVersion := "2.12.3"

//resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "junit" % "junit" % "4.12"


// for debugging sbt problems
logLevel := Level.Debug

scalacOptions += "-deprecation"
//testOptions += Tests.Argument(TestFrameworks.JUnit, --tests=<REGEXPS>)