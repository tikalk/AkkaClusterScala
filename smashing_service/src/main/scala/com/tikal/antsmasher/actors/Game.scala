package com.tikal.antsmasher.actors

import akka.actor.Actor
import com.tikal.antsmasher.SmashingServiceApp
import com.tikal.antsmasher.domain.Ant
import com.tikal.antsmasher.services.KafkaProducerService
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Game(val gameId: Integer) extends Actor {
  val kafkaProducer: KafkaProducerService = new KafkaProducerService(SmashingServiceApp.SMASH_TOPIC, context.system)

  override def receive = {
    case ant: Ant => {
      implicit val formats = DefaultFormats
      //      val ant = parse(js).extract[Ant]

      val document = render(Extraction.decompose(ant))
      val t = compact(document)
      Future {
        kafkaProducer.send(t)
      }
    }
  }
}
