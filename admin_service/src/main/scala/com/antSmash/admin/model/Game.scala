package com.antSmash.admin.model

import javax.persistence.{GeneratedValue, Entity, Id}

import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

@Entity
case class Game() {

  //val log  = LoggerFactory.getLogger(this.getClass.getSimpleName)


  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var name: String = _

  //log.info("a new game named "+name+"  with 3 teams started")


//  val team1 = Team("team1")
//  val team2 = Team("tema2")
//  val team3 = Team("team3")
//

  @BeanProperty
  val state : State =new State("notStarted")

}





