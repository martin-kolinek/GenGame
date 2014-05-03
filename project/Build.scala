import sbt._
import Keys._

object GenGameBuild extends Build {
	val buildSettings = Defaults.defaultSettings ++ Seq(
		name:="GenGame",
		version:="0.1",
		scalaVersion:="2.11.0",
		fork:=true,
		resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
		resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
		libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.5" % "test",
		libraryDependencies += "com.typesafe.slick" % "slick_2.11.0-RC4" % "2.1.0-M1",
		libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.7.2",
		libraryDependencies += "org.spire-math" %% "spire" % "0.7.4",
                libraryDependencies += "org.kolinek" %% "scala-deriving" % "0.2.0", 
		libraryDependencies ++= List("com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
							"ch.qos.logback" % "logback-core" % "1.1.2",
							"ch.qos.logback" % "logback-classic" % "1.1.2",
							"org.slf4j" % "slf4j-api" % "1.7.7",
							"org.slf4j" % "jul-to-slf4j" % "1.7.7"),
		libraryDependencies ++= List("com.jme3" % "jME3-core" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jmonkeyengine3" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-lwjgl" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-lwjgl-natives" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "lwjgl" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-jbullet" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jbullet" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-blender" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-desktop" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-plugins" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-terrain" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-jogg" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "j-ogg-oggd" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "j-ogg-vorbisd" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jinput" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "eventbus" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "stack-alloc" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "vecmath" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "xmlpull-xpp3" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "jME3-niftygui" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "nifty" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "nifty-default-controls" % "3.0.0-SNAPSHOT"
                            ,"com.jme3" % "nifty-style-black" % "3.0.0-SNAPSHOT"),
        scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
	)
	
	lazy val main = Project("main", file("."), settings = buildSettings)
}
