package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemCartProductBinding
import com.itis.delivery.presentation.model.CartProductModel
import com.itis.delivery.utils.toPrice

class CartProductItemHolder(
    private val binding: ItemCartProductBinding,
    private val onItemCartProductClick: (CartProductModel) -> Unit,
    private val onChosenCheck: (Long, Boolean) -> Unit,
    private val onIncreaseCountClick: (CartProductModel) -> Unit,
    private val onDecreaseCountClick: (CartProductModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: CartProductModel) {
        with(binding) {
            mtvProductName.text = item.productName
            mtvPrice.text = toPrice(item.price)
            mtvInCartCount.text = item.count.toString()
            root.setOnClickListener {
                onItemCartProductClick(item)
            }

            Glide.with(root)
                .load(item.productImageUrl)
                .error(R.drawable.no_image)
                .into(ivProduct)

            btnPlus.setOnClickListener {
                onIncreaseCountClick(item)
            }

            btnMinus.setOnClickListener {
                onDecreaseCountClick(item)
            }

            cbChosen.isChecked = item.isChosen

            cbChosen.setOnCheckedChangeListener { _, isChecked ->
                cbChosen.isChecked = isChecked
                onChosenCheck(item.productId, isChecked)
            }
        }
    }
}
