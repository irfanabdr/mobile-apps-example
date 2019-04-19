package com.irfan.bandungweather

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.irfan.bandungweather.fragments.ForecastFragment
import com.irfan.bandungweather.fragments.SettingsFragment
import com.irfan.bandungweather.fragments.CurrentFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    internal var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.current_weather)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CurrentFragment())
        adapter.addFragment(ForecastFragment())
        adapter.addFragment(SettingsFragment())
        view_pager.setAdapter(adapter)

        view_pager.addOnPageChangeListener(mOnPageChangeListener)
        view_pager.setOffscreenPageLimit(adapter.getCount())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onBackPressed() {
        if (view_pager.currentItem != 0) {
            view_pager.currentItem = 0
            return
        }

        super.onBackPressed()
    }

    private fun setActionBarTitle(position: Int) {
        when (position) {
            1 -> supportActionBar?.title = getString(R.string.weather_forecast)
            2 -> supportActionBar?.title = getString(R.string.title_settings)
            else -> supportActionBar?.title = getString(R.string.current_weather)
        }
    }

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            val menuItem = prevMenuItem
            if (menuItem != null) {
                menuItem.isChecked = false
            } else {
                navigation.menu.getItem(0).isChecked = false
            }

            navigation.menu.getItem(position).isChecked = true
            prevMenuItem = navigation.menu.getItem(position)
            setActionBarTitle(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_current -> {
                view_pager.currentItem = 0
                setActionBarTitle(view_pager.currentItem)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_forecast -> {
                view_pager.currentItem = 1
                setActionBarTitle(view_pager.currentItem)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                view_pager.currentItem = 2
                setActionBarTitle(view_pager.currentItem)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private inner class ViewPagerAdapter constructor(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val mFragmentList = ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

         fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }
    }
}
