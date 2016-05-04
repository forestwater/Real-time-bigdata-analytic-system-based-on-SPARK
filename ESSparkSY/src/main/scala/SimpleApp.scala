/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.elasticsearch.spark._ 
import org.elasticsearch.hadoop.mr.EsOutputFormat
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.apache.hadoop.mapred.{FileOutputCommitter, FileOutputFormat, JobConf, OutputFormat}
import org.apache.spark.streaming._

object SimpleApp {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Test elasticsearch.")//.setMaster(master)
    conf.set("es.index.auto.create", "true")
    val sc = new SparkContext(conf)

    val numbers = Map("one" -> 1, "two" -> 2, "three" -> 3)
    val airports = Map("arrival" -> "Otopeni", "SFO" -> "San Fran")
    
    

    val esNodes = "localhost"
    //val esNodes = "9.112.246.31"
    val esResource = "customer/yws"
    val jobConf = new JobConf(sc.hadoopConfiguration)
    jobConf.set("mapred.output.format.class", "org.elasticsearch.hadoop.mr.EsOutputFormat")
    jobConf.setOutputCommitter(classOf[FileOutputCommitter])
    jobConf.set(ConfigurationOptions.ES_RESOURCE, esResource)
    jobConf.set(ConfigurationOptions.ES_NODES, esNodes)
    
    sc.makeRDD(Seq(numbers, airports)).saveToEs(esResource)
        
    }
}    
