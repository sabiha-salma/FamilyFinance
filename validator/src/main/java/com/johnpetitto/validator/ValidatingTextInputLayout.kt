package com.johnpetitto.validator

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

/**
 * An extension of [TextInputLayout] that validates the text of its child
 * [EditText] and displays an error when the input is invalid.
 *
 * Adding a [ValidatingTextInputLayout] to your XML layout file is analogous to
 * adding a [TextInputLayout]:
 * ```
 * <com.johnpetitto.validator.ValidatingTextInputLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      app:errorLabel="@string/some_error">
 *
 *      <EditText
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content" />
 *
 * </com.johnpetitto.validator.ValidatingTextInputLayout>
 * ```
 *
 * To set a [Validator] for your [ValidatingTextInputLayout], call
 * [setValidator]:
 * ```
 * ValidatingTextInputLayout layout = ...
 * layout.setValidator(new Validator() {
 *      public boolean isValid(String input) {
 *          return input.startsWith("J");
 *      }
 * });
 * ```
 *
 * To validate, simply call [validate].
 *
 * There are a handful of predefined validators in [Validators], as well as
 * a utility for validating multiple [Validator] objects at once. You can use
 * either the [EmailValidator] or [PhoneValidator] validators in XML with
 * the `app:validator` tag.
 *
 * See [Introductory Blog Post for Validator](https://web.archive.org/web/20190321040903/http://johnpetitto.com/validator)
 */
class ValidatingTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private var validator: Validator?
    private var errorLabel: String?
    private var validateAfterTextChanged: Boolean
    private var textWatcher: TextWatcher? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ValidatingTextInputLayout)
        val validatorType = a.getInt(R.styleable.ValidatingTextInputLayout_validator, 0)
        validator = when (validatorType) {
            EMAIL_VALIDATOR -> EmailValidator
            PHONE_VALIDATOR -> PhoneValidator
            INTEGER_VALIDATOR -> IntegerValidator
            BIG_DECIMAL_VALIDATOR -> BigDecimalValidator
            DATE_VALIDATOR -> DateValidator
            NOT_EMPTY_VALIDATOR -> NotEmptyValidator
            SIGNED_NUMBER_VALIDATOR -> SignedNumberValidator
            ACCOUNT_NUMBER_VALIDATOR -> AccountNumberValidator
            else -> null
        }
        errorLabel = a.getString(R.styleable.ValidatingTextInputLayout_errorLabel)
        validateAfterTextChanged = a.getBoolean(
            R.styleable.ValidatingTextInputLayout_validateAfterTextChanged,
            false
        )
        a.recycle()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        setValidateAfterTextChanged(validateAfterTextChanged)
    }

    /**
     * Set a [Validator] for validating the contained [EditText] input text.
     */
    fun setValidator(validator: Validator) {
        this.validator = validator
    }

    /**
     * Set the label to show when [validate] returns `false`.
     */
    fun setErrorLabel(@StringRes resId: Int) {
        errorLabel = context.getString(resId)
    }

    /**
     * Add or remove [TextWatcher] for validating the contained [EditText].
     *
     * @param validate if `true` then add [TextWatcher], otherwise - remove it
     */
    private fun setValidateAfterTextChanged(validate: Boolean) {
        validateAfterTextChanged = validate
        if (editText == null) {
            return
        }
        if (validateAfterTextChanged) {
            textWatcher?.let { editText?.removeTextChangedListener(textWatcher) }
            textWatcher = TextWatcherValidator(this)
            editText?.addTextChangedListener(textWatcher)
        } else {
            editText?.removeTextChangedListener(textWatcher)
        }
    }

    /**
     * Validate the contained [EditText]'s input text against the provided
     * [Validator].
     *
     * For validating multiple [ValidatingTextInputLayout] objects at once,
     * call [Validators.validate].
     *
     * @throws IllegalStateException if either no validator has been set or
     * an error is triggered and no error label is set.
     */
    fun validate(): Boolean {
        val input = editText?.text?.toString() ?: error("EditText or its text is empty")
        val valid = validator?.invoke(input)
            ?: error("A Validator must be set; call setValidator first.")

        error = if (valid) {
            null
        } else {
            checkNotNull(errorLabel) {
                "An error label must be set when validating an invalid input; " +
                        "call `setErrorLabel` or `app:errorLabel` first."
            }
            errorLabel
        }
        return valid
    }

    companion object {
        private const val EMAIL_VALIDATOR = 1
        private const val PHONE_VALIDATOR = 2
        private const val INTEGER_VALIDATOR = 3
        private const val BIG_DECIMAL_VALIDATOR = 4
        private const val DATE_VALIDATOR = 5
        private const val NOT_EMPTY_VALIDATOR = 6
        private const val SIGNED_NUMBER_VALIDATOR = 7
        private const val ACCOUNT_NUMBER_VALIDATOR = 8
    }
}
