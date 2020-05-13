package ru.eosan.telposandbox.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.eosan.telposandbox.R
import ru.eosan.telposandbox.ui.main.ledScreen.LedFragment
import ru.eosan.telposandbox.ui.main.relayScreen.RelayFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_led,
    R.string.tab_relay
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> LedFragment()
            1 -> RelayFragment()
            else -> throw IllegalArgumentException("Illegal Tab with position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}