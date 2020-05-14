package com.example.androidkotlinassignment.source


import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinassignment.models.Fact
import com.example.androidkotlinassignment.models.FactCategory
import com.example.androidkotlinassignment.models.Model
import com.example.androidkotlinassignment.source.local.ILocalDataSource
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

class FakeLocalDataSource(
    override val factCategory: MutableLiveData<FactCategory>,
    override val allFacts: MutableLiveData<List<Fact>>
) : ILocalDataSource {

    @ExperimentalStdlibApi
    override suspend fun syncDB(data: Model.Result?): Boolean {
        if (data == null) {
            return false
        }
        val factCategoryId = UUID.randomUUID().toString()
        factCategory.value = FactCategory(UUID.randomUUID().toString(), data.title)
        val list = ArrayList<Fact>()
        for (row in data.rows) {
            if (row.title != null) {
                val fact = Fact(
                    generateHash(row.title!!),
                    row.title!!,
                    row.description,
                    row.imageHref,
                    factCategoryId
                )
            }
        }
        allFacts.value = list
        return true
    }

    @ExperimentalStdlibApi
    override fun generateHash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        val result = StringBuilder(bytes.size)
        bytes.forEach { byte ->
            val i = byte.toInt()
            result.append(HEX_CHARS[i shr 4 and 0xF])
            result.append(HEX_CHARS[i and 0XF])
        }

        return bytes.decodeToString()
    }

    companion object {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }

}