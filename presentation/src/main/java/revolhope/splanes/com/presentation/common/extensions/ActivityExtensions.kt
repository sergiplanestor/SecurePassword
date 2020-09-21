package revolhope.splanes.com.presentation.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


fun FragmentActivity.addFragment(
    containerId: Int,
    fragment: Fragment
) {
    supportFragmentManager
        .beginTransaction()
        .apply {
            if (supportFragmentManager.fragments.isEmpty()) {
                add(containerId, fragment)
            } else {
                replace(containerId, fragment)
            }
        }
        .commitNowAllowingStateLoss()
}

