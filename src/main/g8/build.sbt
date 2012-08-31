import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

organization := "$package$"

name := "unfiltered-heroku-neo4j"

scalaVersion := "2.9.1"

version := "0.1.0-SNAPSHOT"

libraryDependencies ~= { seq =>
  val dispatch = "0.9.0"
  val unfiltered = "0.6.4"
  seq ++ Seq(
    "net.databinder.dispatch" %% "core" % dispatch,
    "net.databinder" %% "unfiltered-netty-server" % unfiltered,
    "net.databinder" %% "unfiltered-spec" % unfiltered % "test"
  )
}

resolvers ++= Nil

neo4jHome := "neo4j_home"

neo4jVersion := "1.8.M07"

initialCommands := "import dispatch._"

