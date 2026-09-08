package com.zerobudget.browser.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationControllerTest {
    @Test fun webHistoryTakesPriority() {
        assertTrue(NavigationController.shouldGoBackInWeb(true))
        assertFalse(NavigationController.shouldGoBackInWeb(false))
    }
}
