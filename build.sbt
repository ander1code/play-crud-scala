name := """play-crud-scala"""
organization := "com.playcrudscala"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
libraryDependencies += "com.roundeights" %% "hasher" % "1.2.0"

libraryDependencies += jdbc
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212"