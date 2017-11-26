package com.antSmash.admin.model

import javax.persistence.{GeneratedValue,Entity, Id}


import scala.beans.BeanProperty

@Entity
case class User(firstName: String ) {//
  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var fistName: String = _

  @BeanProperty
  var lastName: String = _

  @BeanProperty
  var email: String = _

}

object User {
  def apply( firstName: String, lastName: String, email: String): User = {
    new User( firstName )
  }


}



