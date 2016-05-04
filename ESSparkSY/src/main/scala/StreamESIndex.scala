import scala.collection.immutable.HashMap
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.elasticsearch.spark._ 
import org.elasticsearch.hadoop.mr.EsOutputFormat
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.apache.hadoop.mapred.{FileOutputCommitter, FileOutputFormat, JobConf, OutputFormat}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

/**
 * @author sunyan
 */
object StreamESIndex {
  private val checkpointDir = "popularity-data-checkpoint"
  private val msgConsumerGroup = "user-behavior-topic-message-consumer-group"

  def main(args: Array[String]) {

/*    val conf = new SparkConf().setAppName("Test elasticsearch.")//.setMaster(master)
    conf.set("es.index.auto.create", "true")
    val ssc = new StreamingContext(conf, Seconds(1))
    val inputDir = "/tmp/streamInputData"    
    val lines = ssc.textFileStream(inputDir)
    
*/
    
        val Array(zkServers,processingInterval) = args
    val conf = new SparkConf().setAppName("Web Page Popularity Value Calculator")
    val ssc = new StreamingContext(conf, Seconds(processingInterval.toInt))
    //using updateStateByKey asks for enabling checkpoint
    ssc.checkpoint(checkpointDir)
    val kafkaStream = KafkaUtils.createStream(
      //Spark streaming context
      ssc,
      //zookeeper quorum. e.g zkserver1:2181,zkserver2:2181,...
      zkServers,
      //kafka message consumer group ID
      msgConsumerGroup,
      //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
      Map("user-behavior-topic-1" -> 1))
    val msgDataRDD = kafkaStream.map(_._2)
    //val msgDataRDD = kafkaStream.map(s=> "popularity"->s)
    
    //val esNodes = "9.112.246.31"
    val esNodes = "localhost"
      val esResource = "customer/yws"
    msgDataRDD.foreachRDD(rdd=>{
      //rdd.con
      //rdd.map(s=>"popularity"->s).saveToEs(esResource)
      //rdd.map(s=> HashMap("popularity" -> s)).saveToEs(esResource)
      rdd.map(s=>("popularity", s)).saveToEs(esResource)
      //rdd.saveToEs(esResource)
      //rdd.saveAsTextFile("kafka")
    })
        ssc.start()
    ssc.awaitTermination()

    
/*    lines.foreachRDD({(line, time) => 
      val sc = line.context
      val jobConf = new JobConf(sc.hadoopConfiguration)
      jobConf.set("mapred.output.format.class", "org.elasticsearch.hadoop.mr.EsOutputFormat")
      jobConf.setOutputCommitter(classOf[FileOutputCommitter])
      jobConf.set(ConfigurationOptions.ES_RESOURCE, esResource)
      jobConf.set(ConfigurationOptions.ES_NODES, esNodes)
      line.saveToEs(esResource)
      
    })*/

    
  }
  
}