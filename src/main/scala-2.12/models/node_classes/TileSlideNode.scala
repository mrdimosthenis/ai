package models.node_classes

import models.IntTable
import models.direction.{Direction, Down, Left, Right, Up}
import models.node_traits.TrackNode

case class TileSlideNode(content: IndexedSeq[IndexedSeq[Int]], path: Vector[Direction] = Vector.empty[Direction])
  extends TrackNode[IndexedSeq[IndexedSeq[Int]], Direction]{

  val puzzleSize: Int = content.length

  override def neighbors(trackSet: Set[IndexedSeq[IndexedSeq[Int]]]): Vector[TileSlideNode] = {
    val intTable = IntTable(content)
    val (i, j) = intTable.indexesOf(0)
    Vector(Tuple3(i + 1, j, Up), Tuple3(i, j + 1, Left), Tuple3(i - 1, j, Down), Tuple3(i, j - 1, Right))
      .filter(t => t._1 >= 0 && t._1 < puzzleSize && t._2 >= 0 && t._2 < puzzleSize)
      .map(t => TileSlideNode(intTable.exchanged(i, j, t._1, t._2), path :+ t._3))
      .filter(n => !trackSet.contains(content))
  }

  override def isSolution: Boolean = IndexedSeq.tabulate(puzzleSize, puzzleSize)
  {(i, j) => if (i == puzzleSize - 1 && j == puzzleSize - 1) 0 else puzzleSize * i + j + 1} == content

}
