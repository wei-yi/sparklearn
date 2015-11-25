
package com.weiyi.test

import scala.collection.mutable

object TestList {

  def t[B](a: B) = {
    println(a)

  }

  def main(args: Array[String]): Unit = {
    val list = mutable.LinkedList(1, 2)
    val list1 = mutable.LinkedList(1, 3)
    list.append(list1)
    println(list.get(1).get)
    println(list.take(2))
    println(list.+:(5))
    println(list.:+(6))
    println(list.++(list1))
    println(list)
    println(list)
    val name = Seq(10, 1, 1, 1)
    // 0 1 2 3 4 5
    val li = "10|200|"
    val sp = li.split("\\|")
    for (n <- (1 to sp.length - 3); if (n % 2 != 0)) {
      //b|a|a|b|a|b ==>5 5-3 ==2

      println("seq " + sp(n) + " " + sp(n + 1))
      println(sp(n) == sp(n + 1))
    }
    def aa(a: Int): String = "aaa"
    val tt = new Transform
    Transform[Int, String].transform(11) {
      a: Int ⇒ (a + "aa")
    }
    t(1)

    val testhash = new mutable.HashSet[Int]
    val map = new mutable.HashMap[Int,Int]
    val list8 = new mutable.LinkedList[Int]()
    val arry = new Array[Int](10)

    testhash += 1
    testhash += 2
    testhash += 4
    testhash += 5

    map.+=(1 → 2)
    map.+=(3 → 2)
    map.+=(2 → 2)
    list8.tail

    println(testhash)
    println(map)
  }
}


case class Transform[A,B] {
  def transform(a: A)(op: A ⇒ B): List[B] = {
    println(List(op(a)))
    List(op(a))
  }

}