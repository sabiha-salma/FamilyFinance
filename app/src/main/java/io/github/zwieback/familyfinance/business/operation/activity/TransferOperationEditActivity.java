package io.github.zwieback.familyfinance.business.operation.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.johnpetitto.validator.ValidatingTextInputLayout;
import com.mikepenz.iconics.view.IconicsImageView;

import org.droidparts.widget.ClearableEditText;

import java.util.Arrays;
import java.util.List;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.business.operation.adapter.TransferOperationProvider;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.Account;
import io.github.zwieback.familyfinance.core.model.Article;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.type.OperationType;
import io.github.zwieback.familyfinance.databinding.ActivityEditTransferOperationBinding;
import io.reactivex.functions.Consumer;

import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.EXPENSE_ACCOUNT_CODE;
import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.INCOME_ACCOUNT_CODE;
import static io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.RESULT_ACCOUNT_ID;

public class TransferOperationEditActivity
        extends OperationEditActivity<ActivityEditTransferOperationBinding> {

    public static final String INPUT_TRANSFER_OPERATION_ID = "transferExpenseOperationId";
    public static final String INPUT_EXPENSE_ACCOUNT_ID = "expenseAccountId";
    public static final String OUTPUT_TRANSFER_OPERATION_ID = "resultTransferExpenseOperationId";

    /**
     * internal entity is alias for expense operation
     */
    private Operation incomeOperation;

    @Override
    protected int getTitleStringId() {
        return R.string.transfer_operation_activity_edit_title;
    }

    @Override
    protected int getBindingLayoutId() {
        return R.layout.activity_edit_transfer_operation;
    }

    @Override
    protected String getExtraInputId() {
        return INPUT_TRANSFER_OPERATION_ID;
    }

    @Override
    protected String getExtraOutputId() {
        return OUTPUT_TRANSFER_OPERATION_ID;
    }

    @Override
    protected EntityProvider<Operation> createProvider() {
        return new TransferOperationProvider(this);
    }

    @Override
    OperationType getOperationType() {
        return OperationType.TRANSFER_EXPENSE_OPERATION;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        switch (requestCode) {
            case EXPENSE_ACCOUNT_CODE:
                if (resultCode != Activity.RESULT_OK) {
                    break;
                }
                int expenseAccountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID);
                loadExpenseAccount(expenseAccountId);
                break;
            case INCOME_ACCOUNT_CODE:
                if (resultCode != Activity.RESULT_OK) {
                    break;
                }
                int incomeAccountId = extractOutputId(resultIntent, RESULT_ACCOUNT_ID);
                loadIncomeAccount(incomeAccountId);
                break;
        }
    }

    public void onExpenseAccountClick(View view) {
        startAccountActivity(EXPENSE_ACCOUNT_CODE);
    }

    public void onIncomeAccountClick(View view) {
        startAccountActivity(INCOME_ACCOUNT_CODE);
    }

    private Consumer<Account> onSuccessfulExpenseAccountFound() {
        return foundAccount -> {
            entity.setAccount(foundAccount);
            binding.expenseAccount.setText(foundAccount.getName());
            if (foundAccount.getOwner() != null) {
                loadOwner(foundAccount.getOwner().getId());
            }
        };
    }

    private Consumer<Account> onSuccessfulIncomeAccountFound() {
        return foundAccount -> {
            incomeOperation.setAccount(foundAccount);
            binding.incomeAccount.setText(foundAccount.getName());
        };
    }

    private Consumer<Article> onSuccessfulArticleFound() {
        return foundArticle -> entity.setArticle(foundArticle);
    }

    private void loadExpenseAccount(int accountId) {
        loadEntity(Account.class, accountId, onSuccessfulExpenseAccountFound());
    }

    private void loadIncomeAccount(int accountId) {
        loadEntity(Account.class, accountId, onSuccessfulIncomeAccountFound());
    }

    private void loadDefaultArticle() {
        int transferArticleId = databasePrefs.getTransferArticleId();
        loadEntity(Article.class, transferArticleId, onSuccessfulArticleFound());
    }

    private int extractExpenseAccountId() {
        return extractInputId(INPUT_EXPENSE_ACCOUNT_ID, databasePrefs.getAccountId());
    }

    @Override
    protected void createEntity() {
        super.createEntity();
        loadExpenseAccount(extractExpenseAccountId());
        loadDefaultArticle();
        createIncomeOperation();
    }

    private void createIncomeOperation() {
        incomeOperation = new Operation();
        incomeOperation.setType(OperationType.TRANSFER_INCOME_OPERATION);
        bindIncomeOperation(incomeOperation);
    }

    @Override
    protected void loadEntity(int expenseOperationId) {
        loadEntity(Operation.class, expenseOperationId, this::onSuccessfulExpenseOperationFound);
    }

    private void onSuccessfulExpenseOperationFound(Operation expenseOperation) {
        bind(expenseOperation);
        loadEntity(Operation.class, expenseOperation.getLinkedTransferOperation().getId(),
                this::bindIncomeOperation);
    }

    @Override
    protected void bind(Operation operation) {
        entity = operation;
        binding.setExpenseOperation(operation);
        provider.setupIcon(binding.icon.getIcon(), operation);
        super.bind(operation);
    }

    private void bindIncomeOperation(Operation operation) {
        incomeOperation = operation;
        binding.setIncomeOperation(operation);
        setupIncomeOperationBindings();
    }

    @Override
    protected void setupBindings() {
        binding.icon.setOnClickListener(this::onSelectIconClick);
        binding.owner.setOnClickListener(this::onOwnerClick);
        binding.expenseAccount.setOnClickListener(this::onExpenseAccountClick);
        binding.expenseAccount.setOnClearTextListener(() -> entity.setAccount(null));
        binding.date.setOnClickListener(this::onDateClick);
        binding.currency.setOnClickListener(this::onCurrencyClick);
        binding.exchangeRate.setOnClickListener(this::onExchangeRateClick);
        super.setupBindings();
    }

    private void setupIncomeOperationBindings() {
        binding.incomeAccount.setOnClickListener(this::onIncomeAccountClick);
        binding.incomeAccount.setOnClearTextListener(() -> incomeOperation.setAccount(null));
    }

    @Override
    protected void updateEntityProperties(Operation operation) {
        super.updateEntityProperties(operation);
        incomeOperation.setArticle(operation.getArticle());
        incomeOperation.setOwner(operation.getOwner());
        incomeOperation.setExchangeRate(operation.getExchangeRate());
        incomeOperation.setDate(operation.getDate());
        incomeOperation.setValue(operation.getValue());
        incomeOperation.setDescription(operation.getDescription());
    }

    @Override
    protected List<ValidatingTextInputLayout> getLayoutsForValidation() {
        return Arrays.asList(
                binding.ownerLayout,
                binding.expenseAccountLayout, binding.incomeAccountLayout,
                binding.valueLayout, binding.dateLayout,
                binding.currencyLayout, binding.exchangeRateLayout);
    }

    @Override
    protected Consumer<Operation> onSuccessfulSaving() {
        return expenseOperation -> {
            incomeOperation.setLinkedTransferOperation(expenseOperation);
            saveEntity(incomeOperation, onSuccessfulIncomeOperationSaving(expenseOperation));
        };
    }

    private Consumer<Operation> onSuccessfulIncomeOperationSaving(Operation expenseOperation) {
        return incomeOperation -> {
            expenseOperation.setLinkedTransferOperation(incomeOperation);
            saveEntity(expenseOperation, onSuccessfulExpenseOperationSaving());
        };
    }

    private Consumer<Operation> onSuccessfulExpenseOperationSaving() {
        return this::closeActivity;
    }

    @Override
    protected IconicsImageView getIconView() {
        return binding.icon;
    }

    @Override
    ClearableEditText getOwnerEdit() {
        return binding.owner;
    }

    @Override
    ClearableEditText getCurrencyEdit() {
        return binding.currency;
    }

    @Override
    ClearableEditText getExchangeRateEdit() {
        return binding.exchangeRate;
    }

    @Override
    EditText getDateEdit() {
        return binding.date;
    }

    @Override
    EditText getValueEdit() {
        return binding.value;
    }

    @Override
    EditText getDescriptionEdit() {
        return binding.description;
    }
}
