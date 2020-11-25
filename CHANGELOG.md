# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.8.0] - 2020-11-25
### Added
- The "To Whom" field to the template and operation entities, and to the operation filters.

## [1.7.3] - 2020-08-03
### Added
- Missed creation and last update dates to operations when adding operations immediately directly from notifications.

## [1.7.2] - 2020-07-27
### Added
- Ability to clear dates in the exchange rate's filter.

### Changed
- Dashboard screen's layout.

### Fixed
- Saving data in chart display dialogs.
- Displaying data in the pie charts after screen rotation.
- Displaying data on the screen when the user clicks the Back button in nested entities (accounts, articles and persons) after screen rotation.

## [1.7.1] - 2020-07-22
### Fixed
- Missing view recreation.

## [1.7.0] - 2020-07-21
### Added
- Support for Android 10 (Q, API level 29).
- Question before restoring the database or preferences.
- "Creation date" and "last change date" fields for entities.
- Sorting operations:
  * By default
  * By creation date
  * By last change date

## [1.6.5] - 2020-01-20
### Added
- Filter preferences (so far only for operations).

### Fixed
- Start of balance calculation.

## [1.6.4] - 2020-01-09
### Fixed
- App crash when calculating summaries with zero values (bar chart and balance of the operations).
- Updating the bar chart axis after screen rotation.

## [1.6.3] - 2019-12-30
### Added
- Profit bar to the bar chart.

## [1.6.2] - 2019-12-27
### Fixed
- An empty value is returned if it is not specified, not zero.

## [1.6.1] - 2019-12-26
### Fixed
- Changing the icon of any entity.

## [1.6.0] - 2019-12-25
### Added
- Indexes to entities with parents (optimized CTE queries).
- ConstraintLayout dependency.
- [RxkPrefs](https://github.com/afollestad/rxkprefs) dependency.

### Changed
- Increased minimum SDK version to 16.
- Migration to AndroidX.
- All code is converted to Kotlin (with coroutines).
- Interface of the main and backup screens.
- Display of inactive accounts.

### Removed
- [Lightweight-Stream-API](https://github.com/aNNiMON/Lightweight-Stream-API) dependency.
- [Gendalf](https://github.com/deviant-studio/Gendalf) dependency.

## [1.5.7] - 2019-10-01
### Added
- Ability to open articles in the specified folder when click on the article category (parent) view.

## [1.5.6] - 2019-05-31
### Fixed
- NPE on reports screen with blank data after display changes (horizontal bar and pie charts).

## [1.5.5] - 2019-05-29
### Added
- Field `common` to the SMS patterns. It is used when required to create a notification about a partially completed
operation (without an article or something else).

### Changed
- Hint of the SMS pattern's `name` field.
- Enabled case-insensitive matching of the SMS patterns.

### Fixed
- Template's widget of the `name` field.

## [1.5.4] - 2019-04-05
### Added
- Preference to show or hide balance on operation screens.

## [1.5.3] - 2019-04-04
### Added
- An action 'Add operation immediately' to the notification from the incoming SMS.

### Changed
- Optimized account's balance calculation.

## [1.5.2] - 2019-03-29
### Fixed
- Bug in ClearableEditText where icon might not be displayed.

## [1.5.1] - 2018-12-27
### Removed
- Inclusion of StrictMode for release version.

## [1.5.0] - 2018-12-27
### Added
- Privacy policy.
- Show rationale in dialog for the RECEIVE_SMS permission.

## [1.4.4] - 2018-10-17
### Changed
- android.hardware.telephony feature now is not required.

### Fixed
- Removing date and value groups from the sms pattern.
- Template removing.

## [1.4.3] - 2018-07-05
### Fixed
- NPE in OperationQueryBuilder.

## [1.4.2] - 2018-06-07
### Added
- URL field to operation and template entities.
- Multidex support.

### Changed
- InputType of dateGroup and valueGroup fields to numberSigned.
- Updated iconics lib and fontawesome typeface versions.
- Updated icon names.
- Updated versions of other libs (dependencies).

## [1.4.1] - 2018-06-06
### Changed
- Database version for auto creating t_sms_pattern table.

## [1.4.0] - 2018-06-06
### Added
- Type, number, payment_system and card_number fields to account entity.
- Ability to select template if it is regularSelectable.
- Sms pattern entity and view.
- Sms receiver, parser, notification builder and sender.

### Changed
- Increased gradle and gradle-wrapper versions.
- Increased support lib version.
- Increased preference compat lib version.
- Optimized layouts initializing.

### Removed
- Unnecessary code.
- Unnecessary initial layouts.

### Fixed
- Long to Integer cast exception.
- Showed class name of OperationViewDestroyer in console.
- Template item icon.

## [1.3.4] - 2018-02-11
### Fixed
- Fatal signal 11 (SIGSEGV) at 0xdeadd00d (code=1), thread 15725 (Compiler) described at [StackOverflow.com](https://stackoverflow.com/q/47904589/8035065).

## [1.3.3] - 2017-12-20
### Changed
- View account's icon if operation's icon is empty.

## [1.3.2] - 2017-12-18
### Changed
- Clarified titles of charts, filters and displays.

### Fixed
- NPE when searching for fragments in the chart activity.

## [1.3.1] - 2017-12-15
### Changed
- Enabled ProGuard (without obfuscation) to reduce the number of methods.

## [1.3.0] - 2017-12-15
### Added
- Field *exchange_rate_value* to the view *v_operation*.
- Destroyer of views.
- Charts.

### Changed
- Renamed OperationCalculator to BalanceCalculator for clarification.
- Creation and destruction of views: it moved to the trampoline scheduler.

### Fixed
- Scrolling in the filter dialogs.

## [1.2.2] - 2017-11-30
### Added
- Calculator to fields that accept decimal numbers.

### Removed
- DroidParts dependency.

## [1.2.1] - 2017-11-29
### Added
- Credits.

## [1.2.0] - 2017-11-29
### Added
- Templates.

### Changed
- Optimized onActivityResult() methods.

## [1.1.2] - 2017-11-28
### Added
- Bindable annotation to field 'parent' of account, article and person entities.

### Removed
- Unnecessary code.

## [1.1.1] - 2017-11-28
### Removed
- Unnecessary code.

### Fixed
- Adding operations from dashboard without filter.
- Popup menu for folder entities.

## [1.1.0] - 2017-11-27
### Added
- Possibility to duplicate any operation.
- Get default field values for new operations from appropriate filter if defined.
- Changelog.

### Changed
- Developer email.

### Removed
- Unused FlowOfFundsOperationProvider.java.
- Default values (except for article) from all operations filters.

### Fixed
- Link to internal entity in javadoc.
- Adding operations from dashboard activity.

## [1.0.2] - 2017-11-23
### Added
- Add quick links and issue checklist to readme.

## [1.0.1] - 2017-11-22
### Added
- Licence.
- Readme.

### Changed
- Allow to edit active field of the account.
- Load owner and currency only for regular accounts when creating a new one.

## [1.0.0] - 2017-11-22
### Added
- Initial version.
