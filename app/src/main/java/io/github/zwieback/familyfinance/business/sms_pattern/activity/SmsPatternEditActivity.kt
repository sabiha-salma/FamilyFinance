package io.github.zwieback.familyfinance.business.sms_pattern.activity

import android.app.Activity
import android.content.Intent
import com.johnpetitto.validator.ValidatingTextInputLayout
import com.mikepenz.iconics.view.IconicsImageView
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.RESULT_TEMPLATE_ID
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.TEMPLATE_CODE
import io.github.zwieback.familyfinance.business.sms_pattern.adapter.SmsPatternProvider
import io.github.zwieback.familyfinance.business.template.activity.TemplateActivity
import io.github.zwieback.familyfinance.core.activity.EntityEditActivity
import io.github.zwieback.familyfinance.core.adapter.EntityProvider
import io.github.zwieback.familyfinance.core.model.SmsPattern
import io.github.zwieback.familyfinance.core.model.Template
import io.github.zwieback.familyfinance.databinding.ActivityEditSmsPatternBinding
import io.reactivex.functions.Consumer
import org.threeten.bp.LocalDateTime

class SmsPatternEditActivity : EntityEditActivity<SmsPattern, ActivityEditSmsPatternBinding>() {

    override val titleStringId: Int
        get() = R.string.sms_pattern_activity_edit_title

    override val bindingLayoutId: Int
        get() = R.layout.activity_edit_sms_pattern

    override val extraInputId: String
        get() = INPUT_SMS_PATTERN_ID

    override val extraOutputId: String
        get() = OUTPUT_SMS_PATTERN_ID

    override val entityClass: Class<SmsPattern>
        get() = SmsPattern::class.java

    override val layoutsForValidation: List<ValidatingTextInputLayout>
        get() {
            val layouts = mutableListOf(
                binding.nameLayout,
                binding.senderLayout,
                binding.templateNameLayout,
                binding.regexLayout
            )
            if (!binding.dateGroup.text?.toString().isNullOrEmpty()) {
                layouts.add(binding.dateGroupLayout)
            }
            if (!binding.valueGroup.text?.toString().isNullOrEmpty()) {
                layouts.add(binding.valueGroupLayout)
            }
            return layouts
        }

    override val iconView: IconicsImageView
        get() = binding.icon

    override fun createProvider(): EntityProvider<SmsPattern> {
        return SmsPatternProvider(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            TEMPLATE_CODE -> resultIntent?.let {
                val templateId = extractOutputId(resultIntent, RESULT_TEMPLATE_ID)
                loadTemplate(templateId)
            }
        }
    }

    private fun onTemplateClick() {
        val intent = Intent(this, TemplateActivity::class.java)
        startActivityForResult(intent, TEMPLATE_CODE)
    }

    private fun onCommonChange(isChecked: Boolean) {
        if (entity.iconName == null) {
            val smsPatternCopy = entity.copy()
            smsPatternCopy.setCommon(isChecked)
            provider.setupIcon(binding.icon.icon, smsPatternCopy)
        }
    }

    private fun loadTemplate(templateId: Int) {
        loadEntity(Template::class.java, templateId, onSuccessfulTemplateFound())
    }

    private fun onSuccessfulTemplateFound(): Consumer<Template> {
        return Consumer { foundTemplate -> entity.setTemplate(foundTemplate) }
    }

    override fun createEntity() {
        val smsPattern = SmsPattern()
            .setCreateDate(LocalDateTime.now())
        bind(smsPattern)
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun bind(smsPattern: SmsPattern) {
        entity = smsPattern
        binding.smsPattern = smsPattern
        provider.setupIcon(binding.icon.icon, smsPattern)
        super.bind(smsPattern)
    }

    override fun setupBindings() {
        binding.icon.setOnClickListener { onSelectIconClick() }
        binding.templateName.setOnClickListener { this.onTemplateClick() }
        binding.templateName.setOnClearTextListener { entity.setTemplate(null) }
        binding.common.setOnCheckedChangeListener { _, isChecked -> onCommonChange(isChecked) }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun updateEntityProperties(smsPattern: SmsPattern) {
        smsPattern.setLastChangeDate(LocalDateTime.now())
        smsPattern.setName(binding.name.text?.toString())
        smsPattern.setSender(binding.sender.text?.toString())
        smsPattern.setRegex(binding.regex.text?.toString())
        smsPattern.setDateGroup(binding.dateGroup.text?.toString()?.toIntOrNull())
        smsPattern.setValueGroup(binding.valueGroup.text?.toString()?.toIntOrNull())
        smsPattern.setCommon(binding.common.isChecked)
    }

    companion object {
        const val INPUT_SMS_PATTERN_ID = "smsPatternId"
        const val OUTPUT_SMS_PATTERN_ID = "resultSmsPatternId"
    }
}
