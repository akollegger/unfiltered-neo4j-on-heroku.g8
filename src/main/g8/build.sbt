import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

organization := "org.akollegger"

name := "unfiltered-heroku-neo4j"

scalaVersion := "2.9.1"

version := "0.1.0-SNAPSHOT"

libraryDependencies ~= { seq =>
  val dispatch = "0.8.8"
  val unfiltered = "0.6.1"
  seq ++ Seq(
    "net.databinder" %% "dispatch-http" % dispatch,
    // dispatch-lift-json is a source dependency. see project/build.scala
    // "net.databinder" %% "dispatch-lift-json" % "0.8.7",
    "net.databinder" %% "unfiltered-filter" % unfiltered,
    "net.databinder" %% "unfiltered-jetty" % unfiltered
  )
}

resolvers ++= Nil

neo4jHome := "neo4j_home"

neo4jVersion := "1.7-SNAPSHOT"

initialCommands := "import dispatch._"

