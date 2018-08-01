package org.eurofurence.connavigator.util.v2

import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.then
import nl.komponents.kovenant.unwrap

/**
 * [then] with an immediate [unwrap].
 */
infix fun <V, R> Promise<V, Exception>.thenNested(bind: (V) -> Promise<R, Exception>) =
        this.then(bind).unwrap()