package io.github.zwieback.familyfinance.core.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.johnpetitto.validator.Validators
import io.github.zwieback.familyfinance.app.FamilyFinanceApplication
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID
import io.github.zwieback.familyfinance.core.filter.EntityFilter
import io.github.zwieback.familyfinance.core.listener.EntityFilterListener
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.github.zwieback.familyfinance.util.DateUtils
import io.github.zwieback.familyfinance.util.DateUtils.isTextAnLocalDate
import io.github.zwieback.familyfinance.util.DateUtils.stringToLocalDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import kotlin.coroutines.CoroutineContext

abstract class EntityFilterDialog<F : EntityFilter, B : ViewDataBinding> :
    DialogFragment(),
    CoroutineScope {

    protected lateinit var binding: B
    protected lateinit var filter: F
    protected lateinit var databasePrefs: DatabasePrefs
    private lateinit var data: ReactiveEntityStore<Persistable>
    private lateinit var listener: EntityFilterListener<F>
    private lateinit var rootJob: Job
    private val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract val inputFilterName: String

    @get:StringRes
    protected abstract val dialogTitle: Int

    @get:LayoutRes
    protected abstract val dialogLayoutId: Int

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    protected open val layoutsForValidation: List<ValidatingTextInputLayout>
        get() = emptyList()

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EntityFilterListener<*>) {
            listener = context as EntityFilterListener<F>
            data = ((context as Activity).application as FamilyFinanceApplication).data
        } else {
            throw ClassCastException("$context must implement EntityFilterListener")
        }
        rootJob = Job()
        databasePrefs = runBlocking(Dispatchers.IO) {
            DatabasePrefs.with(context)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputFilter = extractFilter()
        filter = createCopyOfFilter(inputFilter)
        binding = createBinding()
        bind(filter)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(extractDialogTitle())
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // override behavior in #onResume()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // default behavior
            }
            .create()
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (noneErrorFound()) {
                updateFilterProperties()
                listener.onApplyFilter(filter)
                dialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        rootJob.cancel()
        disposables.clear()
        super.onDestroy()
    }

    private fun extractFilter(): F {
        return requireArguments().getParcelable(inputFilterName)
            ?: error("No filter in args by $inputFilterName")
    }

    @StringRes
    private fun extractDialogTitle(): Int {
        return requireArguments().getInt(DIALOG_TITLE, dialogTitle)
    }

    /**
     * A copy is needed not to overwrite the values of the fields of the original filter.
     *
     * @param filter an input filter
     * @return a copy of input filter
     */
    protected abstract fun createCopyOfFilter(filter: F): F

    private fun createBinding(): B {
        val inflater = LayoutInflater.from(requireContext())
        return DataBindingUtil.inflate(inflater, dialogLayoutId, null, false)
    }

    @CallSuper
    protected open fun bind(filter: F) {
        // stub
    }

    protected fun <E : IBaseEntity> loadEntity(
        entityClass: Class<E>,
        entityId: Int,
        onSuccess: Consumer<E>
    ) {
        disposables.add(
            data.findByKey(entityClass, entityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess)
        )
    }

    private fun noneErrorFound(): Boolean {
        return Validators.validate(layoutsForValidation)
    }

    protected abstract fun updateFilterProperties()

    companion object {
        const val DIALOG_TITLE = "dialogTitle"

        fun <F : EntityFilter> createArguments(filterName: String, filter: F): Bundle {
            return Bundle().apply {
                putParcelable(filterName, filter)
            }
        }

        fun extractId(resultIntent: Intent, name: String): Int {
            return resultIntent.getIntExtra(name, EMPTY_ID)
        }

        fun determineDate(dateEdit: EditText, defaultDate: LocalDate?): LocalDate {
            return if (isCorrectDate(dateEdit)) {
                stringToLocalDate(dateEdit.text?.toString()) ?: error("Date is not correct")
            } else {
                defaultDate ?: DateUtils.now()
            }
        }

        private fun isCorrectDate(dateEdit: EditText): Boolean {
            return isTextAnLocalDate(dateEdit.text?.toString())
        }
    }
}
