import io.Source
import scala.reflect.ClassTag
import breeze.linalg._

object WeatherData {

    def load(dataDirectory:String = "./", fileName:String = "weather.csv"):WeatherData = 
    {
        val file = Source.fromFile(dataDirectory + fileName)
        val lines = file.getLines.toVector
        val splitLines = lines.map { _.split("\",\"") }

        def fromList[T:ClassTag](index:Int, converter:(String => T)):DenseVector[T] =
            DenseVector.tabulate(lines.size-1) { irow => converter(splitLines(irow+1)(index)) }

        val precipitation = fromList(0, elem => elem.replace("\"", "").toDouble)
        val month = fromList(2, elem => elem.replace("\"", "").toInt)
        val day = fromList(3, elem => elem.replace("\"", "").toInt)
        val year = fromList(4, elem => elem.replace("\"", "").toInt)
        val city = fromList(5, elem => elem.replace("\"", ""))
        val state = fromList(8, elem => elem.replace("\"", ""))
        val avgTemp = fromList(9, elem => elem.replace("\"", "").toInt)
        val maxTemp = fromList(10, elem => elem.replace("\"", "").toInt)
        val minTemp = fromList(11, elem => elem.replace("\"", "").toInt)
        val windDirection = fromList(12, elem => elem.replace("\"", "").toInt)
        val windSpeed = fromList(13, elem => elem.replace("\"", "").toDouble)

        new WeatherData(
            day, 
            month, 
            year, 
            city, 
            state, 
            precipitation, 
            avgTemp, 
            maxTemp, 
            minTemp, 
            windDirection, 
            windSpeed
        )
    }

    class WeatherData(
        val day:DenseVector[Int], 
        val month:DenseVector[Int], 
        val year:DenseVector[Int], 
        val city:DenseVector[String], 
        val state:DenseVector[String], 
        val precipitation:DenseVector[Double],
        val avgTemp:DenseVector[Int],
        val maxTemp:DenseVector[Int],
        val minTemp:DenseVector[Int],
        val windDirection:DenseVector[Int],
        val windSpeed:DenseVector[Double]
    ) {
        lazy val numericFeatureMatrix:DenseMatrix[Double] = 
        DenseMatrix.horzcat(
            precipitation.toDenseMatrix.t,
            avgTemp.mapValues(_.toDouble).toDenseMatrix.t,
            maxTemp.mapValues(_.toDouble).toDenseMatrix.t,
            minTemp.mapValues(_.toDouble).toDenseMatrix.t,
            windDirection.mapValues(_.toDouble).toDenseMatrix.t,
            windSpeed.toDenseMatrix.t
        )
    }

}
