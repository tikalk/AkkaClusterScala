package com.tikal.antsmasher.services

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProducerService {
  val configuration = Map("kafka.bootstrap.servers" -> "localhost:9092")
}

class KafkaProducerService(topicName: String, actorSystem: ActorSystem) {
  //configuration: Map[String, String]) {
  val bootstrapServers: String = KafkaProducerService.configuration.getOrElse("kafka.bootstrap.servers", "localhost:9002")
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