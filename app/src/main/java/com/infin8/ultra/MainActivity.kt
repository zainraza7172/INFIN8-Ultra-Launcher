package com.infin8.ultra

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher

data class LaunchApp(val label: String, val packageName: String, val icon: android.graphics.drawable.Drawable)

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 11, 18)
    private val panel = Color.rgb(18, 24, 36)
    private val accent = Color.rgb(119, 169, 255)
    private lateinit var grid: GridLayout
    private lateinit var search: EditText
    private lateinit var apps: List<LaunchApp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        apps = loadApps()
        buildUi()
        renderApps("")
    }

    private fun loadApps(): List<LaunchApp> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .mapNotNull {
                val info = it.activityInfo ?: return@mapNotNull null
                if (info.packageName == packageName) return@mapNotNull null
                LaunchApp(
                    it.loadLabel(packageManager).toString(),
                    info.packageName,
                    it.loadIcon(packageManager)
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    private fun buildUi() {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(12))
            setBackgroundColor(bg)
        }

        val brand = TextView(this).apply {
            text = "INFIN8"
            textSize = 13f
            letterSpacing = 0.28f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
        }
        outer.addView(brand)

        val title = TextView(this).apply {
            text = "Ultra"
            textSize = 38f
            setTextColor(Color.WHITE)
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setPadding(0, dp(2), 0, dp(2))
        }
        outer.addView(title)

        val subtitle = TextView(this).apply {
            text = "Your space. Your control."
            textSize = 13f
            setTextColor(Color.rgb(157, 169, 190))
            setPadding(0, 0, 0, dp(18))
        }
        outer.addView(subtitle)

        search = EditText(this).apply {
            hint = "Search apps"
            setHintTextColor(Color.rgb(139, 151, 171))
            setTextColor(Color.WHITE)
            textSize = 15f
            singleLine = true
            setPadding(dp(16), 0, dp(16), 0)
            background = rounded(panel, 18)
        }
        outer.addView(search, LinearLayout.LayoutParams(-1, dp(52)))

        val label = TextView(this).apply {
            text = "YOUR APPS"
            textSize = 11f
            letterSpacing = 0.16f
            setTextColor(Color.rgb(139, 151, 171))
            typeface = Typeface.DEFAULT_BOLD
            setPadding(2, dp(22), 0, dp(12))
        }
        outer.addView(label)

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        grid = GridLayout(this).apply {
            columnCount = 4
            alignmentMode = GridLayout.ALIGN_BOUNDS
            useDefaultMargins = false
        }
        scroll.addView(grid)
        outer.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(outer)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                renderApps(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun renderApps(query: String) {
        grid.removeAllViews()
        val filtered = apps.filter { it.label.contains(query, ignoreCase = true) }
        filtered.forEach { app ->
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(3), dp(8), dp(3), dp(12))
                isClickable = true
                isFocusable = true
                setOnClickListener { launch(app.packageName) }
            }
            val icon = ImageView(this).apply {
                setImageDrawable(app.icon)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            cell.addView(icon, LinearLayout.LayoutParams(dp(48), dp(48)))
            val name = TextView(this).apply {
                text = app.label
                textSize = 11f
                maxLines = 1
                gravity = Gravity.CENTER
                setTextColor(Color.rgb(235, 239, 248))
                setPadding(0, dp(7), 0, 0)
            }
            cell.addView(name, LinearLayout.LayoutParams(-1, -2))
            val lp = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(dp(2), 0, dp(2), 0)
            }
            grid.addView(cell, lp)
        }
        if (filtered.isEmpty()) {
            val empty = TextView(this).apply {
                text = "No apps found"
                textSize = 14f
                setTextColor(Color.LTGRAY)
                setPadding(0, dp(20), 0, 0)
            }
            grid.addView(empty)
        }
    }

    private fun launch(packageName: String) {
        packageManager.getLaunchIntentForPackage(packageName)?.let { startActivity(it) }
    }

    private fun rounded(color: Int, radiusDp: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radiusDp).toFloat()
        setStroke(dp(1), Color.rgb(34, 44, 62))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
