package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object AntsFirehoseApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("AntsFirehose")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
