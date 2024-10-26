package vn.edu.tdc.currencyconvert.models

import com.google.gson.annotations.SerializedName

data class SymbolResponse(
//    @SerializedName("symbols")
    val symbols: Map<String, String>

)
