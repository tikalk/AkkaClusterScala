package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.tikal.antsmasher.AntsGenerator.{Produce, Start, Stop}

import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object AntsFirehoseApp {
  def main(args: Array[String]): Unit = {
    println("main started")
    val system = ActorSystem("AntsFirehose")
    val antsGeneratorActor = system.actorOf(Props(classOf[AntsGenerator]), "antsGeneratorActor")
    val antsProduceActor = system.actorOf(Props(classOf[AntsProducer]), "antsProducerActor")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
    antsGeneratorActor ! Start("", antsProduceActor)
    try {      Thread.sleep(20000) }      finally {}

    antsGeneratorActor ! Stop("")
    println("main finished")
  }
}

class AntsProducer extends Actor with ActorLogging {
  override def receive: Receive = {
    case Produce(ants) => {
      // send all ants in the list to Kafka
      // https://github.com/cakesolutions/scala-kafka-client/wiki/Scala-Kafka-Client
      // or
      // https://www.programcreek.com/scala/akka.kafka.ProducerSettings  example 18
      ants.foreach(antLocation => log.info("push ant to kafka " + antLocation))
    }
    case x : Any => log.info("unknown message " + x)
  }
}

object AntsGenerator {
  case class Start(id:String, ref : ActorRef);
  case class Stop(id:String);
  case class Produce(ants : List[AntLocation])
}

case class AntLocation(id : String, x : Int, y : Int);


class AntsGenerator extends Actor with ActorLogging {
  var allAnts = List.empty[AntLocation];
  var started : Boolean = false ;

  def moveAnt(antlocation: AntLocation): AntLocation = {
    AntLocation( antlocation.id, antlocation.x + 1, antlocation.y + 1) ;

  }

  def produceAnts(): Unit = {
    log.info("produceAnts started")
    Random.setSeed(System.nanoTime());

    while (started) {
      log.info("while " + allAnts.size)
      allAnts = allAnts.map(antLocation => moveAnt(antLocation))

      // once out of 10, create a new ant
      if (Random.nextInt(10) <= 3 ) {
        allAnts = AntLocation(System.nanoTime().toString , 0, 0) :: allAnts ;
      }

      // sleep 1 second // TODO replace with a scheduled actor?
      try {      Thread.sleep(1000) }      finally {}
    }
    log.info("done while")
  }

  override def receive: Receive = {
    case x : Start => {
      log.info("start")
      started = true ;
      Future {
        produceAnts()
        x.ref ! Produce(allAnts)
      }
    }
    case x : Stop => {
      log.info("stop")
      started = false ;
    }
    case x : Any => log.info("unknown message " + x)
  }
}

