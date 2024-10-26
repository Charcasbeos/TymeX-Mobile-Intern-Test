package vn.edu.tdc.currencyconvert.network

import retrofit2.http.GET
import vn.edu.tdc.currencyconvert.models.RateResponse
import vn.edu.tdc.currencyconvert.models.SymbolResponse

interface ApiService {
    companion object {
         val BASE_URL = "https://api.exchangeratesapi.io/v1/"
    }
    @GET("symbols?access_key=981ff24f722453cd9ec1161d50508036")
    suspend fun getSymbols(): SymbolResponse
    @GET("latest?access_key=981ff24f722453cd9ec1161d50508036")
    suspend fun getRates(): RateResponse
}