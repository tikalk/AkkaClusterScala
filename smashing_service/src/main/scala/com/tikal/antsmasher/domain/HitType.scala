package com.tikal.antsmasher.domain

object HitType extends Enumeration{
  type Type = Value
  val SelfSmash = Value(0)
  val Smash = Value(1)
  val Hit = Value(2)
  val Miss = Value(3)

  override def toString() =
    this match {
      case Smash => "Smash"
      case SelfSmash => "SelfSmash"
      case Hit=> "Hit"
      case Miss => "Miss"
    }
}
