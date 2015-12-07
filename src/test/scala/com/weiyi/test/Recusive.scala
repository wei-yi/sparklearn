object  Test extends App {
// 1 1 2 3 5
  def r2(p: Int, n: Int, count: Int): Int = {
  if (count > 3) {
    r2(n, p + n, count - 1)
  } else if(count == 3){
    p + n
  } else {
    return 1
  }
}
  println(r2(1,1,3))
println(r2(1,1,4))
}