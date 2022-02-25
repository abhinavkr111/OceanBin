package com.nipun.oceanbin.feature_oceanbin.feature_home.data.remote.dto

import com.nipun.oceanbin.feature_oceanbin.feature_home.local.models.HourlyModel

data class HourlyContent(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Int,
    val sys: SysX,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
){
    fun toHourlyModel(): HourlyModel {
        return HourlyModel(
            iconId = "https://openweathermap.org/img/wn/${weather[0].icon}@2x.png",
            temp = (main.feels_like-273.15).toInt(),
            time = dt_txt.substring(11,16)
        )
    }
}