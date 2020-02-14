package database

import org.apache.spark.rdd.RDD
import schema.SchemaElement

import scala.collection.JavaConverters._

class IGSI(database: String, trackChanges: Boolean, trackExecutionTimes: Boolean) extends Serializable {

  def saveRDD(rdd: RDD[SchemaElement], map: Iterator[SchemaElement] => Iterator[Any], batch: Boolean): Unit = {
    rdd.foreachPartition { p =>
      var tmpResult: Result[Boolean] = new Result[Boolean](trackExecutionTimes, trackChanges)
      if (p.nonEmpty) {
        val graphDatabase: OrientConnector = OrientConnector.getInstance(database, trackChanges, trackExecutionTimes)
        tmpResult = graphDatabase.writeCollection(map(p).toList.asJava, batch).asInstanceOf[Result[Boolean]]
        if (trackChanges) {
          Result.syncStaticMerge(tmpResult)
        }
      }
    }
  }


  def updateDelta(rdd: RDD[TripletWrapper], map: Iterator[TripletWrapper] => Iterator[Any], additions: Boolean): Unit = {

    rdd.foreachPartition { p =>
      var tmpResult: Result[Boolean] = new Result[Boolean](trackExecutionTimes, trackChanges)
      if (p.nonEmpty) {
        val graphDatabase: OrientConnector = OrientConnector.getInstance(database, trackChanges, trackExecutionTimes)
        tmpResult = graphDatabase.updateCollection(map(p).toList.asJava, additions).asInstanceOf[Result[Boolean]]
        if (trackChanges) {
          Result.syncStaticMerge(tmpResult)
        }
      }
    }


  }
}
