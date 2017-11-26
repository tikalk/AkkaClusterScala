package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.tikal.antsmasher.AntsGenerator.{Produce, Start, Stop}
import com.tikal.antsmasher.controller.{ChangeGameState, Game, WebServer}

import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object AntSpecies {
  val A : String = "A"
  val B : String = "B"
  val C : String = "C"
}

object AntsFirehoseApp {
  var system : ActorSystem = _   //= ActorSystem("AntsFirehose")

  def main(args: Array[String]): Unit = {
    Random.setSeed(System.nanoTime())
    println("main started")

    // starting the akka actor system
    system = ActorSystem("AntsFirehose")

    // Starting the web server
    WebServer.startServer("localhost", 8080)

    val g = ChangeGameState ;// new ChangeGameState()
    val theGame = Game("1", "not started")
    g.createGame(theGame)
    g.startGame(theGame)
//    val antsGeneratorActor = system.actorOf(Props(classOf[AntsGenerator]), "antsGeneratorActor")
//    val antsProduceActor = system.actorOf(Props(classOf[AntsKafkaProducer]), "antsProducerActor")
//    antsGeneratorActor ! Start("", antsProduceActor)
    try {      Thread.sleep(20000) }      finally {}
    g.stopGame(theGame)
//    antsGeneratorActor ! Stop("")
    println("main finished")
  }
}





