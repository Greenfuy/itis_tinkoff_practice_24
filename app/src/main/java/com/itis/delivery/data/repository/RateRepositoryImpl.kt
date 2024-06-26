package com.itis.delivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.base.Keys.PRODUCT_ID
import com.itis.delivery.base.Keys.RATE
import com.itis.delivery.base.Keys.RATES_COLLECTION_KEY
import com.itis.delivery.base.Keys.USER_ID
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.domain.model.RateModel
import com.itis.delivery.domain.repository.RateRepository
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository
): RateRepository {

    override suspend fun getRateAvgByProductId(productId: Long): Double {
        db.collection(RATES_COLLECTION_KEY)
            .whereEqualTo("productId", productId)
            .get()
            .await()
            .let { it ->
                if (it.documents.isEmpty()) return 0.0
                return it.documents
                    .map { it.get("rate").toString().toDouble() }
                    .average()
            }
    }

    override suspend fun addRate(productId: Long, rate: Int) : Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId != null) {
            val id = UUID.randomUUID().toString()
            db.collection(RATES_COLLECTION_KEY)
                .document(id)
                .set(RateModel(id, productId, userId, rate))
                .await()
            return true
        } else {
            throw UserNotAuthorizedException("User not authorized")
        }
    }

    override suspend fun isProductRated(productId: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId != null) {
            val documents = db.collection(RATES_COLLECTION_KEY)
                .whereEqualTo(PRODUCT_ID, productId)
                .whereEqualTo(USER_ID, userId)
                .get()
                .await()
                .documents
            return documents.isNotEmpty()
        } else {
            throw UserNotAuthorizedException("User not authorized")
        }
    }

    override suspend fun getProductIndicesByRate(rate: Int): List<Long> {
       val documents = db.collection(RATES_COLLECTION_KEY)
           .whereGreaterThanOrEqualTo(RATE, rate)
           .get()
           .await()
           .documents
        return if (documents.isNotEmpty()) documents.map {it.getLong(PRODUCT_ID)!!} else emptyList()
    }
}