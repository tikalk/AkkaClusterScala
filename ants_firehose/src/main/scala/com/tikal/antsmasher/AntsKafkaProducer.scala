package com.tikal.antsmasher

import akka.actor.{Actor, ActorLogging}
import com.tikal.antsmasher.AntsGenerator.Produce

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



object AntsKafkaProducer {
  val TOPIC = "ANTS_STREAM"
}


class AntsKafkaProducer extends Actor with ActorLogging {

  val kafkaProducer : ServiceKafkaProducer = new ServiceKafkaProducer(AntsKafkaProducer.TOPIC, context.system)

  override def receive: Receive = {
    case Produce(ants) => {
      log.info(s"started to push ants list of size ${ants.size} to kafka")
      // send all ants in the list to Kafka
      // https://github.com/cakesolutions/scala-kafka-client/wiki/Scala-Kafka-Client
      // or
      // https://www.programcreek.com/scala/akka.kafka.ProducerSettings  example 18
      ants.foreach(antLocation => {
        log.info("push ant to kafka " + antLocation)
        Future { kafkaProducer.send(antLocation.toJson) }
      })
    }
    case x : Any => log.info("unknown message " + x)
  }
}

