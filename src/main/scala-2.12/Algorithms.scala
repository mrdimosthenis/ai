import models.{Node, TrackNode}

import scala.annotation.tailrec
import scala.collection.immutable.Queue

object Algorithms {

  def search[A](startingNode: Node[A], isBreadthFirst: Boolean = true): Option[A] = {

    @tailrec
    def recur(coll: Seq[Node[A]]): Option[A] = {
      if (coll.isEmpty) None
      else {
        val currentNode = coll match {
          case _: List[Node[A]] => coll.head
          case _: Vector[Node[A]] => coll.last
        }
        if (currentNode.isSolution) Some(currentNode.content)
        else {
          val restNodes = coll match {
            case _: List[Node[A]] => coll.drop(1)
            case _: Vector[Node[A]] => coll.dropRight(1)
          }
          recur(restNodes ++ currentNode.neighbors)
        }
      }
    }

    val startingSeq = if (isBreadthFirst) List(startingNode) else Vector(startingNode)
    recur(startingSeq)

  }

  //

  def breadthFirstWithTracking[A, B](startingNode: TrackNode[A, B]): Option[Vector[B]] = {

    @tailrec
    def recur(queue: Queue[TrackNode[A, B]], trackSet: Set[A]): Option[Vector[B]] = {
      if (queue.isEmpty) None
      else {
        val currentNode = queue.dequeue._1
        if (currentNode.isSolution) Some(currentNode.path)
        else recur(queue.dequeue._2.enqueue(currentNode.neighbors(trackSet)), trackSet + currentNode.content)
      }
    }

    recur(Queue(startingNode), Set.empty[A])

  }

  def depthFirstWithTracking[A, B](startingNode: TrackNode[A, B]): Option[Vector[B]] = {

    @tailrec
    def recur(stack: List[TrackNode[A, B]], trackSet: Set[A]): Option[Vector[B]] = {
      if (stack.isEmpty) None
      else {
        val currentNode = stack.head
        if (currentNode.isSolution) Some(currentNode.path)
        else recur(currentNode.neighbors(trackSet).toList ++ stack.tail, trackSet + currentNode.content)
      }
    }

    recur(List(startingNode), Set.empty[A])

  }

}
