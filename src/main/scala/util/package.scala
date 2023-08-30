package object util {

  def line(length: Int): String =
    "\u2500" * length

  val line5  = line(5)
  val line10 = line(10)
  val line80 = line(80)

  final implicit class StringExtensions(private val str: String) extends AnyVal {

    @inline def color(escape: String): String = s"$escape$str${Console.RESET}"
    @inline def green: String                 = color(Console.GREEN)
    @inline def red: String                   = color(Console.RED)
    @inline def blue: String                  = color(Console.BLUE)
    @inline def yellow: String                = color(Console.YELLOW)
    @inline def magenta: String               = color(Console.MAGENTA)
    @inline def cyan: String                  = color(Console.CYAN)
  }
}
