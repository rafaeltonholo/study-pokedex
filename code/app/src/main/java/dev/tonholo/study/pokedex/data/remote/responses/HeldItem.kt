package dev.tonholo.study.pokedex.data.remote.responses


import com.google.gson.annotations.SerializedName

data class HeldItem(
    val item: Item,
    @SerializedName("version_details")
    val versionDetails: List<VersionDetail>,
)
