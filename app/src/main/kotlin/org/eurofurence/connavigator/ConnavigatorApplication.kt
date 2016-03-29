package org.eurofurence.connavigator

import android.app.Application
import android.os.StrictMode
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache
import io.swagger.client.ApiInvoker
import net.danlew.android.joda.JodaTimeAndroid
import org.eurofurence.connavigator.extensions.kb
import java.io.File

/**
 * Created by Pazuzu on 03.03.2016.
 */
class ConnavigatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODA
        JodaTimeAndroid.init(this)

        // Sets up the API invoker, uses a small cache and a few threads
        ApiInvoker.initializeInstance(DiskBasedCache(File(cacheDir, "volleycache.dat"), 5.kb), null, 2, null, 30);
    }
}