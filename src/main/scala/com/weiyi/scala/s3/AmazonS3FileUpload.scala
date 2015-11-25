package com.weiyi.scala.s3

import awscala._, s3._
object AmazonS3FileUpload {
  implicit val s3 = S3.at(Region.Beijing)

  def bucketsList: Seq[Bucket] = s3.buckets

  def createBucket(bucketName: String) = s3.createBucket(bucketName)

  def getOrCreateBucket(bucketName: String): Bucket ={
    s3.bucket(bucketName) match {
      case  Some(bucket) => bucket
      case  None => createBucket(bucketName)
    }
  }

  def deleteBucket(bucketName: String, forceEmpty: Boolean) = {
    s3.bucket(bucketName) match {
      case Some(bucket) =>
        if(forceEmpty) {
          bucketFilesList(bucketName).foreach(key ⇒ bucket.delete(key))
          s3.delete(bucket)
        }else {
          s3.delete(bucket)
        }
      case None ⇒ println("Bucket not exist")
    }
  }

  def bucketFilesList(bucketName: String): List[String] = {
    val summaries: Seq[S3ObjectSummary] = getOrCreateBucket(bucketName).objectSummaries
    summaries.foreach(k ⇒ println(k.getKey) )
    def recursion(list: List[S3ObjectSummary], result: List[String]): List[String] = {
      if (!list.isEmpty) {
        recursion(list.tail, list(0).getKey :: result)
      } else {
        result
      }
    }
    recursion(summaries.toList, List())
  }

  def fileList(s3Path: String) = {

  }

  def main(args: Array[String]): Unit = {

    val path = "/Users/yuanyi/bigdata/testData/user.txt"

    val path1 = "/Users/yuanyi/bigdata/testData/wordcount.jar"

    //bucketsList.foreach(println)

    val bucket = createBucket("movedatatest")
    //    val bucket = getOrCreateBucket("movedatatest")
    // bucket.put("wc.jar", new File(path1))
    // deleteBucket("movedatatest", true)
    bucketFilesList("movedatatest").foreach(println)
    //    bucket.get("wc.jar") match {
    //      case Some(file) ⇒ println(file.publicUrl)
    //    }
    // delete("movedatatest", false)
    println("-----------------------------------")
    //bucketsList.foreach(println)

    val  ss  = "s3://big-data/primitive/hsqj/xd.game.hsqj.props"
    println(ss.split("/").length)
  }

}
