package com.antSmash.admin.model

case class Team( name : String) {
  val user = User("sam")//"sam", "cardonis", "samuelc@tikalk.com")
  user.email    = "samuelc@tikalk.com"
  user.fistName = "sam"
  user.lastName = "cardonis"
  val player1 = Player("p1", user)
  val player2 = Player("p2", user)
  val player3 = Player("p3" ,user)
  val ant1 = AntSpecie("specie1")
  val ant2 = AntSpecie("specie1")
  val ant3 = AntSpecie("specie1")
}
