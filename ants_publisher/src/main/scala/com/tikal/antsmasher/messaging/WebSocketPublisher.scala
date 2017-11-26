package com.tikal.antsmasher.messaging

import akka.actor.{Actor, ActorLogging}
import com.tikal.antsmasher.messaging.KafkaConsumer.GameMessage
import com.tikal.antsmasher.traits.Publisher

object WebSocketPublisher {

}

class WebSocketPublisher extends Actor with ActorLogging with Publisher {
  override def receive = {

    case message : GameMessage => println("Publisher got1: " + message)
    case message  => println("Publisher got2: " + message)

  }
}
