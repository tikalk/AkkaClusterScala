package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}
import com.tikal.antsmasher.messaging.KafkaConsumer.ConsumeMessages
import com.tikal.antsmasher.messaging.{KafkaConsumer, WebSocketPublisher}
import com.tikal.antsmasher.traits.AntsConsumer

import scala.io.StdIn

object AntsPublisherApp {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("AntsPublisher")

    try {
      val publisher = system.actorOf(Props[WebSocketPublisher], "webSocketPublisher")
      val consumer = system.actorOf(Props(new KafkaConsumer(system, publisher)))
      consumer ! ConsumeMessages
    }

    finally {
//      system.terminate()
    }
  }
}
