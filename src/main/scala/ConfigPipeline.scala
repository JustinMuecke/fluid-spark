
import java.io.File

import database._
import input.{NTripleParser, RDFGraphParser}
import org.apache.spark.graphx.PartitionStrategy.RandomVertexCut
import org.apache.spark.{SparkConf, SparkContext}
import schema.SchemaElement

import scala.collection.mutable

class ConfigPipeline(config: MyConfig) {

  //    create tmp directory
  val logDir: File = new File(config.getString(config.VARS.spark_log_dir))
  if (!logDir.exists())
    logDir.mkdirs()

  var maxMemory = "1g"
  if (config.getString(config.VARS.spark_memory) != null)
    maxMemory = config.getString(config.VARS.spark_memory)

  var maxCores = "4"
  if (config.getString(config.VARS.spark_cores) != null)
    maxCores = config.getString(config.VARS.spark_cores)
  //delete output directory
  val conf = new SparkConf().setAppName(config.getString(config.VARS.spark_name)).
    setMaster(config.getString(config.VARS.spark_master)).
    set("spark.eventLog.enabled", "true").
    set("spark.eventLog.dir", config.getString(config.VARS.spark_log_dir)).
    set("spark.driver.memory", maxMemory).
    set("spark.executor.memory", maxMemory).
    set("spark.driver.maxResultSize", "0").
    set("spark.core.max", maxCores).
    set("spark.executor.core", maxCores)


  val inputFiles: java.util.List[String] = config.getStringList(config.VARS.input_filename)
  Constants.TYPE = config.getString(config.VARS.input_graphLabel)

  if (config.getString(config.VARS.input_namespace) != null)
    NTripleParser.baseURI = config.getString(config.VARS.input_namespace)

  if (config.getString(config.VARS.input_defaultSource) != null)
    NTripleParser.defaultSource = config.getString(config.VARS.input_defaultSource)


  //OUT
  OrientDbOptwithMem.URL = config.getString(config.VARS.db_url)
  OrientDbOptwithMem.USERNAME = config.getString(config.VARS.db_user)
  OrientDbOptwithMem.PASSWORD = config.getString(config.VARS.db_password)

  val database = config.getString(config.VARS.db_name)
  val trackChanges = config.getBoolean(config.VARS.igsi_trackChanges)

  var logChangesDir: String = null
  if (trackChanges == true) {
    logChangesDir = config.getString(config.VARS.igsi_logChangesDir)
    val file: File = new File(logChangesDir)
    if (!file.exists) file.mkdirs
  }

  val minWait = config.getLong(config.VARS.igsi_minWait)
  val minPartitions = config.getInt(config.VARS.spark_partitions);

  def start(): ChangeTracker = {
    var iteration = 0
    val iterator: java.util.Iterator[String] = inputFiles.iterator()

    val secondaryIndexFile = "secondaryIndex.ser.gz"
    while (iterator.hasNext) {
      if (iteration == 0)
        OrientDbOptwithMem.create(database, config.getBoolean(config.VARS.igsi_clearRepo))
      else
        OrientDbOptwithMem.getInstance(database, trackChanges).open()

      if (iteration > 0 && trackChanges)
        ChangeTracker.getInstance().resetScores()


      if (iteration == 0)
        SecondaryIndexMem.init(trackChanges, secondaryIndexFile, false)
      //      else
      //        SecondaryIndexMem.init(trackChanges, secondaryIndexFile, true);

      if (minWait > 0) {
        println("waiting for " + minWait + " ms")
        Thread.sleep(minWait)
        println("...continuing!")
      }
      val startTime = System.currentTimeMillis();
      if (minWait > 0) {
        println("waiting for " + minWait + " ms")
        Thread.sleep(minWait)
        println("...continuing!")
      }

      val sc = new SparkContext(conf)
      val igsi = new IGSI(database, trackChanges)

      val inputFile = iterator.next()


      //parse n-triple file to RDD of GraphX Edges

      val edges = sc.textFile(inputFile).filter(line => !line.trim.isEmpty).map(line => NTripleParser.parse(line))
      //build graph from vertices and edges from edges

      val graph = RDFGraphParser.parse(edges)
      val partionedgraph = graph.partitionBy(RandomVertexCut, minPartitions);
      //      println(s"Nodes: ${partionedgraph.vertices.count()}")

      val schemaExtraction = config.INDEX_MODELS.get(config.getString(config.VARS.schema_indexModel))
      //Schema Summarization:
      val schemaElements = partionedgraph.aggregateMessages[(Int, mutable.HashSet[SchemaElement])](
        triplet => schemaExtraction.sendMessage(triplet),
        (a, b) => schemaExtraction.mergeMessage(a, b))

      //merge all instances with same schema
      val aggregatedSchemaElements = schemaElements.values.reduceByKey(_ ++ _)
      //      println(s"Schema Elements: ${aggregatedSchemaElements.size}")

      //  (incremental) writing
      aggregatedSchemaElements.values.foreach(tuple => igsi.tryAddOptimized(tuple))

      OrientDbOptwithMem.getInstance(database, trackChanges).removeOldImprintsAndElements(startTime)


      sc.stop
      if (trackChanges) {
        ChangeTracker.getInstance().exportToCSV(logChangesDir + "/changes.csv", iteration)
        //export(logChangesDir + "/performance.csv", iteration)
      }
      SecondaryIndexMem.getInstance().persist();
      OrientDbOptwithMem.getInstance(database, trackChanges).close()
      //      OrientDbOptwithMem.removeInstance(database)
      println(s"Iteration ${iteration}")
      //      print(SecondaryIndexMem.getInstance().toString)
      iteration += 1
    }

    ChangeTracker.getInstance()
  }

  //  def export(filename: String, iteration: Int): Unit = {
  //    val file = new File(filename)
  //    val delimiter = ';'
  //
  //    val header = Array[String]("Iteration", "timeLoadingData (ms)", "timeParsingData (ms)", "timeSummarizeData (ms)",
  //      "timeAggregateSummaries (ms)", "timeWriteSummaries (ms)", "timeDeleteSummaries (ms)", "totalTimeUpdate (ms)", "totalTime (ms)")
  //
  //    val writer = new BufferedWriter(new FileWriter(file, iteration > 0))
  //    if (iteration <= 0) { //write headers
  //      var i = 0
  //      while (i < header.length - 1) {
  //        writer.write(header(i) + delimiter)
  //        i += 1
  //      }
  //      writer.write(header(header.length - 1))
  //      writer.newLine()
  //    }
  //    val contentLine: String = iteration.toString + delimiter + timeLoadingData.toString + delimiter +
  //      timeParsingData.toString + delimiter + timeSummarizeData.toString + delimiter + timeAggregateSummaries.toString + delimiter +
  //      timeWriteSummaries.toString + delimiter + timeDeleteSummaries.toString + delimiter +
  //      (timeWriteSummaries + timeDeleteSummaries).toString + delimiter +
  //      (timeLoadingData + timeParsingData + timeSummarizeData + timeAggregateSummaries + timeWriteSummaries + timeDeleteSummaries).toString
  //
  //    writer.write(contentLine)
  //    writer.newLine()
  //    writer.close()
  //  }
}


object Main {
  def main(args: Array[String]) {

    // this can be set into the JVM environment variables, you can easily find it on google
    if (args.isEmpty) {
      println("Need config file")
      return
    } else
      println("Conf:" + args(0))

    val pipeline: ConfigPipeline = new ConfigPipeline(new MyConfig(args(0)))

    //recommended to wait 1sec after timestamp since time is measured in seconds (not ms)
    pipeline.start()
  }
}
