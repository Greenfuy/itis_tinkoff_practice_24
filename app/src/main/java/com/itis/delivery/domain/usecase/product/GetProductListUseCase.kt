package com.itis.delivery.domain.usecase.product

import com.itis.delivery.domain.mapper.ProductUiModelMapper
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: ProductUiModelMapper,
) {
    suspend operator fun invoke(page: Int) : List<ProductUiModel> {
        return withContext(dispatcher) {
            mapper.mapDomainModelListToUiModelList(productRepository.getProducts(page))
        }
    }
}
