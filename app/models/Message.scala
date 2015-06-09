package models

case class Message(id: Int, sender: String, receiver: String) {
  def toJsonString: String = "{\"id\":" + id + ", \"sender\": \"" + sender + "\", \"receiver\": \"" + receiver + "\"}"
}
