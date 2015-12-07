//package src.main.java
//
//import audit.lib.spark.ops.{IncomeSaveOps, OutgoSaveOps, CurrencyInOutSaveOps}
//import audit.{CurrencyInOutDAO, OutgoDAO, IncomeDAO}
//import org.apache.spark.SparkContext
//import org.apache.spark.sql.SQLContext
//import org.apache.spark.sql.types._
//import spray.json.DefaultJsonProtocol
//import util.TimeUtils
//
//case class Field(`type`: String, fieldName: String)
//
//case class Operation(kind: String, tableName: String, joinTmpTableName2Path: Option[Map[String,String]], cache: Boolean, sql: String)
//
//case class Table(tableName: String, path: String, fields: List[Field], delimiter: String)
//
//case class GameConfig(filePath: String, delimiter: String, fields: List[Field], tableAlias: String,
//                      operations: List[Operation], subscriber: Option[String] = None)
//
//object MyJsonProtocol extends DefaultJsonProtocol {
//  implicit val field = jsonFormat2(Field)
//  implicit val operation = jsonFormat5(Operation)
//  implicit val colorFormat = jsonFormat6(GameConfig)
//}
//
//case class Game(sc: SparkContext, conf: GameConfig) {
//  val  sqlContext = new SQLContext(sc)
//  val HIVE_INCOME_TABLE = "income"
//  val HIVE_OUTGO_TABLE  = "outgo"
//  val HIVE_CURRENCYINOUT_TABLE  = "currencyInOut"
//
//  def createStructField(fields: List[Field], result: List[StructField]): List[StructField] = {
//    if (fields.size != 0) {
//      createStructField(fields.tail, StructField(fields(0).fieldName, getStructType(fields(0).`type`), false) :: result)
//    } else {
//      result.reverse
//    }
//  }
//
//  val schema: StructType = new StructType(createStructField(conf.fields, List()).toArray)
//
//  def getStructType(`type`: String) = `type`.toLowerCase match {
//    case "int"     ⇒ IntegerType
//    case "string"  ⇒ StringType
//    case "long"    ⇒ LongType
//    case "float"   ⇒ FloatType
//    case "double"  ⇒ DoubleType
//    case "boolean" ⇒ BooleanType
//  }
//
//
//  val gameDF =sqlContext.read
//    .format("com.databricks.spark.csv")
//    .option("header", "false")
//    .option("delimiter", conf.delimiter)
//    .schema(schema)
//    .load(conf.filePath)
//
//  gameDF.registerTempTable(conf.tableAlias)
//  sqlContext.udf.register("toYM", TimeUtils.toYM _)
//
//  val operations = conf.operations
//
//  def createTableDFs() = {
//    if (operations.size > 1)
//      sqlContext.cacheTable(conf.tableAlias)
//    for (op ← operations) {
//      op.kind match {
//        case "join" ⇒ val joinTables = op.joinTmpTableName2Path
//          if (joinTables != None) {
//            op.joinTmpTableName2Path.get.foreach { case (name, path) ⇒
//              sqlContext.read.json(path).registerTempTable(name)
//              sqlContext.cacheTable(name)
//            }
//          }
//          sqlContext.sql(op.sql).registerTempTable(op.tableName)
//          if (op.cache)
//            sqlContext.cacheTable(op.tableName)
//        case "create" ⇒ val df = sqlContext.sql(op.sql)
//          df.registerTempTable(op.tableName)
//          if (op.cache)
//            sqlContext.cacheTable(op.tableName)
//          op.tableName.toLowerCase match {
//            case "income" ⇒ df.map(IncomeDAO.apply(_)).insertInto("income", HIVE_INCOME_TABLE)
//            case "outgo" ⇒ df.map(OutgoDAO.apply(_)).insertInto("outgo", HIVE_OUTGO_TABLE)
//            case "currencyinout" ⇒ df.map(CurrencyInOutDAO.apply(_)).insertInto("currencyinout", HIVE_CURRENCYINOUT_TABLE)
//            case _ ⇒ println("tableName not exist, case class not exist")
//          }
//      }
//    }
//    sqlContext.clearCache()
//  }
//
//}
//
