package vn.edu.tdc.currencyconvert.models

import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.currencyconvert.network.ApiService
import java.io.IOException

class CurrencyModel {

    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    lateinit var timeCurrent: String

    // Get Symbols
    suspend fun getSymbols(): List<SymbolData> {
        return try {
            val response = apiService.getSymbols()
            response.symbols.map { SymbolData(it.key, it.value) }
        } catch (e: IOException) {
            // Handle network error
            throw Exception("Network error: ${e.message}")
        } catch (e: HttpException) {
            // Handle HTTP error
            throw Exception("HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            // Handle other errors
            throw Exception("Unknown error: ${e.message}")
        }
    }

    // Get Rates
    suspend fun getRates(): Pair<List<RateData>, String> {
        return try {
            val rateDataResponse = apiService.getRates()
            val rateList = rateDataResponse.rates.map { RateData(it.key, it.value) }
            timeCurrent = rateDataResponse.timestamp
            Pair(rateList, rateDataResponse.base)
        } catch (e: IOException) {
            // Handle network error
            throw Exception("Network error: ${e.message}")
        } catch (e: HttpException) {
            // Handle HTTP error
            throw Exception("HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            // Handle other errors
            throw Exception("Unknown error: ${e.message}")
        }
    }
}
