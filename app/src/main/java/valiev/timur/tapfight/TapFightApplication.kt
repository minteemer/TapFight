package valiev.timur.tapfight

import android.app.Application

class TapFightApplication : Application() {
    companion object {
        lateinit var instance: TapFightApplication
            private set
    }

    init {
        instance = this
    }
}