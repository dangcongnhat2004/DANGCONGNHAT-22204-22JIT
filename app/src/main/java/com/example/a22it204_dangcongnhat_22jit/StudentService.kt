package com.example.a22it204_dangcongnhat_22jit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentService {
    companion object{
        val retofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://172.20.10.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiUser: StudentService= retofit.create(StudentService::class.java)
    }

    @GET("student")
    fun getDataUser() : Call<List<Student>>
    @DELETE("student/{id}")
    fun deleteStudent(@Path("id") studentId: Int): Call<Void>
    @POST("student")
    fun addStudent(@Body student: Student): Call<Void> // Phương thức POST để thêm sinh viên mới
    @PUT("student/{id}")
    fun updateStudent(@Path("id") studentId: Long, @Body student: Student): Call<Void>
}