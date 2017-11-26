package com.tikal.antsmasher

sealed class AntType ;
class RedFire extends AntType ;
class BlueFire extends AntType ;
class GreenFire extends AntType ;

class Ant(id : String, antType : AntType) {

}
