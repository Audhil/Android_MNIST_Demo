package com.wordpress.smdaudhilbe.sirajmnistdemo.models

/**
 *
 * Created by mohammed-2284 on 08/10/17.
 */

class Classification internal constructor() {

    //conf is the output
    var conf: Float = 0.toFloat()
        private set
    //input label
    var label: String? = null
        private set

    init {
        this.conf = -1.0f
        this.label = null
    }

    internal fun update(conf: Float, label: String) {
        this.conf = conf
        this.label = label
    }
}