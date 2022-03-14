# ScalaForDataScience
This is a repository for helper functions for my Scala for data science labs.

## WeatherData

To open the weather data set, use the following code.

```
val data_folder = "data/"
val filename = "weather.csv"
val weather = WeatherData.load(data_folder, filename)
```
