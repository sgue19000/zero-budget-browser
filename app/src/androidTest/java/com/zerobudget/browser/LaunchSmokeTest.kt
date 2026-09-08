package com.zerobudget.browser

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zerobudget.browser.ui.BrowserActivity
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LaunchSmokeTest {
    @get:Rule val rule = ActivityScenarioRule(BrowserActivity::class.java)
    @Test fun launchesWithoutCrash() {
        rule.scenario.onActivity { activity -> assertNotNull(activity) }
    }
}
