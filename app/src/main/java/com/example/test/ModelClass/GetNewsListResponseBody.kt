package com.example.test.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

    class GetNewsListResponseBody {
        @SerializedName("count")
        @Expose
        var count: Int? = null

        @SerializedName("next")
        @Expose
        var next: String? = null

        @SerializedName("previous")
        @Expose
        var previous: Any? = null

        @SerializedName("results")
        @Expose
        var results: List<Result>? = null

        class Result {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("url")
            @Expose
            var url: String? = null

            @SerializedName("image_url")
            @Expose
            var imageUrl: String? = null

            @SerializedName("news_site")
            @Expose
            var newsSite: String? = null

            @SerializedName("summary")
            @Expose
            var summary: String? = null

            @SerializedName("published_at")
            @Expose
            var publishedAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("featured")
            @Expose
            var featured: Boolean? = null

            @SerializedName("launches")
            @Expose
            var launches: List<Launch>? = null

            @SerializedName("events")
            @Expose
            var events: List<Any>? = null


            class Launch {
                @SerializedName("launch_id")
                @Expose
                var launchId: String? = null

                @SerializedName("provider")
                @Expose
                var provider: String? = null
            }
        }
    }
