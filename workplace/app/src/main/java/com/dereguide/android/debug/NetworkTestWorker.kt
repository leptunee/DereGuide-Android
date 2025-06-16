package com.dereguide.android.debug

import android.content.Context
import android.util.Log
import androidx.work.*
import com.dereguide.android.data.api.DereGuideApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 网络连接测试工作类
 * 用于调试 API 连接问题
 */
class NetworkTestWorker @Inject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters,
    private val apiService: DereGuideApiService
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "NetworkTestWorker"
        const val WORK_NAME = "network_test_work"
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "开始网络连接测试...")
            
            // 测试 API 信息端点
            val infoResponse = apiService.getApiInfo()
            if (infoResponse.isSuccessful) {
                val info = infoResponse.body()
                Log.d(TAG, "✓ API 信息获取成功: $info")
            } else {
                Log.e(TAG, "✗ API 信息获取失败: ${infoResponse.code()}")
            }
            
            // 测试卡片列表端点
            val cardsResponse = apiService.getAllCards()
            if (cardsResponse.isSuccessful) {
                val cards = cardsResponse.body()
                Log.d(TAG, "✓ 卡片列表获取成功，共 ${cards?.result?.size} 张卡片")
            } else {
                Log.e(TAG, "✗ 卡片列表获取失败: ${cardsResponse.code()}")
            }
            
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "网络测试出错", e)
            Result.failure()
        }
    }
}

/**
 * 网络测试工具类
 */
object NetworkTestUtil {
    
    fun startNetworkTest(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<NetworkTestWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                NetworkTestWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }
}
