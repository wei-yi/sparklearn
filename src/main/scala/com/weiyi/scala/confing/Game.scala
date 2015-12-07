package com.weiyi.scala.confing

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import spray.json.DefaultJsonProtocol

case class Field(`type`: String, fieldName: String)

case class Operation(opKind: String, inputTables: Option[List[Table]], resultTable: String, cache: Boolean, sql: String)

case class Table(kind: String, tableName: String, path: String, fields: Option[List[Field]], delimiter: Option[String], cache: Boolean)

case class GameConfig(operations: List[Operation], subscriber: Option[String] = None)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val field = jsonFormat2(Field)
  implicit val table = jsonFormat6(Table)
  implicit val operation = jsonFormat5(Operation)
  implicit val colorFormat = jsonFormat2(GameConfig)
}

case class Game(sc: SparkContext, conf: GameConfig) {
  val sqlContext = new SQLContext(sc)

  def createStructField(fields: List[Field], result: List[StructField]): List[StructField] = {
    if (fields.size != 0) {
      createStructField(fields.tail, StructField(fields(0).fieldName, getStructType(fields(0).`type`), false) :: result)
    } else {
      result.reverse
    }
  }


  def getStructType(`type`: String) = `type`.toLowerCase match {
    case "int"     ⇒ IntegerType
    case "string"  ⇒ StringType
    case "long"    ⇒ LongType
    case "float"   ⇒ FloatType
    case "double"  ⇒ DoubleType
    case "boolean" ⇒ BooleanType
  }

  sqlContext.udf.register("toYM", TimeUtils.toYM _)

  val operations = conf.operations

  def createTableDFs() = {
    for (op ← operations) {
       val inputTables = op.inputTables  //init all tables
      if(inputTables != None) {
        inputTables.get.foreach{
          case table ⇒ table.kind match {
            case "json" ⇒ sqlContext.read.json(table.path).registerTempTable(table.tableName)
            case "csv" ⇒  // csv must have fields, delimiter
              val schema: StructType = new StructType(createStructField(table.fields.get, List()).toArray)
              val gameDF = sqlContext
                .read.format("com.databricks.spark.csv")
                .option("header", "false")
                .option("delimiter", table.delimiter.get)
                .schema(schema)
                .load(table.path)
              gameDF.registerTempTable(table.tableName)
          }
            if(table.cache) sqlContext.cacheTable(table.tableName)
        }
      }
      val opDF = sqlContext.sql(op.sql)
        opDF.registerTempTable(op.resultTable)
      op.opKind.toLowerCase() match {
        case "qinsert" ⇒ op.resultTable match {
          case "income" ⇒ opDF.show() //.map()
          case "outgo" ⇒ opDF.show() //.map()
          case "currencyinout" ⇒ opDF.show()  //.map()
          case  _ ⇒ println("dot support this table:" + op.resultTable )
        }
        case "qjoin" ⇒ opDF.show()
        case "query" ⇒ opDF.show()
      }
    }
    sqlContext.clearCache()
  }
}