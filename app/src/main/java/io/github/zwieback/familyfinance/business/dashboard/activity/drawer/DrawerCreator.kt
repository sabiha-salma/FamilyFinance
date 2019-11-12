package io.github.zwieback.familyfinance.business.dashboard.activity.drawer

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.app.info.DeveloperInfo
import io.github.zwieback.familyfinance.business.article.activity.ExpenseArticleActivity
import io.github.zwieback.familyfinance.business.article.activity.IncomeArticleActivity
import io.github.zwieback.familyfinance.business.backup.activity.BackupActivity
import io.github.zwieback.familyfinance.business.currency.activity.CurrencyActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.EXCHANGE_RATE_CODE
import io.github.zwieback.familyfinance.business.exchange_rate.activity.ExchangeRateActivity
import io.github.zwieback.familyfinance.business.exchange_rate.filter.ExchangeRateFilter.Companion.EXCHANGE_RATE_FILTER
import io.github.zwieback.familyfinance.business.person.activity.PersonActivity
import io.github.zwieback.familyfinance.business.preference.activity.SettingsActivity
import io.github.zwieback.familyfinance.core.activity.EntityActivity.Companion.INPUT_READ_ONLY
import io.github.zwieback.familyfinance.core.drawer.DrawerListener
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.util.EmailUtils

class DrawerCreator(private val activity: DashboardActivity) : Drawer.OnDrawerItemClickListener {

    fun createDrawer(toolbar: Toolbar) {
        DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggleAnimated(true)
            .withSelectedItem(NO_SELECTED_ITEM.toLong())
            .withDrawerItems(createItems())
            .withOnDrawerItemClickListener(this)
            .withOnDrawerListener(DrawerListener(activity))
            .build()
    }

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
        when (drawerItem.identifier.toInt()) {
            CURRENCIES_ID -> startEditableEntityActivity(CurrencyActivity::class.java)
            EXCHANGE_RATES_ID -> startEditableEntityActivityWithFilter(
                ExchangeRateActivity::class.java,
                EXCHANGE_RATE_FILTER,
                activity.exchangeRateFilter,
                EXCHANGE_RATE_CODE
            )
            PEOPLE_ID -> startEditableEntityActivity(PersonActivity::class.java)
            EXPENSE_ARTICLES_ID -> startEditableEntityActivity(ExpenseArticleActivity::class.java)
            INCOME_ARTICLES_ID -> startEditableEntityActivity(IncomeArticleActivity::class.java)
            BACKUP_ID -> startActivity(BackupActivity::class.java)
            SETTINGS_ID -> startActivity(SettingsActivity::class.java)
            CONTACT_ID -> EmailUtils.sendEmail(activity, DeveloperInfo.EMAIL)
        }
        return false
    }

    private fun <F : EntityFilter> startEditableEntityActivityWithFilter(
        activityClass: Class<*>,
        filterName: String,
        filter: F?,
        requestCode: Int
    ) {
        val intent = Intent(activity, activityClass)
            .putExtra(filterName, filter)
            .putExtra(INPUT_READ_ONLY, false)
        startActivityForResult(intent, requestCode)
    }

    private fun startEditableEntityActivity(activityClass: Class<*>) {
        val intent = Intent(activity, activityClass)
        intent.putExtra(INPUT_READ_ONLY, false)
        startActivity(intent)
    }

    private fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(activity, activityClass))
    }

    private fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    private fun startActivityForResult(intent: Intent, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }

    companion object {
        private const val NO_SELECTED_ITEM = -1
        private const val CURRENCIES_ID = 100
        private const val EXCHANGE_RATES_ID = 101
        private const val PEOPLE_ID = 102
        private const val EXPENSE_ARTICLES_ID = 103
        private const val INCOME_ARTICLES_ID = 104
        private const val BACKUP_ID = 200
        private const val SETTINGS_ID = 300
        private const val CONTACT_ID = 301

        private fun createItems(): List<IDrawerItem<*>> {
            val items = mutableListOf<IDrawerItem<*>>()
            items.add(
                PrimaryDrawerItem()
                    .withIdentifier(CURRENCIES_ID.toLong())
                    .withIcon(CommunityMaterial.Icon.cmd_currency_sign)
                    .withName(R.string.drawer_item_currencies)
                    .withSelectable(false)
            )
            items.add(
                PrimaryDrawerItem()
                    .withIdentifier(EXCHANGE_RATES_ID.toLong())
                    .withIcon(CommunityMaterial.Icon.cmd_cash)
                    .withName(R.string.drawer_item_exchange_rates)
                    .withSelectable(false)
            )
            items.add(
                PrimaryDrawerItem()
                    .withIdentifier(PEOPLE_ID.toLong())
                    .withIcon(CommunityMaterial.Icon.cmd_account_multiple)
                    .withName(R.string.drawer_item_people)
                    .withSelectable(false)
            )
            items.add(
                PrimaryDrawerItem()
                    .withIdentifier(EXPENSE_ARTICLES_ID.toLong())
                    .withIcon(CommunityMaterial.Icon2.cmd_trending_down)
                    .withName(R.string.drawer_item_expense_articles)
                    .withSelectable(false)
            )
            items.add(
                PrimaryDrawerItem()
                    .withIdentifier(INCOME_ARTICLES_ID.toLong())
                    .withIcon(CommunityMaterial.Icon2.cmd_trending_up)
                    .withName(R.string.drawer_item_income_articles)
                    .withSelectable(false)
            )
            items.add(DividerDrawerItem())
            items.add(
                SecondaryDrawerItem()
                    .withIdentifier(BACKUP_ID.toLong())
                    .withIcon(CommunityMaterial.Icon2.cmd_sync)
                    .withName(R.string.drawer_item_backup)
                    .withSelectable(false)
            )
            items.add(DividerDrawerItem())
            items.add(
                SecondaryDrawerItem()
                    .withIdentifier(SETTINGS_ID.toLong())
                    .withIcon(FontAwesome.Icon.faw_cog)
                    .withName(R.string.drawer_item_settings)
                    .withSelectable(false)
            )
            items.add(
                SecondaryDrawerItem()
                    .withIdentifier(CONTACT_ID.toLong())
                    .withIcon(FontAwesome.Icon.faw_bullhorn)
                    .withName(R.string.drawer_item_contact)
                    .withSelectable(false)
            )
            return items
        }
    }
}
