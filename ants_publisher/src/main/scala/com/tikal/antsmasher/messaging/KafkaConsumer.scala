package com.tikal.antsmasher.messaging

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.google.gson.Gson
import com.tikal.antsmasher.messaging.KafkaConsumer.{ConsumeMessages, GameMessage}
import com.tikal.antsmasher.traits.AntsConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.Future

object KafkaConsumer {
  case class ConsumeMessages ()
  case class GameMessage (id : String, x : Int, y : Int, species : String)
}

class KafkaConsumer (val system: ActorSystem, val publisher : ActorRef) extends Actor with ActorLogging with AntsConsumer {

  implicit val materializer = ActorMaterializer()

  override def preStart()  {


  }

  override def postStop() {

  }

  override def receive = {
    case ConsumeMessages => consumeMessages()
  }

  def consumeMessages (): Unit = {
    val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("CoSmashingAnts")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val gson = new Gson

    try {

      Consumer.committableSource(consumerSettings, Subscriptions.topics("mytopic123"))
        .mapAsync(1) {
          message =>
            println("Consumer got: " + message)
//            println("value: " + message.record.value())
//            val innerMessage = gson.fromJson(message.record.value(), classOf[GameMessage])
//            println("fromJson: " )
//            publisher ! innerMessage
            publisher ! message.record.value()
            Future.successful(message)
        }
        .mapAsync(1) {
          message =>
            message.committableOffset.commitScaladsl()
            Future.successful(Done)
        }
        .runWith(Sink.ignore)
    }

    catch {
      case ex : Exception => {
        println(ex.getMessage)
        ex.printStackTrace()
      }
    }
  }
}
