import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import scoverage.ScoverageSbtPlugin._

object GenGameBuild extends Build {

    val jmeVersion = "3.0.0.20140325-SNAPSHOT"

    val buildSettings = Defaults.defaultSettings ++ instrumentSettings ++ Seq(
        name := "GenGame",
        version := "0.1",
        scalaVersion := "2.11.2",
        fork := true,
        resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
        libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test",
         libraryDependencies += "junit" % "junit" % "4.8.1" % "test",
        libraryDependencies += "com.typesafe.slick" %% "slick" % "2.1.0-RC1",
        libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.7.2",
        libraryDependencies += "org.spire-math" %% "spire" % "0.7.5",
        libraryDependencies += "org.kolinek" %% "scala-deriving" % "0.2.1",
        libraryDependencies += "org.easymock" % "easymock" % "2.5" % "test",
        libraryDependencies ++= List("com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
            "ch.qos.logback" % "logback-core" % "1.1.2",
            "ch.qos.logback" % "logback-classic" % "1.1.2",
            "org.slf4j" % "slf4j-api" % "1.7.7",
            "org.slf4j" % "jul-to-slf4j" % "1.7.7"),
        libraryDependencies ++= List("com.typesafe" % "config" % "1.2.1",
            "net.ceedubs" %% "ficus" % "1.1.1"),
        libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.19.6",
        libraryDependencies += "com.chuusai" %% "shapeless" % "2.0.0",
        libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.1",
        libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2",
        libraryDependencies ++= List("com.jme3" % "jME3-core" % jmeVersion, "com.jme3" % "jmonkeyengine3" % jmeVersion, "com.jme3" % "jME3-lwjgl" % jmeVersion, "com.jme3" % "jME3-lwjgl-natives" % jmeVersion, "com.jme3" % "lwjgl" % jmeVersion, "com.jme3" % "jME3-jbullet" % jmeVersion, "com.jme3" % "jbullet" % jmeVersion, "com.jme3" % "jME3-blender" % jmeVersion, "com.jme3" % "jME3-desktop" % jmeVersion, "com.jme3" % "jME3-plugins" % jmeVersion, "com.jme3" % "jME3-terrain" % jmeVersion, "com.jme3" % "jME3-jogg" % jmeVersion, "com.jme3" % "j-ogg-oggd" % jmeVersion, "com.jme3" % "j-ogg-vorbisd" % jmeVersion, "com.jme3" % "jinput" % jmeVersion, "com.jme3" % "eventbus" % jmeVersion, "com.jme3" % "stack-alloc" % jmeVersion, "com.jme3" % "vecmath" % jmeVersion, "com.jme3" % "xmlpull-xpp3" % jmeVersion, "com.jme3" % "jME3-niftygui" % jmeVersion, "com.jme3" % "nifty" % jmeVersion, "com.jme3" % "nifty-default-controls" % jmeVersion, "com.jme3" % "nifty-style-black" % jmeVersion),
        scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
        ScoverageKeys.highlighting := true)

    lazy val main = Project("main", file("."), settings = buildSettings) aggregate (macroSub) dependsOn (macroSub)

    lazy val macroSub = Project("macro", file("macro")) settings (
        name := "GenGame-macro",
        version := "0.1",
        scalaVersion := "2.11.2",
        libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.2",
        EclipseKeys.skipParents in ThisBuild := false)
}
