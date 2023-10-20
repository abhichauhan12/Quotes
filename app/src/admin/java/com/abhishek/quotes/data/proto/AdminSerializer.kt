package com.abhishek.quotes.data.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.abhishek.quotes.Admin
import java.io.InputStream
import java.io.OutputStream


object AdminSerializer : Serializer<Admin> {
    override val defaultValue: Admin
        get() = Admin.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Admin {
        return Admin.parseFrom(input)
    }

    override suspend fun writeTo(admin: Admin, output: OutputStream) {
        admin.writeTo(output)
    }
}

val Context.adminDataStore : DataStore<Admin> by dataStore(
    fileName = "adminInfo.pb",
    serializer = AdminSerializer,
)