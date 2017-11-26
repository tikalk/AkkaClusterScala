package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.tikal.antsmasher.AntsGenerator.{Produce, Start, Stop}

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
  def main(args: Array[String]): Unit = {
    Random.setSeed(System.nanoTime())
    println("main started")
    val system = ActorSystem("AntsFirehose")
    val antsGeneratorActor = system.actorOf(Props(classOf[AntsGenerator]), "antsGeneratorActor")
    val antsProduceActor = system.actorOf(Props(classOf[AntsKafkaProducer]), "antsProducerActor")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
    antsGeneratorActor ! Start("", antsProduceActor)
    try {      Thread.sleep(20000) }      finally {}

    antsGeneratorActor ! Stop("")
    println("main finished")
  }
}


object AntsGenerator {
  case class Start(id:String, ref : ActorRef);
  case class Stop(id:String);
  case class Produce(ants : List[AntLocation])
}

case class AntLocation(id : String, x : Int, y : Int, species : String);


class AntsGenerator extends Actor with ActorLogging {
  var allAnts = List.empty[AntLocation];
  var started : Boolean = false ;

  def moveAnt(antlocation: AntLocation): AntLocation = {
    AntLocation( antlocation.id, antlocation.x + 1, antlocation.y + 1, antlocation.species) ;

  }

  def produceAnts(): Unit = {
    log.info("produceAnts started")
    Random.setSeed(System.nanoTime());

    while (started) {
      log.info("while " + allAnts.size)
      allAnts = allAnts.map(antLocation => moveAnt(antLocation))

      // once out of 10, create a new ant
      if (Random.nextInt(10) <= 3 ) {
        val newAnt = generateNewAnt()
        allAnts =  newAnt :: allAnts ;
      }

      // sleep 1 second // TODO replace with a scheduled actor?
      try {      Thread.sleep(1000) }      finally {}
    }
    log.info("done while")
  }


  def generateNewAnt() : AntLocation = {
    val r = Random.nextInt(3)   // 0, 1, 2
    val species = r match {
      case 0 => AntSpecies.A
      case 1 => AntSpecies.B
      case 2 => AntSpecies.C
      case _ => AntSpecies.A
    }
    val (x, y) = species match {
      case AntSpecies.A => (100,0)
      case AntSpecies.B => (0,0)
      case AntSpecies.C => (0,100)
    }

    AntLocation(id = System.nanoTime().toString, x , y , species)
  }


  override def receive: Receive = {
    case x : Start => {
      log.info("start")
      started = true ;
      Future {
        produceAnts()
      }
      Future {
        while(true) {
          x.ref ! Produce(allAnts) // sends list of all ant locations to AntsKafkaProducer Actor
          try {      Thread.sleep(1000) }      finally {}
        }
      }
    }
    case x : Stop => {
      log.info("stop")
      started = false ;
    }
    case x : Any => log.info("unknown message " + x)
  }
}

