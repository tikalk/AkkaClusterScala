package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.concurrent.Future
import scala.util.Random
import com.tikal.antsmasher.AntsGenerator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}


case class AntLocation(id : String, x : Int, y : Int, species : String) {
  def toJson() : String = {
    "{\"id\":\"" + id + "\",\"species\":\"" + species + "\",\"xPromil\":" + x + ",\"yPromil\":" + y + "}"

  }
}


object AntsGenerator {
  case class Start(id:String, ref : ActorRef);
  case class Stop(id:String);
  case class Produce(ants : List[AntLocation])
}


class AntsGenerator extends Actor with ActorLogging {
  var allAnts = List.empty[AntLocation];
  var started : Boolean = false ;
  var counter = 1

  def moveAnt(antlocation: AntLocation): Option[AntLocation] = {
    def decideMove() : (Int, Int) = {
      (if (Random.nextInt(2) == 0) -1 else 1,     // x coordinate - same probability for left and right
        if (Random.nextFloat()<.25) -1 else 1     // y coordinate - go back only in 25% of the time
      )
    }

    val (deltaX, deltaYorig) = decideMove()
    val newX = antlocation.x + deltaX
    // fix deltaY - if ant in the first 10%, go forward
    val deltaY = if (antlocation.y < 10) 1 else deltaYorig
    val newY = antlocation.y + deltaY
    if ((newX >= 100) || (newY >= 100) || (newX < 0) || (newY < 0)) {
      None
    }
    else {
      Some(AntLocation(antlocation.id, newX, newY, antlocation.species))
    }
  }

  def produceAnts(): Unit = {
    log.info("produceAnts started")
    Random.setSeed(System.nanoTime());

    while (started) {
      log.info("while " + allAnts.size)
      allAnts = allAnts.flatMap(antLocation => moveAnt(antLocation))

      // once out of 10, create a new ant
      if (Random.nextInt(10) <= 3 ) {
        val newAnt = generateNewAnt()
        allAnts = ( newAnt :: allAnts ) .sortWith((x,y) => x.id<y.id);
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
      case AntSpecies.A => (25,0)
      case AntSpecies.B => (50,0)
      case AntSpecies.C => (75,0)
    }

    counter+=1
    val c : String = counter.toString
    AntLocation(id = c + "-" + System.nanoTime().toString, x , y , species)
  }


  override def receive: Receive = {
    case x : Start => {
      log.info("start")
      started = true ;
      Future {
        produceAnts()
      }
      Future {
        while(started) {
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