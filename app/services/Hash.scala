package services

import com.roundeights.hasher.{ Hasher, Algo }
import scala.language.postfixOps

class Hash {
  def stringToSHA512(password: String): String = Algo.sha512(password).hex
}