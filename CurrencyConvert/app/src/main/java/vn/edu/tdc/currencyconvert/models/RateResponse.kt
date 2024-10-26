package vn.edu.tdc.currencyconvert.models

import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("base")
    val base : String,
    @SerializedName("timestamp")
    val timestamp : String,
    @SerializedName("rates")
    val rates: Map<String, String>
)