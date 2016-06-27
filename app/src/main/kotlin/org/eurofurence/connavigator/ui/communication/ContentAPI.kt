package org.eurofurence.connavigator.ui.communication

/**
 * Created by Pazuzu on 12.04.2016.
 */
interface ContentAPI {
    /**
     * Called when the data has been updated.
     */
    fun dataUpdated() {
        // No operation
    }

    /**
     * Called when the search button is clicked in the toolbar
     */
    fun onSearchButtonClick(){

    }
}