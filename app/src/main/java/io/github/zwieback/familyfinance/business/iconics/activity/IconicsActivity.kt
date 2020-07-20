package io.github.zwieback.familyfinance.business.iconics.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.ITypeface
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.iconics.fragment.IconicsFragment
import io.github.zwieback.familyfinance.business.iconics.listener.OnIconSelectListener
import io.github.zwieback.familyfinance.constant.UiConstants
import io.github.zwieback.familyfinance.core.activity.ActivityWrapper
import io.github.zwieback.familyfinance.core.drawer.DrawerListener
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.random.Random

class IconicsActivity : ActivityWrapper(), OnIconSelectListener {

    private lateinit var fonts: List<ITypeface>
    private lateinit var drawer: Drawer
    private var currentSearch: String? = null

    override val titleStringId: Int
        get() = 0

    override val isDisplayHomeAsUpEnabled: Boolean
        get() = false

    private val registeredSortedFonts: List<ITypeface>
        get() = Iconics.getRegisteredFonts(this)
            .sortedWith(Comparator { typeface1, typeface2 ->
                typeface1.fontName.compareTo(typeface2.fontName)
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        fonts = registeredSortedFonts
        val items = buildDrawerItems(fonts)
        val identifierCmd = determineIdentifierOfCommunityMaterial(fonts)
        drawer = buildDrawer(toolbar, fonts, items, identifierCmd)
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_iconics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_entity_search, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchView(searchView: SearchView) {
        addDisposable(
            searchView.queryTextChanges()
                .debounce(
                    UiConstants.UI_DEBOUNCE_TIMEOUT,
                    TimeUnit.MILLISECONDS,
                    AndroidSchedulers.mainThread()
                )
                .subscribe { searchName ->
                    currentSearch = searchName.toString()
                    currentSearch?.let { currentSearch ->
                        fonts
                            .forEachIndexed { index, font ->
                                val foundCount = findMatchedIconCount(currentSearch, font)
                                drawer.updateBadge(index.toLong(), StringHolder("$foundCount"))
                            }
                    }
                    searchInFragment(currentSearch)
                }
        )
    }

    override fun onIconSelected(iconName: String) {
        val resultIntent = Intent()
            .putExtra(OUTPUT_ICON_NAME, iconName)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun findMatchedIconCount(searchName: String, font: ITypeface): Int {
        val searchInLowerCase = searchName.toLowerCase(Locale.getDefault())
        return font.icons
            .asSequence()
            .filter { icon -> icon.toLowerCase(Locale.getDefault()).contains(searchInLowerCase) }
            .count()
    }

    private fun buildDrawerItems(fonts: List<ITypeface>): List<IDrawerItem<*>> {
        val items = ArrayList<IDrawerItem<*>>(fonts.size)
        for ((index, font) in fonts.withIndex()) {
            val description = if (font.author.isEmpty()) {
                font.version
            } else {
                "${font.version} - ${font.author}"
            }
            val drawerItem = PrimaryDrawerItem()
                .withName(font.fontName)
                .withBadge(font.icons.size.toString())
                .withDescription(description)
                .withBadgeStyle(BadgeStyle().withColorRes(R.color.md_grey_200))
                .withIcon(extractRandomIcon(font))
                .withIdentifier(index.toLong())

            items.add(drawerItem)
        }
        return items
    }

    private fun extractRandomIcon(typeface: ITypeface): IIcon {
        val randomIndex = Random.Default.nextInt(typeface.icons.size)
        val randomIconName = typeface.icons
            .asSequence()
            .filterIndexed { index, _ -> index == randomIndex }
            .firstOrNull()
            ?: typeface.icons.iterator().next()
        return typeface.getIcon(randomIconName)
    }

    private fun determineIdentifierOfCommunityMaterial(fonts: List<ITypeface>): Int {
        val communityMaterialFontName = CommunityMaterial.fontName
        val communityMaterialIdentifier = fonts
            .indexOfFirst { font -> communityMaterialFontName == font.fontName }
        return max(communityMaterialIdentifier, 0)
    }

    private fun buildDrawer(
        toolbar: Toolbar,
        fonts: List<ITypeface>,
        items: List<IDrawerItem<*>>,
        selectedItemIdentifier: Int
    ): Drawer {
        return DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withDrawerItems(items)
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    val fontName = fonts[position].fontName
                    supportActionBar?.title = fontName
                    loadIcons(fontName)
                    return false
                }
            })
            .withOnDrawerListener(DrawerListener(this))
            .withFireOnInitialOnClick(true)
            .withSelectedItem(selectedItemIdentifier.toLong())
            .build()
    }

    private fun loadIcons(fontName: String) {
        val iconicsFragment = IconicsFragment.newInstance(fontName)
        iconicsFragment.onSearch(currentSearch)
        supportFragmentManager.beginTransaction()
            .replace(R.id.iconics_fragment, iconicsFragment)
            .commit()
    }

    private fun searchInFragment(searchName: String?) {
        findFragment()?.onSearch(searchName)
    }

    private fun findFragment(): IconicsFragment? {
        return supportFragmentManager.findFragmentById(R.id.iconics_fragment) as IconicsFragment?
    }

    companion object {
        const val OUTPUT_ICON_NAME = "iconName"
    }
}
