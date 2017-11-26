package com.antSmash.admin.model


class RealTimeData(//var id : java.lang.String,
                  val stationId : String,
                //  var date : String,
                  val time      : String,
                //  var dateTime : java.lang.Long = _
                  val temperature : String,
                //  var maxTemperature : String = _
                //  var minTemperature : String = _
                //
                //  var nearGroundTemperature : String = _
                //  var humidity : String = _
                //  var rain : String = _

                  val day   : Int,
                  val month : Int,
                  val year  : Int) {

  override def toString() = {
    s"$stationId $day-$month-$year $time $temperature"
  }
}