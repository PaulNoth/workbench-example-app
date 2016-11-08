package example
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.{CanvasRenderingContext2D, MouseEvent, html}

import scala.util.Random

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val renderer = canvas.getContext("2d")
      .asInstanceOf[CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = 400

    renderer.font = "50px sans-serif"
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"

    val obstacleGap = 200
    val holeSize = 50
    val gravity = 0.2

    var playerY = canvas.height / 2.0
    var playerV = 0.0
    var dead = 0
    var frame = -50
    val obstacles = collection.mutable.Queue.empty[Int]

    def runLive() = {
      frame += 2
      if(frame >= 0 && frame % obstacleGap == 0) {
        obstacles.enqueue(Random.nextInt(canvas.height - 2 * holeSize) + holeSize)
      }
      if(obstacles.length > 7) {
        obstacles.dequeue()
        frame -= obstacleGap
      }

      playerY = playerY + playerV
      playerV = playerV + gravity

      renderer.fillStyle = "darkblue"

      for((holeY, i) <- obstacles.zipWithIndex) {
        val holeX = i * obstacleGap - frame + canvas.width
        renderer.fillRect(holeX, 0, 5, holeY - holeSize)
        renderer.fillRect(holeX, holeY + holeSize, 5, canvas.height - holeY - holeSize)

        if(math.abs(holeX - canvas.width / 2) < 5 && math.abs(holeY - playerY) > holeSize) {
          dead = 50
        }
      }

      renderer.fillStyle = "darkgreen"
      renderer.fillRect(canvas.width / 2 - 5, playerY - 5, 10, 10)
      if(playerY < 0 || playerY > canvas.height) {
        dead = 50
      }
    }

    def runDead() = {
      playerY = canvas.height / 2
      playerV = 0
      frame = -50
      dead -= 1
      obstacles.clear()
      renderer.fillStyle = "darkred"
      renderer.fillText("Game Over", canvas.width / 2, canvas.height / 2)
    }

    def run() = {
      renderer.clearRect(0, 0, canvas.width, canvas.height)
      if(dead > 0) {
        runDead()
      } else {
        runLive()
      }
    }

    dom.setInterval(run _, 20)

    canvas.onclick = (e: MouseEvent) => {
      playerV -= 5
    }
  }
}
