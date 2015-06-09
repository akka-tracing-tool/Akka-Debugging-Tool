package models

case class MessageRelation(id1: Int, id2: Int) {
  def toJsonString: String = "{\"id1\":" + id1 + ", \"id2\": " + id2 + "}"
}
