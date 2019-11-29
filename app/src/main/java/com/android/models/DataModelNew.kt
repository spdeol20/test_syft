package com.android.models

data class DataModelNew(
    val incomplete_results: Boolean,
    val items: ArrayList<Item>?,
    val total_count: Int?
)