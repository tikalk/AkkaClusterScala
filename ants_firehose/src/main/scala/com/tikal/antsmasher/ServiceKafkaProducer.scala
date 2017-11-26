package com.tikal.antsmasher

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
//import play.api.Configuration

object ServiceKafkaProducer {
  val configuration = Map("kafka.bootstrap.servers" -> "localhost:9092")
}

class ServiceKafkaProducer(topicName: String, actorSystem: ActorSystem) {
                           //configuration: Map[String, String]) {
  val bootstrapServers: String = ServiceKafkaProducer.configuration.getOrElse("kafka.bootstrap.servers", "localhost:9002")
    //.getOrElse(throw new Exception("No config for kafka bootstrap servers!"))

  val producerSettings: ProducerSettings[String, String] = ProducerSettings(actorSystem, new StringSerializer,
    new StringSerializer)
    .withBootstrapServers(bootstrapServers)

  val producer: KafkaProducer[String, String] = producerSettings.createKafkaProducer

  def send(logRecordStr: String): Unit = {
    println("kafka send started")
    producer.send(new ProducerRecord(topicName, logRecordStr))
    println("kafka send finished")
  }

}