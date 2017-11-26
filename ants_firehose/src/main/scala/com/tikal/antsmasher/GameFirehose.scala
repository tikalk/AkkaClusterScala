package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.tikal.antsmasher.AntsGenerator.{Start, Stop}


class GameFirehose(id : String) extends Actor with ActorLogging {
  var antsGeneratorActor : ActorRef = _ ;
  var antsProduceActor : ActorRef = _
  override def receive: Receive = {
    case "create" =>
      antsGeneratorActor = context.actorOf(Props(classOf[AntsGenerator]), "antsGeneratorActor")
      antsProduceActor = context.actorOf(Props(classOf[AntsKafkaProducer]), "antsProducerActor")
    case "start" =>
      antsGeneratorActor ! Start("", antsProduceActor)
    case "pause" =>
      None
    case "resume" =>
      None
    case "stop" =>
      antsGeneratorActor ! Stop("")
      try {      Thread.sleep(1000) }      finally {}
      context.stop(self)

    // + terminate myself
  }
}