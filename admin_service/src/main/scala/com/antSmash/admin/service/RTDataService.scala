package com.antSmash.admin.service

import com.antSmash.admin.model.RealTimeData
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service

import scala.io.Source

@Service
class RTDataService {
  
  private val logger: Logger = LoggerFactory.getLogger(classOf[RTDataService])

  // temporary
  def getDataFromIms(): scala.List[RealTimeData] = List(
    new RealTimeData("148","10:00:00","27.0", 1, 8, 2017),
    new RealTimeData("148","10:10:00","27.1", 1, 8, 2017),
    new RealTimeData("148","10:20:00","27.2", 1, 8, 2017),
    new RealTimeData("148","10:30:00","27.3", 1, 8, 2017),
    new RealTimeData("148","10:40:00","27.4", 1, 8, 2017),
    new RealTimeData("148","10:50:00","27.5", 1, 8, 2017)) ;


  def findAllStations(): Map[String, String] = {
    Map("178" -> "Tel-Aviv", "22" -> "Jerusalem");
  }
  // this is correct implementation (except the URL prefix)
  def readFromIms(station : String, month : Int, year : Int, channel : String = "TD") : List[RealTimeData] = {
    try {
      // change to correct ngrok according to Instructions from Evyatar   : 178, 2015-01-01T00:10:00+02:00, TD, 14.8
      val list1 :List[String] = Source.fromURL(s"http://ed7378f8.ngrok.io/csv/monthly/$channel/$station/$year/$month").getLines().toList
      val list22 = list1.map( x =>
        {
          val arrStr = x.split(", ")
          val time        : String = arrStr(1).split("T")(1).split('+')(0)
          val temperature : String = arrStr(3)
          val day   : Int=arrStr(1).split("T")(0).split('-')(2).toInt
          val month : Int=arrStr(1).split("T")(0).split('-')(1).toInt
          val year  : Int=arrStr(1).split("T")(0).split('-')(0).toInt
          val  rtd  : RealTimeData = new RealTimeData(station, time,temperature,day,  month, year)
          rtd
        }
      )
//
//
//      val list2 = for( e :String<- list1) yield {
//
//        val arrStr = e.split(",")
//        val time      : String =arrStr(1).split("T")(1).split('+')(0)
//        val temperature : String =arrStr(3)
//        val day   : Int=arrStr(1).split("T")(0).split('-')(2).toInt
//        val month : Int=arrStr(1).split("T")(0).split('-')(1).toInt
//        val year  : Int=arrStr(1).split("T")(0).split('-')(0).toInt
//        val  rtd  : RealTimeData = new RealTimeData(station, time,temperature,day,  month, year)
//        rtd
//      }
      //Source.fromURL(s"http://localhost:8090/csv/monthly/${channel}/$station/$year/$month").getLines().toList
      list22
    }
    catch {
      //case ex : Exception => logger.info(s"$year")
      case _ => List()
    }

  }

  /***
    * TODO - implement !!!
    * @param stationId
    * @param month
    * @param year
    * @param channel
    * @return a list of RealTimeData objects, representing observations from
    *         all days of the given month in the given year
    */
  def findByStationAndMonthYear(stationId : String, month : Int, year : Int, channel : String = "TD") : List[RealTimeData] = {
    // temporary implementation with hard coded results
    //getDataFromIms()
    readFromIms(stationId, month, year, channel)
    // Implement this method correctly!
    // (hint: call readFromIms() and then operate on the data you got)
     //List()
  }
  

}