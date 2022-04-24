package database

import org.apache.spark.rdd.RDD
import schema.SchemaElement

import scala.collection.JavaConverters._

class IGSI(database: String, trackChanges: Boolean, trackExecutionTimes: Boolean) extends Serializable {

  def saveRDD(rdd: RDD[SchemaElement], map: Iterator[SchemaElement] => Iterator[Any], batch: Boolean,
              datasourcePayload: Boolean, maxCoresInt: Int, iteration : Int): Unit = {
    rdd.foreachPartition { p =>
      var tmpResult: Result[Boolean] = new Result[Boolean](trackExecutionTimes, trackChanges)
      var tmpPersist : Result[Boolean] = new Result[Boolean](trackExecutionTimes, trackChanges)
      if (p.nonEmpty) {
        val graphDatabase: OrientConnector = OrientConnector.getInstance(database, trackChanges, trackExecutionTimes, maxCoresInt)
        val persistDatabase : OrientConnector = OrientConnector.getInstance(database + "-" + iteration, trackChanges, trackExecutionTimes, maxCoresInt)
        tmpResult = graphDatabase.writeCollection(map(p).toList.asJava, batch, datasourcePayload).asInstanceOf[Result[Boolean]]
        tmpPersist = persistDatabase.writeCollection(map(p).toList.asJava, batch, datasourcePayload).asInstanceOf[Result[Boolean]]
        if (trackChanges) {
          Result.syncStaticMerge(tmpResult)
        }
      }
    }
  }


  def updateDelta(rdd: RDD[TripletWrapper], map: Iterator[TripletWrapper] => Iterator[Any], additions: Boolean,
                  datasourcePayload: Boolean, maxCoresInt: Int): Unit = {

    rdd.foreachPartition { p =>
      var tmpResult: Result[Boolean] = new Result[Boolean](trackExecutionTimes, trackChanges)
      if (p.nonEmpty) {
        val graphDatabase: OrientConnector = OrientConnector.getInstance(database, trackChanges, trackExecutionTimes, maxCoresInt)
        tmpResult = graphDatabase.updateCollection(map(p).toList.asJava, additions, datasourcePayload).asInstanceOf[Result[Boolean]]
        if (trackChanges) {
          Result.syncStaticMerge(tmpResult)
        }
      }
    }


  }
}
