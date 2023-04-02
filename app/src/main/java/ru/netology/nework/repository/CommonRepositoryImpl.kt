package ru.netology.nework.repository

class CommonRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext
    private val context: Context,
) : CommonRepository {

    override suspend fun getAllUsers(): List<User> {
        try {
            val response = apiService.getAllUsers()
            if (!response.isSuccessful) {
                val errJson = response.errorBody()?.string()
                if (errJson.isNullOrBlank()) throw UnknownError
                throw getErrorResponse(errJson)
            }
            return response.body() ?: throw getErrorResponse()
        } catch (e: ErrorResponse) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getUserById(id: Long): User {
        try {
            val response = apiService.getUserById(id)
            if (!response.isSuccessful) {
                val errJson = response.errorBody()?.string()
                if (errJson.isNullOrBlank()) throw UnknownError
                throw getErrorResponse(errJson)
            }
            return response.body() ?: throw getErrorResponse()
        } catch (e: ErrorResponse) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    private fun getErrorResponse(errJson: String? = null): Throwable {
        if (errJson.isNullOrBlank()) return ErrorResponse(context.getString(R.string.error_empty_response))
        return gson.fromJson(
            errJson,
            ErrorResponse::class.java
        )
    }

}