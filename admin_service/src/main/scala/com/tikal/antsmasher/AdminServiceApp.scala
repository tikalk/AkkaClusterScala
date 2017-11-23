package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object AdminServiceApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("AdminService")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
