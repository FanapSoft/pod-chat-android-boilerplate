package fanap.pod.chat.boilerplateapp

import android.app.Application
import fanap.pod.chat.boilerplateapp.di.Factory

class App :Application() {
    companion object {
        var factory: Factory = Factory()
    }

}