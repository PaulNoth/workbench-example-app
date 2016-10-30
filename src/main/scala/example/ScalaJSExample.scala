package example
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.{CanvasRenderingContext2D, html}

import scala.scalajs.js

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val renderer = canvas.getContext("2d")
      .asInstanceOf[CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    val gradient = renderer
      .createLinearGradient(canvas.width / 2 - 100, 0, canvas.width / 2 + 100, 0)
    gradient.addColorStop(0, "red")
    gradient.addColorStop(0.5, "green")
    gradient.addColorStop(1, "blue")
    renderer.fillStyle = gradient

    renderer.textAlign = "center"
    renderer.textBaseline = "middle"

    def render() = {
      renderer.clearRect(0, 0, canvas.width, canvas.height)
      val date = new js.Date
      renderer.font = "75px sans-serif"

      val time = Seq(date.getHours(), date.getMinutes(), date.getSeconds()).mkString(":")
      renderer.fillText(time, canvas.width / 2, canvas.height / 2)
    }

    dom.setInterval(render _, 1000)
  }
}
