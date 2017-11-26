package com.antSmash.admin.service

import com.antSmash.admin.model.RealTimeData
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RealTimeTemperature {

  private val logger: Logger = LoggerFactory.getLogger(classOf[RealTimeTemperature])


  @Autowired
  val rtService : RTDataService = null ;

  
  /**
   * Implement this service correctly!
   * [hint: call rtService.findByStationAndMonthYear()]
   */
  def temperatureForOneDay2(stationId : String, day : String, month : String, year : String, channel : String = "TD"): String = {
    val results =
        "['00:10', 21.0]," +
        "['00:20', 21.4]," +
        "['00:30', 22.0]," +
        "['00:40', 22.5]," +
        "['00:50', 23.0]," +
        "['01:00', 24.0]" 
        // ...
        //"]"


    "[ ['Time', 'Temperature']," + results + "]"
    // THE RESULT OF THIS SERVICE IS A STRING THAT looks like this:
//    "[ ['Time', 'Temperature']," +
//    "['00:10', 21.0]," +
//    "['00:20', 21.4]," +
//    "['00:30', 22.0]," +
//    "['00:40', 22.5]," +
//    "['00:50', -23.0]," +
//    "['01:00', 24.0]" +
//    // ...
//    "]"
  }

  /**
    * Implement this service correctly!
    * [hint: call rtService.findByStationAndMonthYear()]
    *
    */
  def temperatureForOneDay(stationId : String, day : String, month : String, year : String, channel : String = "TD"): String = {
    val resultsMn : List[RealTimeData] =  rtService.findByStationAndMonthYear(stationId, month.toInt, year.toInt, channel)
    val resultDay  : List[RealTimeData] = resultsMn
      .filter(rtd  => rtd.day.toString.equals(day))

    val resultSorted  : List[RealTimeData] = resultDay
            .sortBy(rtd => rtd.time)

    val r2: List[String]= resultSorted.
      map( x   =>
                  "['" +x.time+"', "+x.temperature+"]"
    )
    val r3:String = r2.mkString(",")


    "[ ['Time', 'Temperature']," + r3 + "]"
    // THE RESULT OF THIS SERVICE IS A STRING THAT looks like this:
    //    "[ ['Time', 'Temperature']," +
    //    "['00:10', 21.0]," +
    //    "['00:20', 21.4]," +
    //    "['00:30', 22.0]," +
    //    "['00:40', 22.5]," +
    //    "['00:50', -23.0]," +
    //    "['01:00', 24.0]" +
    //    // ...
    //    "]"
  }


  /**
    * Implement this service correctly!
    */
  def minMaxTemperature(stationId : String, month : String, year : String): String = {

    val monthYear : List[RealTimeData] =  rtService.findByStationAndMonthYear(stationId, month.toInt, year.toInt)
    val strList: Seq[String] =
    for (day <-  1 to 30) yield {

      val day1: List[RealTimeData] = monthYear.filter(x => x.day == day)
      val temp = day1.map(x => x.temperature.toDouble)
      val max = temp.max
      val min = temp.min

      val s: String = "['" + day + '-' + month + '-' + year + "', " + max + "," + min + "]"
      s
    }
//    val xx = List(
//        "['01-06-2016',35.0,24.0]",
//        "['02-06-2016',34.0,23.0]",
//        "['03-06-2016',35.0,24.0]",
//        "['04-06-2016',34.0,23.0]"
//        )
    logger.info(strList.mkString(","))
    "[ ['Date', 'Max Temperature', 'Min Temperature']," + strList.mkString(",") + "]"

  }
  

  

}